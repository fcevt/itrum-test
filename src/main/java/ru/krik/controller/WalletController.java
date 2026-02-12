package ru.krik.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.krik.model.OperationType;
import ru.krik.model.RequestDto;
import ru.krik.model.ResponseDto;
import ru.krik.service.NotificationDispatchService;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class WalletController {
    private final NotificationDispatchService notificationDispatchService;

    @PostMapping("/v1/wallet")
    public ResponseEntity<ResponseDto> makeSyncRequest(@RequestBody RequestDto request) throws ExecutionException, InterruptedException {
            return ResponseEntity.ok(notificationDispatchService.dispatch(request));
    }

    @GetMapping("/v1/wallets/{WALLET_UUID}")
    public ResponseEntity<ResponseDto> getBalanceRequest(@PathVariable String WALLET_UUID) throws ExecutionException, InterruptedException {
        RequestDto requestDto = new RequestDto();
        requestDto.setOperationType(OperationType.BALANCE);
        requestDto.setWalletId(UUID.fromString(WALLET_UUID));
        return ResponseEntity.ok(notificationDispatchService.dispatch(requestDto));
    }
}
