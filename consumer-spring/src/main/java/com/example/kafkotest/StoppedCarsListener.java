package com.example.kafkotest;

import io.tpd.kafkaexample.StoppedCarDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class StoppedCarsListener {
    private static final Logger logger = LoggerFactory.getLogger(StoppedCarsListener.class);

    @KafkaListener(topics = "stopped_cars", clientIdPrefix = "json-stopped-cars", properties = {
            "spring.json.use.type.headers=false",
            "spring.json.value.default.type=io.tpd.kafkaexample.StoppedCarDto"
    })
    public void listenAsUser(
            @Payload(required = false) StoppedCarDto payload
    ) {
        logger.info("received:  Payload: {}", payload);
    }

}
