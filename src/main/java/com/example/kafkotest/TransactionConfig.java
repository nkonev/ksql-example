package com.example.kafkotest;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.ChainedKafkaTransactionManager;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
@EnableTransactionManagement
public class TransactionConfig implements TransactionManagementConfigurer
{

    private static final String CHAINED = "chained";

    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Autowired
    KafkaTransactionManager kafkaTransactionManager;

    @Autowired
    MongoTransactionManager mongoTransactionManager;

//    @Aut

    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return new ChainedKafkaTransactionManager<>(kafkaTransactionManager, mongoTransactionManager);
    }

//    @Bean
//    public KafkaTransactionManager<?, ?> kafkaTransactionManager(ProducerFactory<?, ?> producerFactory) {
//        return new KafkaTransactionManager<>(producerFactory);
//    }

//    @Bean(name = CHAINED)
//    public ChainedKafkaTransactionManager<?, ?> kafkaChainedTransactionManager(KafkaTransactionManager kafkaTransactionManager, MongoTransactionManager mongoTransactionManager) {
//        return new ChainedKafkaTransactionManager<>(kafkaTransactionManager, mongoTransactionManager);
//    }

}
