package com.example.trade.validation.rule;

import com.example.trade.validation.model.Trade;
import com.example.trade.validation.exception.ValidationException;
import java.math.BigDecimal;

/**
 * Validates that the trade amount falls within acceptable limits.
 */
public class TradeAmountValidationRule implements ValidationRule {
    private final BigDecimal minAmount;
    private final BigDecimal maxAmount;
    private static final String RULE_ID = "TRADE_AMOUNT";

    public TradeAmountValidationRule(BigDecimal minAmount, BigDecimal maxAmount) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    public boolean validate(Trade trade) {
        if (trade.getQuantity() == null || trade.getPrice() == null) {
            throw new ValidationException("Trade quantity and price must not be null", RULE_ID);
        }

        BigDecimal tradeAmount = trade.getQuantity().multiply(trade.getPrice());

        if (tradeAmount.compareTo(minAmount) < 0) {
            throw new ValidationException(
                String.format("Trade amount %s is below minimum allowed amount %s", 
                    tradeAmount, minAmount),
                RULE_ID
            );
        }

        if (tradeAmount.compareTo(maxAmount) > 0) {
            throw new ValidationException(
                String.format("Trade amount %s exceeds maximum allowed amount %s", 
                    tradeAmount, maxAmount),
                RULE_ID
            );
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return String.format("Trade amount must be between %s and %s", minAmount, maxAmount);
    }
}