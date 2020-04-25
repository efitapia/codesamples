package com.github.efitapia.payservice.api.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    private Long senderAccountId;
    private Long recipientAccountId;
    private BigDecimal amount;
}
