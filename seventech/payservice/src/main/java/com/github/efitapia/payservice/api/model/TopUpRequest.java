package com.github.efitapia.payservice.api.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopUpRequest {
    private Long accountId;
    private BigDecimal amount;
}
