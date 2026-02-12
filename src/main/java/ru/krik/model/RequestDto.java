package ru.krik.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.krik.model.annotation.ValidOperationType;

import java.util.UUID;


@Data
public class RequestDto {

    @NotNull
    private UUID walletId;

    @NotNull
    @ValidOperationType
    private OperationType operationType;

    @Min(100)
    Double amount;
}
