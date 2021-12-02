package com.example.kafkotest;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Consumer;

@Configuration
public class KsqldbConfig {

    @Value("${ksql.host}")
    private String host;

    @Value("${ksql.port}")
    private int port;

    @Bean(destroyMethod = "close")
    public Client ksqlClient(KsqldbClientCustomizer... customizers) {
        ClientOptions options = ClientOptions.create()
                .setHost(host)
                .setPort(port);
        Client client = Client.create(options);

        if (customizers != null) {
            for (KsqldbClientCustomizer clientCustomizer : customizers) {
                clientCustomizer.accept(client);
            }
        }

        return client;
    }
}

interface KsqldbClientCustomizer extends Consumer<Client> {}
