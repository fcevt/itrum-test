package ru.krik.service;

import ru.krik.model.ResponseDto;

import java.util.UUID;

public interface WalletService {
    ResponseDto deposit(double amount, UUID walletId);
    ResponseDto withdraw(double amount, UUID walletId);
    ResponseDto getBalance(UUID walletId);

}
