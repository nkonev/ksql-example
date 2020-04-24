package com.example.kafkotest;

import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.kafka.transaction.ChainedKafkaTransactionManager;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
@EnableTransactionManagement
public class TransactionConfig implements TransactionManagementConfigurer {

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        MongoTransactionManager mongoTransactionManager = new MongoTransactionManager(dbFactory);
        mongoTransactionManager.setOptions(TransactionOptions.builder().writeConcern(WriteConcern.JOURNALED).build());
        return mongoTransactionManager;
    }

    @Autowired
    private KafkaTransactionManager kafkaTransactionManager;

    @Autowired
    private MongoTransactionManager mongoTransactionManager;

    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return new ChainedKafkaTransactionManager<>(kafkaTransactionManager, mongoTransactionManager);
    }

}
