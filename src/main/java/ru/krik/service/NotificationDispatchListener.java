package ru.krik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import ru.krik.model.RequestDto;
import ru.krik.model.ResponseDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationDispatchListener {
    private final WalletServiceImpl walletService;

    @SendTo
    @KafkaListener(topics = "${com.itrum.test.kafka.request-topic}")
    public ResponseDto listen(RequestDto request) {
        log.info("listen received request {}", request);

            switch (request.getOperationType()) {
                case BALANCE -> {
                    return walletService.getBalance(request.getWalletId());
                }
                case DEPOSIT -> {
                    return  walletService.deposit(request.getAmount(), request.getWalletId());
                }
                case WITHDRAW -> {
                    return  walletService.withdraw(request.getAmount(), request.getWalletId());
                }

                default -> {
                    ResponseDto responseDto = new ResponseDto();
                    responseDto.setMessage("Unknown operation type");
                    return responseDto;
            }
        }
    }
}
