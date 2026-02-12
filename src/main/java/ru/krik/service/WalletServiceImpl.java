package ru.krik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krik.model.ResponseDto;
import ru.krik.model.Wallet;
import ru.krik.repository.WalletRepository;
import ru.krik.util.Util;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    @Override
    public ResponseDto getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        ResponseDto responseDto = new ResponseDto();
        if (wallet == null) {
            responseDto.setMessage("Кошелька не существует");
            return responseDto;
        }
        responseDto.setBalance(wallet.getBalance());
        return responseDto;
    }

    @Override
    public ResponseDto withdraw(double amount, UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        ResponseDto responseDto = new ResponseDto();
        if (wallet == null) {
            responseDto.setMessage("Кошелька не существует");
            return responseDto;
        }

        if (wallet.getBalance() < Util.convertDollarsToCents(amount)) {
            responseDto.setMessage("Недостаточно средств");
            return responseDto;
        }
        wallet.setBalance(wallet.getBalance() - Util.convertDollarsToCents(amount));
        walletRepository.save(wallet);
        responseDto.setBalance(Util.convertCentsToDollars(wallet.getBalance()));
        return responseDto;
    }

    @Override
    public ResponseDto deposit(double amount, UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        ResponseDto responseDto = new ResponseDto();
        if (wallet == null) {
            responseDto.setMessage("Кошелька не существует");
            return responseDto;
        }
        wallet.setBalance(wallet.getBalance() + Util.convertDollarsToCents(amount));
        walletRepository.save(wallet);
        responseDto.setBalance(Util.convertCentsToDollars(wallet.getBalance()));
        return responseDto;
    }
}
