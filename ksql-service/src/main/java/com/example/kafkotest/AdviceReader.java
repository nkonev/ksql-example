package com.example.kafkotest;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.Row;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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
