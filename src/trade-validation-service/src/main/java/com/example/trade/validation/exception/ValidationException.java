package com.example.trade.validation.exception;

/**
 * Exception thrown when trade validation fails.
 */
public class ValidationException extends RuntimeException {
    private final String ruleId;

    public ValidationException(String message, String ruleId) {
        super(message);
        this.ruleId = ruleId;
    }

    public ValidationException(String message, String ruleId, Throwable cause) {
        super(message, cause);
        this.ruleId = ruleId;
    }

    public String getRuleId() {
        return ruleId;
    }
}