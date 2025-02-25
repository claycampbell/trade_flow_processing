package com.example.trade.validation.rule;

import com.example.trade.validation.model.Trade;
import com.example.trade.validation.exception.ValidationException;
import java.util.Set;
import java.util.HashSet;

/**
 * Validates that the trade currency is in the list of allowed currencies.
 */
public class CurrencyValidationRule implements ValidationRule {
    private final Set<String> allowedCurrencies;
    private static final String RULE_ID = "CURRENCY";

    public CurrencyValidationRule(Set<String> allowedCurrencies) {
        this.allowedCurrencies = new HashSet<>(allowedCurrencies);
    }

    @Override
    public boolean validate(Trade trade) {
        if (trade.getCurrency() == null || trade.getCurrency().trim().isEmpty()) {
            throw new ValidationException("Trade currency must not be null or empty", RULE_ID);
        }

        String currency = trade.getCurrency().toUpperCase();
        if (!allowedCurrencies.contains(currency)) {
            throw new ValidationException(
                String.format("Currency %s is not in the list of allowed currencies: %s",
                    currency, allowedCurrencies),
                RULE_ID
            );
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return String.format("Currency must be one of: %s", allowedCurrencies);
    }

    /**
     * Creates a default instance with common currencies.
     * @return A CurrencyValidationRule with USD, EUR, GBP, and JPY as allowed currencies
     */
    public static CurrencyValidationRule createDefault() {
        Set<String> defaultCurrencies = new HashSet<>();
        defaultCurrencies.add("USD");
        defaultCurrencies.add("EUR");
        defaultCurrencies.add("GBP");
        defaultCurrencies.add("JPY");
        return new CurrencyValidationRule(defaultCurrencies);
    }
}