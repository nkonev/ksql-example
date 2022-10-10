package com.example.kafkotest;

import io.confluent.ksql.api.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
class AdviceReader implements KsqldbClientCustomizer, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdviceReader.class);

    private ApplicationContext applicationContext;

    @Override
    public void accept(Client client) {
        client.streamQuery("SELECT * FROM advices_original EMIT CHANGES;")
                .thenAccept(streamedQueryResult -> {
                    // https://www.confluent.io/blog/announcing-ksqldb-0-25-1/#at-least-once-semantics
                    LOGGER.info("Query has started. Query ID: " + streamedQueryResult.queryID());
                    LoggingSubscriber subscriber = new LoggingSubscriber();
                    streamedQueryResult.subscribe(subscriber);
                }).exceptionally(e -> {
                    LOGGER.error("Request failed: " + e);
                    SpringApplication.exit(applicationContext, () -> 1);
                    return null;
                });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
