package io.tpd.kafkaexample;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record UserChangeDto (
    @JsonProperty("REGISTERTIME")
    Instant registerTime,
    @JsonProperty("GENDER")
    String gender,
    @JsonProperty("REGIONID")
    String regionId
){}