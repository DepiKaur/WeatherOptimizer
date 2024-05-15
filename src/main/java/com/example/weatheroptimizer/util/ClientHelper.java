package com.example.weatheroptimizer.util;

import com.example.weatheroptimizer.exceptions.ClientException;
import com.example.weatheroptimizer.exceptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;

/**
 * @author Depinder Kaur
 * @version 0.1
 * <h2>ClientHelper</h2>
 * @date 2024-05-09
 */

public class ClientHelper {
    public static boolean checkTimeInRange(LocalDateTime inputTime){
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime wantedTime = currentTime.plusHours(24);
        LocalDateTime lowerRange = wantedTime.minusMinutes(30);
        LocalDateTime upperRange = wantedTime.plusMinutes(30);

        if((inputTime.isBefore(upperRange) || inputTime.isEqual(upperRange)) && (inputTime.isAfter(lowerRange) || inputTime.isEqual(lowerRange)) ){
            return true;
        }
        return false;
    }

    public static Exception handleWebClientResponseException(WebClientResponseException ex) {
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND || ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return new ClientException(ex.getResponseBodyAs(ErrorMessage.class).getMessage());
        }
        return ex;
    }
}
