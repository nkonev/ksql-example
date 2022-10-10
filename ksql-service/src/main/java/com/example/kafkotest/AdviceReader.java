package com.example.kafkotest;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.Row;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class AdviceReader implements KsqldbClientCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdviceReader.class);

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
                    throw new RuntimeException(e);
                });
    }
}
