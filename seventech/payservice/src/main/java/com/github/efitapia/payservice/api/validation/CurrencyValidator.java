package com.github.efitapia.payservice.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class CurrencyValidator implements ConstraintValidator<Currency, BigDecimal> {
    public void initialize(Currency constraint) {
    }

    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return value != null
            && value.compareTo(ZERO) >= 0
            && Math.abs(value.scale()) <= 2;
    }
}
