package com.example.weatheroptimizer.exceptions;

/**
 * @author Depinder Kaur
 * @version 0.1
 * <h2>ErrorMessage</h2>
 * @date 2024-05-13
 */

public class ErrorMessage {

    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
