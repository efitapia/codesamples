package com.github.efitapia.payservice.api.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runners.Parameterized;

import javax.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyValidatorTest {

    private static final ConstraintValidatorContext DUMMY_CONTEXT = null;

    private final CurrencyValidator validator = new CurrencyValidator();

    @Test
    void shouldReturnFalseWhenValueIsNull() {
        assertFalse(validator.isValid(null, DUMMY_CONTEXT));
    }

    @Test
    void shouldReturnFalseWhenValueIsNegative() {
        BigDecimal value = BigDecimal.valueOf(-3.0);
        assertFalse(validator.isValid(value, DUMMY_CONTEXT));
    }

    @Test
    void shouldReturnFalseWhenValueScaleGreaterThan2() {
        BigDecimal value = BigDecimal.valueOf(0.123);

        assertFalse(validator.isValid(value, DUMMY_CONTEXT));
    }

    @Test
    void shouldReturnFalseWhenValueScaleLessThanMinus2() {
        BigDecimal value = BigDecimal.valueOf(123, -3);
        System.out.println(value.scale());
        assertFalse(validator.isValid(value, DUMMY_CONTEXT));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0D, 0.0, 0.00, 1D, 1.1, 1.11, 1.11000})
    void shouldReturnTrueWhenScaleIsLessThan3(Double value) {
        assertTrue(validator.isValid(BigDecimal.valueOf(value), DUMMY_CONTEXT));
    }

    @Test
    void shouldReturnTrueWhenScaleIsGreaterThanMinus3() {
        BigDecimal value = BigDecimal.valueOf(123, -2);
        assertTrue(validator.isValid(value, DUMMY_CONTEXT));
    }


}