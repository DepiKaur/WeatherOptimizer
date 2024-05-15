package com.example.weatheroptimizer.clients.model;

/**
 * @author Depinder Kaur
 * @version 0.1
 * <h2>Temperature</h2>
 * @date 2024-05-09
 */

public record Temperature(Double value, String unit) implements Comparable<Temperature> {
    @Override
    public int compareTo(Temperature temp) {
        return value.compareTo(temp.value());
    }
}
