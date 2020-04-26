package com.github.efitapia.payservice.service;

import java.math.BigDecimal;

public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException(BigDecimal currentBalance) {
        super("Not enough money. Balance: " + currentBalance);
    }

}
