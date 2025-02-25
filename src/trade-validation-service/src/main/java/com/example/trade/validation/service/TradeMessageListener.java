package com.example.trade.validation.service;

import com.example.trade.validation.model.Trade;
import com.example.trade.validation.model.TradeMessage;
import com.example.trade.validation.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Service that listens for trade messages from Kafka and processes them through validation.
 */
@Service
public class TradeMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(TradeMessageListener.class);
    private final TradeValidationService validationService;

    @Autowired
    public TradeMessageListener(TradeValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * Processes incoming trade messages with retry capability.
     * Uses a separate container factory with retry configuration.
     */
    @KafkaListener(
        topics = "${trade-validation.kafka.input-topic}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "retryableKafkaListenerContainerFactory"
    )
    public void processTradeMessage(@Payload TradeMessage message) {
        String messageId = message.getMessageId();
        logger.info("Received trade message: {}", messageId);

        try {
            Trade trade = message.toTrade();
            boolean isValid = validationService.validateTrade(trade);
            
            if (isValid) {
                logger.info("Trade validation successful for message: {}", messageId);
                // TODO: Send validation success message to output topic
            }
        } catch (ValidationException e) {
            logger.error("Validation failed for message {}: {}", messageId, e.getMessage());
            // TODO: Send validation failure message to error topic
            throw e; // Allow retry mechanism to handle the error
        } catch (Exception e) {
            logger.error("Unexpected error processing message {}: {}", messageId, e.getMessage());
            // TODO: Send system error message to error topic
            throw e; // Allow retry mechanism to handle the error
        }
    }

    /**
     * Processes incoming trade messages without retry (for messages that shouldn't be retried).
     */
    @KafkaListener(
        topics = "${trade-validation.kafka.input-topic-no-retry}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void processTradeMessageNoRetry(@Payload TradeMessage message) {
        String messageId = message.getMessageId();
        logger.info("Received trade message (no retry): {}", messageId);

        try {
            Trade trade = message.toTrade();
            boolean isValid = validationService.validateTrade(trade);
            
            if (isValid) {
                logger.info("Trade validation successful for message: {}", messageId);
                // TODO: Send validation success message to output topic
            }
        } catch (Exception e) {
            logger.error("Error processing message {} (no retry): {}", messageId, e.getMessage());
            // TODO: Send error message to error topic
            // Don't rethrow - we don't want to retry these messages
        }
    }
}