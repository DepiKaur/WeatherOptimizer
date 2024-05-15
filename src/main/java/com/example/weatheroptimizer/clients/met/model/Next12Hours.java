
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
    "summary",
    "details"
})
public class Next12Hours {

    @JsonProperty("summary")
    private Summary summary;
    @JsonProperty("details")
    private Details__1 details;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Next12Hours() {
    }

    /**
     * 
     * @param summary
     * @param details
     */
    public Next12Hours(Summary summary, Details__1 details) {
        super();
        this.summary = summary;
        this.details = details;
    }

    @JsonProperty("summary")
    public Summary getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    @JsonProperty("details")
    public Details__1 getDetails() {
        return details;
    }

    @JsonProperty("details")
    public void setDetails(Details__1 details) {
        this.details = details;
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
        return "Next12Hours{" +
                "summary=" + summary +
                ", details=" + details +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
