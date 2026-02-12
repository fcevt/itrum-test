package ru.krik.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;
import ru.krik.config.KafkaProperties;
import ru.krik.model.RequestDto;
import ru.krik.model.ResponseDto;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
public class NotificationDispatchService {

    private final KafkaProperties kafkaProperties;
    private final ReplyingKafkaTemplate<String, RequestDto, ResponseDto>  replyingKafkaTemplate;

    public ResponseDto dispatch(RequestDto notificationDispatchRequest) throws ExecutionException, InterruptedException {
        String requestTopic = kafkaProperties.getRequestTopic();
        ProducerRecord<String, RequestDto> producerRecord = new ProducerRecord<>(requestTopic, notificationDispatchRequest);

        var requestReplyFuture = replyingKafkaTemplate.sendAndReceive(producerRecord);
        return requestReplyFuture.get().value();
    }
}
