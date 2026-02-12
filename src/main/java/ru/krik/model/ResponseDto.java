package ru.krik.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResponseDto {

    UUID responseId;
    double balance;
    String message;
    private LocalDateTime timestamp;
    String status;
}
