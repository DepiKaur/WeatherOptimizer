
package com.example.weatheroptimizer.clients.met.model;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "precipitation_amount"
})

public class Details__3 {

    @JsonProperty("precipitation_amount")
    private Double precipitationAmount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Details__3() {
    }

    /**
     * 
     * @param precipitationAmount
     */
    public Details__3(Double precipitationAmount) {
        super();
        this.precipitationAmount = precipitationAmount;
    }

    @JsonProperty("precipitation_amount")
    public Double getPrecipitationAmount() {
        return precipitationAmount;
    }

    @JsonProperty("precipitation_amount")
    public void setPrecipitationAmount(Double precipitationAmount) {
        this.precipitationAmount = precipitationAmount;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Details__3{" +
                "precipitationAmount=" + precipitationAmount +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
