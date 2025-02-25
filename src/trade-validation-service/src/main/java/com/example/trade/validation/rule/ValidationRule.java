package com.example.trade.validation.rule;

import com.example.trade.validation.model.Trade;

/**
 * Interface for trade validation rules.
 * Each validation rule represents a specific check that must be performed on a trade.
 */
public interface ValidationRule {
    /**
     * Validates a trade against this rule.
     * @param trade The trade to validate
     * @return true if the trade passes validation, false otherwise
     * @throws ValidationException if there's an error during validation
     */
    boolean validate(Trade trade);

    /**
     * Gets an error message when validation fails.
     * @return The error message associated with this rule
     */
    String getErrorMessage();
}