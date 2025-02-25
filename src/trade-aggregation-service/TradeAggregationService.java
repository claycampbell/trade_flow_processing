package com.example.tradeaggregationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class TradeAggregationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeAggregationServiceApplication.class, args);
    }
}

@Service
class TradeAggregationService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TradeAggregationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "enriched-trade-events", groupId = "trade-aggregation")
    public void aggregateTrade(String tradeEvent) {
        // Perform trade aggregation logic here
        String aggregatedTradeEvent = aggregate(tradeEvent);

        kafkaTemplate.send("aggregated-trade-events", aggregatedTradeEvent);
    }

    private String aggregate(String tradeEvent) {
        // Implement aggregation logic
        return tradeEvent + " aggregated";
    }
}