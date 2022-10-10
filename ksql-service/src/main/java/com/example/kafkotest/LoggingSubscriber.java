package com.example.kafkotest;

import io.confluent.ksql.api.client.Row;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggingSubscriber implements Subscriber<Row> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingSubscriber.class);

    private Subscription subscription;

    public LoggingSubscriber() {
    }

    @Override
    public synchronized void onSubscribe(Subscription subscription) {
        LOGGER.info("Subscriber is subscribed.");
        this.subscription = subscription;

        // Request the first row
        this.subscription.request(1);
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
        subscription.cancel();
    }

    @Override
    public synchronized void onComplete() {
        LOGGER.info("Query has ended.");
        subscription.cancel();
    }
}
