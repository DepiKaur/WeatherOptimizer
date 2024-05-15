package com.example.weatheroptimizer.clients.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * @author Depinder Kaur
 * @version 0.1
 * <h2>WeatherData</h2>
 * @date 2024-05-09
 */

public record WeatherData(
        WeatherSource source,
        Temperature temperature,
        Humidity humidity,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
        LocalDateTime validTime)
        implements Comparable<WeatherData> {
    @Override
    public int compareTo(WeatherData data) {
        return temperature.compareTo(data.temperature());
    }
}
