package io.tpd.kafkaexample;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record StoppedCarDto(@JsonProperty("LATITUDE") BigDecimal latitude, @JsonProperty("LONGITUDE")BigDecimal longitude) {
}
