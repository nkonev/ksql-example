package com.example.kafkotest;

import io.tpd.kafkaexample.PracticalAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.List;

// https://thepracticaldeveloper.com/2018/11/24/spring-boot-kafka-config/
@EnableScheduling
@SpringBootApplication
public class ConsumerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @KafkaListener(topics = "${tpd.topic-name}", clientIdPrefix = "json")
    public void listenAsObject(
            @Payload List<PracticalAdvice> payloads
    ) {
        for (PracticalAdvice payload : payloads) {
            logger.info("received:  Payload: {}", payload);
        }
    }

}
