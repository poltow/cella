package com.cella.interview.validators;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static com.cella.interview.validators.CountryCodeValidator.isValid;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class CountryCodeValidatorTest {

    @Test
    @DisplayName("Should validate ARG")
    public void validateARG() {
        assertTrue(isValid("ARG"));
    }

    @Test
    @DisplayName("Should not validate XXX")
    public void validateXXX() {
        assertFalse(isValid("XXX"));
    }

    @Test
    @DisplayName("Should not validate empty")
    public void validateEmpty() {
        assertFalse(isValid(""));
    }

    @Test
    @DisplayName("Should not validate null")
    public void validateNull() {
        assertFalse(isValid(null));
    }
}
