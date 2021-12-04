package io.tpd.kafkaexample;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record UserChangeDto (
    @JsonProperty("REGISTERTIME")
    LocalDateTime registerTime,
    @JsonProperty("GENDER")
    String gender,
    @JsonProperty("REGIONID")
    String regionId
){}