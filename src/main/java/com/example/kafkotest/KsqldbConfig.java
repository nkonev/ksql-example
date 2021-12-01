package com.example.kafkotest;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.confluent.ksql.api.client.Row;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Configuration
public class KsqldbConfig {

    public static String KSQLDB_SERVER_HOST = "localhost";
    public static int KSQLDB_SERVER_HOST_PORT = 8088;

    private static final Logger LOGGER = LoggerFactory.getLogger(KsqldbConfig.class);

    @Bean(destroyMethod = "close")
    public Client ksqlClient() {
        ClientOptions options = ClientOptions.create()
                .setHost(KSQLDB_SERVER_HOST)
                .setPort(KSQLDB_SERVER_HOST_PORT);
        Client client = Client.create(options);


        client.streamQuery("SELECT * FROM advices_original EMIT CHANGES;")
                .thenAccept(streamedQueryResult -> {
                    LOGGER.info("Query has started. Query ID: " + streamedQueryResult.queryID());
                    RowSubscriber subscriber = new RowSubscriber();
                    streamedQueryResult.subscribe(subscriber);
                }).exceptionally(e -> {
                    LOGGER.error("Request failed: " + e);
                    return null;
                });

        return client;
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
