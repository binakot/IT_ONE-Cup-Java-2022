package com.example.demo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationUtilTest {

    @Test
    void validateTableName() {
        assertTrue(ValidationUtil.validateReportTableName("Customer"));
        assertTrue(ValidationUtil.validateReportTableName("customer"));
        assertTrue(ValidationUtil.validateReportTableName("CUSTOMER"));
        assertTrue(ValidationUtil.validateReportTableName("CuStOmeR"));
        assertTrue(ValidationUtil.validateReportTableName("Customer123"));

        assertFalse(ValidationUtil.validateReportTableName(" Customer"));
        assertFalse(ValidationUtil.validateReportTableName("Customer "));
        assertFalse(ValidationUtil.validateReportTableName(" Customer "));
        assertFalse(ValidationUtil.validateReportTableName("Cust omer"));
        assertFalse(ValidationUtil.validateReportTableName("Customer 123"));
        assertFalse(ValidationUtil.validateReportTableName("CustomerЛАЛА"));
    }
}
