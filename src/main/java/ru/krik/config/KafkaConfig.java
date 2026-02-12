package ru.krik.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import ru.krik.model.RequestDto;
import ru.krik.model.ResponseDto;

import java.time.Duration;

@Configuration
@EnableKafka

public class KafkaConfig {
    KafkaProperties kafkaProperties = new KafkaProperties();


    @Bean
    KafkaMessageListenerContainer<String, ResponseDto> kafkaMessageListenerContainer(
            ConsumerFactory<String,ResponseDto> consumerFactory) {
       String replyTopic = kafkaProperties.getReplyTopic();
       ContainerProperties containerProperties = new ContainerProperties(replyTopic);
       return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
   }

    @Bean
    ReplyingKafkaTemplate<String, RequestDto, ResponseDto> replyingKafkaTemplate(
            ProducerFactory<String, RequestDto> producerFactory,
            KafkaMessageListenerContainer<String, ResponseDto> kafkaMessageListenerContainer
    ) {
        Duration replyTimeout = kafkaProperties.getReplyTimeout();
        var replyingKafkaTemplate = new ReplyingKafkaTemplate<>(producerFactory, kafkaMessageListenerContainer);
        replyingKafkaTemplate.setDefaultReplyTimeout(replyTimeout);
        return replyingKafkaTemplate;
    }

    @Bean
    KafkaTemplate<String, ResponseDto> kafkaTemplate(ProducerFactory<String, ResponseDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,RequestDto>> kafkaListenerContainerFactory(
            ConsumerFactory<String, RequestDto> consumerFactory,
            KafkaTemplate<String, ResponseDto> kafkaTemplate
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, RequestDto>();
        factory.setConsumerFactory(consumerFactory);
        factory.setReplyTemplate(kafkaTemplate);
        return factory;
    }
}
