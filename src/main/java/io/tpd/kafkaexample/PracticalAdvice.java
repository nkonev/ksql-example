package io.tpd.kafkaexample;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class PracticalAdvice {
    private final String message;
    private final int identifier;
    private final LocalDateTime datetime;

    public PracticalAdvice(@JsonProperty("message") final String message,
                           @JsonProperty("identifier") final int identifier,
                           @JsonProperty("datetime") LocalDateTime datetime) {
        this.message = message;
        this.identifier = identifier;
        this.datetime = datetime;
    }

    public String getMessage() {
        return message;
    }

    public int getIdentifier() {
        return identifier;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    @Override
    public String toString() {
        return "PracticalAdvice{" +
                "message='" + message + '\'' +
                ", identifier=" + identifier +
                ", datetime=" + datetime +
                '}';
    }
}