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
                    RowSubscriber subscriber = new RowSubscriber();
                    streamedQueryResult.subscribe(subscriber);
                }).exceptionally(e -> {
                    LOGGER.error("Request failed: " + e);
                    return null;
                });

    }
}

class RowSubscriber implements Subscriber<Row> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RowSubscriber.class);

    private Subscription subscription;

    public RowSubscriber() {
    }

    @Override
    public synchronized void onSubscribe(Subscription subscription) {
        LOGGER.info("Subscriber is subscribed.");
        this.subscription = subscription;

        // Request the first row
        subscription.request(1);
    }

    @Override
    public synchronized void onNext(Row row) {
        LOGGER.info("Received a row from ksql client: " + row.values());

        // Request the next row
        subscription.request(1);
    }

    @Override
    public synchronized void onError(Throwable t) {
        LOGGER.error("Received an error: " + t);
    }

    @Override
    public synchronized void onComplete() {
        LOGGER.info("Query has ended.");
    }
}
