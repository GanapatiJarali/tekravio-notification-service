package com.tekravio.notification.service.config;

import com.tekravio.notification.service.exception.ValidationException;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {
//    @Bean
//    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
//
//        // Send failed messages to DLQ
//        DeadLetterPublishingRecoverer recoverer =
//                new DeadLetterPublishingRecoverer(kafkaTemplate,
//                        (record, ex) -> new TopicPartition
//                                (record.topic() + ".dlq", record.partition())
//                );
//
//
//        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 3); // 3 retries, 1s delay
//
//        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, fixedBackOff);
//
//
//        errorHandler.addNotRetryableExceptions(
//                IllegalArgumentException.class,
//                ValidationException.class
//        );
//
//        return errorHandler;
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
//            ConsumerFactory<String, Object> consumerFactory,
//            DefaultErrorHandler errorHandler) {
//
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//
//        factory.setConsumerFactory(consumerFactory);
//
//        factory.setCommonErrorHandler(errorHandler);
//
//        return factory;
//    }
}
