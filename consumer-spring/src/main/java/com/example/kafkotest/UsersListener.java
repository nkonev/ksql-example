package com.example.kafkotest;

import io.tpd.kafkaexample.UserChangeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UsersListener{
    private static final Logger logger = LoggerFactory.getLogger(UsersListener.class);

    @KafkaListener(topics = "users", clientIdPrefix = "json-users", properties = {
            "spring.json.value.default.type=io.tpd.kafkaexample.UserChangeDto",
            "key.deserializer=org.apache.kafka.common.serialization.LongDeserializer",
    })
    public void listenAsUser(
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) Long key,
            @Payload(required = false) UserChangeDto payload
    ) {
        logger.info("received:  Payload: key {}, {}", key, payload);
    }

}
