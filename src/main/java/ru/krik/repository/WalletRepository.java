package ru.krik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.krik.model.Wallet;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    double findWalletByWalletId(UUID walletId);
}
