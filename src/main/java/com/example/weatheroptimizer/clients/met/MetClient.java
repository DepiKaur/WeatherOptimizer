package com.example.weatheroptimizer.clients.met;

import com.example.weatheroptimizer.clients.WeatherDataProvider;
import com.example.weatheroptimizer.clients.met.model.Met;
import com.example.weatheroptimizer.clients.met.model.Timeseries;
import com.example.weatheroptimizer.clients.model.Humidity;
import com.example.weatheroptimizer.clients.model.Temperature;
import com.example.weatheroptimizer.clients.model.WeatherData;
import com.example.weatheroptimizer.clients.model.WeatherSource;
import com.example.weatheroptimizer.util.ClientHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Depinder Kaur
 * @version 0.1
 * <h2>MetClient</h2>
 * @date 2024-05-09
 */

@Component
public class MetClient  implements WeatherDataProvider {
    private WebClient client = WebClient.create();
    private Met metData;

    public Met getMetData() {
        Mono<Met> mono = client
                .get()
                .uri("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=59.3110&lon=18.0300")
                .retrieve()
                .bodyToMono(Met.class)
                .onErrorMap(WebClientResponseException.class, ex -> ClientHelper.handleWebClientResponseException(ex));

        return metData = mono.block();
    }

    private Optional<Timeseries> getTime(Met metData) {
        List<Timeseries> timeseries = metData.getProperties().getTimeseries();

        for (Timeseries t : timeseries) {
            if (ClientHelper.checkTimeInRange(t.getTime())) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    private Temperature getTemperature(Timeseries timeseries) {
        return new Temperature(timeseries.getData().getInstant().getDetails().getAirTemperature(),metData.getProperties().getMeta().getUnits().getAirTemperature());
    }

    private Humidity getHumidity(Timeseries timeseries){
        return new Humidity(timeseries.getData().getInstant().getDetails().getRelativeHumidity(), metData.getProperties().getMeta().getUnits().getRelativeHumidity());
    }

    @Override
    public WeatherData predictWeather() {
        Met metData = getMetData();
        Optional<Timeseries> tomorrowsData = getTime(metData);
        if(tomorrowsData.isPresent()){
            return new WeatherData(WeatherSource.MET,
                    getTemperature(tomorrowsData.get()),
                    getHumidity(tomorrowsData.get()),
                    tomorrowsData.get().getTime());
        }
        else {
            throw new NoSuchElementException("No weather data found for tomorrow");
        }
    }

}
