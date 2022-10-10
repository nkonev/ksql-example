package com.example.kafkotest;

import io.confluent.ksql.api.client.Client;

import java.util.function.Consumer;

interface KsqldbClientCustomizer extends Consumer<Client> {
}
