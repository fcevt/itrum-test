package ru.krik.controller;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.krik.model.RequestDTO;
import ru.krik.model.ResponseDto;
import ru.krik.service.ProducerService;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("api/v1/wallet")
@RequiredArgsConstructor
public class ValletController {
    private final ProducerService producerService;

    @PostMapping
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody RequestDTO requestDTO) {
        try {
            producerService.sendMessage("my-topic", requestDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
