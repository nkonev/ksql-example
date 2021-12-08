package com.example.kafkotest;

import io.tpd.kafkaexample.StoppedCarDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class StoppedCarsListener {
    private static final Logger logger = LoggerFactory.getLogger(StoppedCarsListener.class);

    @KafkaListener(topics = "stopped_cars", clientIdPrefix = "json-stopped-cars", properties = {
            "spring.json.use.type.headers=false",
            "spring.json.value.default.type=io.tpd.kafkaexample.StoppedCarDto",
            "key.deserializer=org.apache.kafka.common.serialization.StringDeserializer",
    })
    public void listenAsUser(
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key,
            @Payload(required = false) StoppedCarDto payload
    ) {
        logger.info("received:  Payload: key {}, {}", key, payload);
    }

}
