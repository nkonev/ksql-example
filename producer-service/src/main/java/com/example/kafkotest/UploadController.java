package com.example.kafkotest;

import io.tpd.kafkaexample.CoordinatesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
public class UploadController {

    @Autowired
    private KafkaTemplate<String, CoordinatesDto> kafkaTemplate;

    private static final String COMMA_DELIMITER = ",";

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Value("${coordinates.topic-name}")
    private String coordinatesTopicName;

    @PostMapping("/upload")
    public void post(MultipartFile file) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                // https://yandex.ru/map-constructor/location-tool/?from=club
                final var carId = values[0];
                final var coordinatesDto = new CoordinatesDto(Double.parseDouble(values[1]), Double.parseDouble(values[2]));
                final var timestamp = Long.valueOf(values[3]);
                LOGGER.info("Producing carId={}, coordinatesDto={}", carId, coordinatesDto);
                kafkaTemplate.send(coordinatesTopicName, null, timestamp, carId, coordinatesDto);
            }
        }
    }
}
