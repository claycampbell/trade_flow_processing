package com.example.trade.validation.service;

import com.example.trade.validation.model.Trade;
import com.example.trade.validation.rule.ValidationRule;
import com.example.trade.validation.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for validating trades using configured validation rules.
 */
@Service
public class TradeValidationService {
    private static final Logger logger = LoggerFactory.getLogger(TradeValidationService.class);
    private final List<ValidationRule> validationRules;

    public TradeValidationService(List<ValidationRule> validationRules) {
        this.validationRules = new ArrayList<>(validationRules);
    }

    /**
     * Validates a trade against all configured validation rules.
     * @param trade The trade to validate
     * @return true if the trade passes all validation rules
     * @throws ValidationException if any validation rule fails
     */
    public boolean validateTrade(Trade trade) {
        logger.debug("Validating trade: {}", trade.getTradeId());
        
        List<String> errors = validationRules.stream()
            .filter(rule -> {
                try {
                    return !rule.validate(trade);
                } catch (ValidationException e) {
                    logger.error("Validation failed for trade {}: {}", trade.getTradeId(), e.getMessage());
                    throw e;
                } catch (Exception e) {
                    logger.error("Unexpected error validating trade {}: {}", trade.getTradeId(), e.getMessage());
                    throw new ValidationException("Unexpected validation error", "SYSTEM_ERROR", e);
                }
            })
            .map(ValidationRule::getErrorMessage)
            .collect(Collectors.toList());

        if (!errors.isEmpty()) {
            String errorMessage = String.join("; ", errors);
            logger.error("Trade {} failed validation: {}", trade.getTradeId(), errorMessage);
            throw new ValidationException(errorMessage, "VALIDATION_FAILED");
        }

        logger.info("Trade {} passed all validations", trade.getTradeId());
        return true;
    }

    /**
     * Adds a new validation rule to the service.
     * @param rule The validation rule to add
     */
    public void addValidationRule(ValidationRule rule) {
        validationRules.add(rule);
        logger.info("Added new validation rule: {}", rule.getClass().getSimpleName());
    }

    /**
     * Gets the current list of validation rules.
     * @return List of configured validation rules
     */
    public List<ValidationRule> getValidationRules() {
        return new ArrayList<>(validationRules);
    }
}