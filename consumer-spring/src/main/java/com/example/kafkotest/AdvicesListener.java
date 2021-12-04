package com.example.kafkotest;

import io.tpd.kafkaexample.PracticalAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdvicesListener {
    private static final Logger logger = LoggerFactory.getLogger(AdvicesListener.class);

    @KafkaListener(topics = "${tpd.topic-name}", clientIdPrefix = "json-advices")
    public void listenAsObject(
            @Payload List<PracticalAdvice> payloads
    ) {
        for (PracticalAdvice payload : payloads) {
            logger.info("received:  Payload: {}", payload);
        }
    }
}
