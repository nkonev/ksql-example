package com.example.kafkotest;

import io.tpd.kafkaexample.CoordinatesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UploadController {

    @Autowired
    private KafkaTemplate<String, CoordinatesDto> kafkaTemplate;

    private static final String COMMA_DELIMITER = ",";

    @Value("${coordinates.topic-name}")
    private String coordinatesTopicName;

    @PostMapping("/upload")
    public void post(MultipartFile file) throws IOException {
        //file.getBytes()
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                // https://yandex.ru/map-constructor/location-tool/?from=club
                // carId, latitude, longitude
                kafkaTemplate.send(coordinatesTopicName, values[0], new CoordinatesDto(Double.parseDouble(values[1]), Double.parseDouble(values[2])));
            }
        }
    }
}
