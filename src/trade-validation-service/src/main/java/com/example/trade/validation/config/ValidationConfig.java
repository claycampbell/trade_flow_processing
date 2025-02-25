package com.example.trade.validation.config;

import com.example.trade.validation.rule.TradeAmountValidationRule;
import com.example.trade.validation.rule.CurrencyValidationRule;
import com.example.trade.validation.rule.ValidationRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Configuration class for setting up trade validation rules.
 */
@Configuration
public class ValidationConfig {

    @Value("${trade-validation.rules.min-trade-amount}")
    private BigDecimal minTradeAmount;

    @Value("${trade-validation.rules.max-trade-amount}")
    private BigDecimal maxTradeAmount;

    @Value("${trade-validation.rules.allowed-currencies}")
    private List<String> allowedCurrencies;

    @Bean
    public TradeAmountValidationRule tradeAmountValidationRule() {
        return new TradeAmountValidationRule(minTradeAmount, maxTradeAmount);
    }

    @Bean
    public CurrencyValidationRule currencyValidationRule() {
        Set<String> currencies = new HashSet<>(allowedCurrencies);
        return new CurrencyValidationRule(currencies);
    }

    @Bean
    public List<ValidationRule> validationRules(
            TradeAmountValidationRule tradeAmountValidationRule,
            CurrencyValidationRule currencyValidationRule) {
        return Arrays.asList(
            tradeAmountValidationRule,
            currencyValidationRule
        );
    }
}