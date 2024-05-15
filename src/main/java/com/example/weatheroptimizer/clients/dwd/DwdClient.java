package com.example.weatheroptimizer.clients.dwd;

import com.example.weatheroptimizer.clients.dwd.model.Dwd;
import com.example.weatheroptimizer.clients.WeatherDataProvider;
import com.example.weatheroptimizer.clients.model.Humidity;
import com.example.weatheroptimizer.clients.model.Temperature;
import com.example.weatheroptimizer.clients.model.WeatherData;
import com.example.weatheroptimizer.clients.model.WeatherSource;
import com.example.weatheroptimizer.exceptions.ClientException;
import com.example.weatheroptimizer.exceptions.ErrorMessage;
import com.example.weatheroptimizer.util.ClientHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Depinder Kaur
 * @version 0.1
 * <h2>DwdClient</h2>
 * @date 2024-05-09
 */

@Service
public class DwdClient implements WeatherDataProvider {
    private static final String ENTRY_POINT = "https://api.open-meteo.com/v1";
    private WebClient webClient;

    public DwdClient() {
        webClient = WebClient.builder()
                .baseUrl(ENTRY_POINT)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private Dwd getDwdData() {
        Mono<Dwd> mono = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/dwd-icon")
                        .queryParam("latitude", "59.3094")
                        .queryParam("longitude", "18.0234")
                        .queryParam("hourly",
                                String.join(",", "temperature_2m", "relative_humidity_2m", "precipitation"))
                        .queryParam("timezone", "{timezone}")
                        .queryParam("forecast_days", "2")
                        .build("Europe/Berlin"))
                .retrieve()
                .bodyToMono(Dwd.class)
                .onErrorMap(WebClientResponseException.class, ex -> ClientHelper.handleWebClientResponseException(ex));

        return mono.block();
    }

    private Optional<String> getTimeFromDwdData(Dwd dwdData) {
        for (String dateTime : dwdData.getHourly().getTime()) {
            if (ClientHelper.checkTimeInRange(LocalDateTime.parse(dateTime))) {
                return Optional.of(dateTime);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> getIndexOfTimeInHourlyList(Dwd dwdData, String dateTime) {
        if (dwdData.getHourly().getTime().contains(dateTime)) {
            return Optional.of(dwdData.getHourly().getTime().indexOf(dateTime));
        }
        return Optional.empty();
    }

    private Temperature getTemperature(Dwd dwdData, String dateTime) {
        Optional<Integer> opIndex = getIndexOfTimeInHourlyList(dwdData, dateTime);
        if (opIndex.isPresent()) {
            int index = opIndex.get();
            if (dwdData.getHourly().getTemperature2m().size() > index) {
                return new Temperature(dwdData.getHourly().getTemperature2m().get(index),
                        dwdData.getHourlyUnits().getTemperature2m());
            }
        }
        throw new NoSuchElementException("Temperature data missing for desired time");
    }

    private Humidity getHumidity(Dwd dwdData, String dateTime) {
        Optional<Integer> opIndex = getIndexOfTimeInHourlyList(dwdData, dateTime);
        if (opIndex.isPresent()) {
            int index = opIndex.get();
            if (dwdData.getHourly().getRelativeHumidity2m().size() > index) {
                return new Humidity((double) dwdData.getHourly().getRelativeHumidity2m().get(index),
                        dwdData.getHourlyUnits().getRelativeHumidity2m());
            }
        }
        throw new NoSuchElementException("Humidity data missing for desired time");
    }

    @Override
    public WeatherData predictWeather() {
        Dwd dwdData = getDwdData();
        Optional<String> optionalDateTime = getTimeFromDwdData(dwdData);
        if (optionalDateTime.isPresent()) {
            String dateTime = optionalDateTime.get();
            return new WeatherData(WeatherSource.DWD,
                    getTemperature(dwdData,dateTime),
                    getHumidity(dwdData,dateTime),
                    LocalDateTime.parse(dateTime));
        } else {
            throw new NoSuchElementException("No weather data found for tomorrow");
        }
    }
}
