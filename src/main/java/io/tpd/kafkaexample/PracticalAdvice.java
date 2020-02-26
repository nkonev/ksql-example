package io.tpd.kafkaexample;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class PracticalAdvice {

    @Id
    private String identifier;
    private String message;
    private LocalDateTime datetime;

    public PracticalAdvice(
            @JsonProperty("identifier") final String identifier,
            @JsonProperty("message") final String message,
            @JsonProperty("datetime") LocalDateTime datetime) {
        this.message = message;
        this.identifier = identifier;
        this.datetime = datetime;
    }

    public String getMessage() {
        return message;
    }

    public String getIdentifier() {
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

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}