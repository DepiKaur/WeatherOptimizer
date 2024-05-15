package com.example.weatheroptimizer.util;

import java.time.LocalDateTime;

/**
 * @author Depinder Kaur
 * @version 0.1
 * <h2>CustomDateTimeFormatter</h2>
 * @date 2024-05-09
 */

public class CustomDateTimeFormatter {

    public static final java.time.format.DateTimeFormatter FORMATTER = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String getFormattedDateTime(LocalDateTime dateTime) {
        return FORMATTER.format(dateTime);
    }
}

