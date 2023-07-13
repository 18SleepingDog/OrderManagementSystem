package com.order.system.dto.request;

import com.order.system.annotation.LatitudeLongitudeListPattern;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CustomerCreateOrderRequest {
    @JsonProperty("origin")
    @NotNull(message = "The origin list must be provided.")
    @LatitudeLongitudeListPattern(message = "The origin list must contain exactly two coordinates(latitude, longitude). Hence, latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180.")
    private List<String> originLatitudeLongitudeListPattern;

    @JsonProperty("destination")
    @NotNull(message = "The destination list must be provided.")
    @LatitudeLongitudeListPattern(message = "The destination list must contain exactly two coordinates(latitude, longitude). Hence, latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180.")
    private List<String> destinationLatitudeLongitudeListPattern;
}
