package com.github.efitapia.payservice.api.model;

import com.github.efitapia.payservice.api.validation.Currency;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TopUpRequest {

    @Min(1) @NotNull
    private Long accountId;

    @Currency
    private BigDecimal amount;

}
