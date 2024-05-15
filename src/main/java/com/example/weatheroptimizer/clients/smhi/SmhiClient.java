package com.example.weatheroptimizer.clients.smhi;

import com.example.weatheroptimizer.clients.smhi.model.Parameter;
import com.example.weatheroptimizer.clients.smhi.model.Smhi;
import com.example.weatheroptimizer.clients.smhi.model.TimeSeries;
import com.example.weatheroptimizer.clients.WeatherDataProvider;
import com.example.weatheroptimizer.clients.model.Humidity;
import com.example.weatheroptimizer.clients.model.Temperature;
import com.example.weatheroptimizer.clients.model.WeatherData;
import com.example.weatheroptimizer.clients.model.WeatherSource;
import com.example.weatheroptimizer.util.ClientHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Depinder Kaur
 * @version 0.1
 * <h2>SmhiClient</h2>
 * @date 2024-05-09
 */

@Service
public class SmhiClient implements WeatherDataProvider {

    private static final String ENTRY_POINT = "https://opendata-download-metfcst.smhi.se/";
    private static final String GET_DATA = "/api/category/pmp3g/version/2/geotype/point/lon/18.0300/lat/59.3110/data.json";
    private WebClient webClient;

    public SmhiClient() {
        webClient = WebClient.builder()
                .baseUrl(ENTRY_POINT)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private Smhi getSmhiData() {
        Mono<Smhi> mono = webClient
                .get()
                .uri(GET_DATA)
                .retrieve()
                .bodyToMono(Smhi.class)
                .onErrorMap(WebClientResponseException.class, ex -> ClientHelper.handleWebClientResponseException(ex));

        return mono.block();
    }

    private Optional<TimeSeries> getTimeSeriesFromSmhiData(Smhi smhiData) {
        for (TimeSeries t : smhiData.getTimeSeries()) {
            if (ClientHelper.checkTimeInRange(t.getValidTime())) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    private Temperature getTemperature(TimeSeries timeSeries) {
        for (Parameter parameter : timeSeries.getParameters()) {
            if (parameter.getName().equalsIgnoreCase("t")) {
                return new Temperature(parameter.getValues().get(0), parameter.getUnit());
            }
        }
        throw new NoSuchElementException("Temperature data missing for desired time");
    }

    private Humidity getHumidity(TimeSeries timeSeries) {
        for (Parameter parameter : timeSeries.getParameters()) {
            if (parameter.getName().equalsIgnoreCase("r")) {
                return new Humidity((double) Math.toIntExact(Math.round(parameter.getValues().get(0))),
                                                                                parameter.getUnit());
            }
        }
        throw new NoSuchElementException("Humidity data missing for desired time");
    }

    @Override
    public WeatherData predictWeather() {
        Smhi smhiData = getSmhiData();
        Optional<TimeSeries> tomorrowsData = getTimeSeriesFromSmhiData(smhiData);
        if (tomorrowsData.isPresent()) {
            return new WeatherData(WeatherSource.SMHI,
                    getTemperature(tomorrowsData.get()),
                    getHumidity(tomorrowsData.get()),
                    tomorrowsData.get().getValidTime());
        } else {
            throw new NoSuchElementException("No weather data found for tomorrow");
        }
    }
}

