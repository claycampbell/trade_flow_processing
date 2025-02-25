package com.example.tradeenrichmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class TradeEnrichmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeEnrichmentServiceApplication.class, args);
    }
}

@Service
class TradeEnrichmentService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TradeEnrichmentService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "validated-trade-events", groupId = "trade-enrichment")
    public void enrichTrade(String tradeEvent) {
        // Perform trade enrichment logic here
        String enrichedTradeEvent = enrich(tradeEvent);

        kafkaTemplate.send("enriched-trade-events", enrichedTradeEvent);
    }

    private String enrich(String tradeEvent) {
        // Implement enrichment logic
        return tradeEvent + " enriched";
    }
}