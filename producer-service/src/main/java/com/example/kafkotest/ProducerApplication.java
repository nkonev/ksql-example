package com.example.kafkotest;

import io.tpd.kafkaexample.PracticalAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

// https://thepracticaldeveloper.com/2018/11/24/spring-boot-kafka-config/
@EnableScheduling
@SpringBootApplication
public class ProducerApplication {

	private static final Logger logger = LoggerFactory.getLogger(ProducerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Value("${tpd.topic-name}")
	private String topicName;

	@Autowired
	private KafkaTemplate<String, Object> template;

	private static final AtomicLong al = new AtomicLong();

	@Scheduled(cron = "* * * * * *")
	public void run() {
		long longValue = al.incrementAndGet();
		logger.info("Producing {}", longValue);
		this.template.send(
				topicName,
				String.valueOf(longValue),
				new PracticalAdvice(String.valueOf(longValue), "A Practical Advice Number " + longValue, LocalDateTime.now())
		);
	}

}
