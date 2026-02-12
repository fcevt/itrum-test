package ru.krik.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "wallets")
@RequiredArgsConstructor
@Getter
@Setter
public class Wallet {

    @Id
    @Column(name = "id")
    private UUID walletId;

    private Long balance;
}
