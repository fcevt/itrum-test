package ru.krik.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.krik.model.RequestDto;
import ru.krik.model.ResponseDto;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaSyncConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.request-topic}")
    private String requestTopic;

    @Value("${app.kafka.response-topic}")
    private String responseTopic;

    @Bean
    public NewTopic requestTopic() {
        return TopicBuilder.name(requestTopic)
                .partitions(3)      // 3 раздела
                .replicas(1)       // 1 репликация
                .build();
    }

    @Bean
    public NewTopic replyTopic() {
        return TopicBuilder.name(responseTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public ProducerFactory<String, RequestDto> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean(name = "producerResponseFactory")
    public ProducerFactory<String, ResponseDto> producerResponseFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    @Bean
    public ConsumerFactory<String, ResponseDto> consumerFactory() {
        JsonDeserializer<ResponseDto> deserializer = new JsonDeserializer<>();
//        deserializer.addTrustedPackages("com.example.demo");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sync-response-group");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
//        , new StringDeserializer(), deserializer
    }

    @Bean
    public KafkaMessageListenerContainer<String, ResponseDto> kafkaMessageListenerContainer() {
        ContainerProperties containerProperties = new ContainerProperties(responseTopic);
        return new KafkaMessageListenerContainer<>(consumerFactory(), containerProperties);
    }

    @Bean
    public ReplyingKafkaTemplate<String, RequestDto, ResponseDto> replyingKafkaTemplate() {
        ReplyingKafkaTemplate<String, RequestDto, ResponseDto> template =
                new ReplyingKafkaTemplate<>(producerFactory(), kafkaMessageListenerContainer());
        template.setDefaultReplyTimeout(Duration.ofMillis(5000));
        return template;
    }
    @Bean
    public KafkaTemplate<String, ResponseDto> replyKafkaTemplate() {
        return new KafkaTemplate<>(producerResponseFactory());
    }
}
