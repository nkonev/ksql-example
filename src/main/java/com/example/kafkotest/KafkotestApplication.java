package com.example.kafkotest;

import io.tpd.kafkaexample.PracticalAdvice;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// https://thepracticaldeveloper.com/2018/11/24/spring-boot-kafka-config/
@EnableScheduling
@SpringBootApplication
public class KafkotestApplication {

	private static final Logger logger = LoggerFactory.getLogger(KafkotestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KafkotestApplication.class, args);
	}

	@Value("${tpd.topic-name}")
	private String topicName;

	@Autowired
	private KafkaTemplate<String, Object> template;

	@Transactional
	@KafkaListener(topics = "${tpd.topic-name}", clientIdPrefix = "json")
	public void listenAsObject(
			@Payload List<PracticalAdvice> payloads
	) {
		for (PracticalAdvice payload: payloads) {
			logger.info("received:  Payload: {}", payload);
		}
	}

	@Bean
	public NewTopic adviceTopic() {
		return new NewTopic(topicName, 1, (short) 1);
	}

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
