package com.example.tradevalidationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class TradeValidationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeValidationServiceApplication.class, args);
    }
}

@Service
class TradeValidationService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TradeValidationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "trade-events", groupId = "trade-validation")
    public void validateTrade(String tradeEvent) {
        // Perform trade validation logic here
        boolean isValid = validate(tradeEvent);

        if (isValid) {
            kafkaTemplate.send("validated-trade-events", tradeEvent);
        }
    }

    private boolean validate(String tradeEvent) {
        // Implement validation logic
        return true;
    }
}