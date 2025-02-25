package com.example.trade.validation.simulation;

import com.example.trade.validation.model.TradeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Service for simulating trade messages for system testing.
 */
@Service
@EnableScheduling
public class TradeSimulationService {
    private static final Logger logger = LoggerFactory.getLogger(TradeSimulationService.class);
    
    private final KafkaTemplate<String, TradeMessage> kafkaTemplate;
    private final Random random = new Random();
    private final AtomicBoolean enabled = new AtomicBoolean(true);
    
    @Value("${trade-validation.kafka.input-topic}")
    private String tradeTopic;
    
    private static final List<String> INSTRUMENTS = Arrays.asList(
        "AAPL", "GOOGL", "MSFT", "AMZN", "META",
        "TSLA", "NFLX", "NVDA", "JPM", "BAC"
    );
    
    private static final List<String> CURRENCIES = Arrays.asList(
        "USD", "EUR", "GBP", "JPY"
    );
    
    private static final List<String> COUNTERPARTIES = Arrays.asList(
        "JPMC", "GS", "MS", "CS", "UBS",
        "BARC", "HSBC", "BNP", "DB", "CITI"
    );

    @Autowired
    public TradeSimulationService(KafkaTemplate<String, TradeMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Gets the number of trading instruments in the simulation.
     */
    public int getInstrumentCount() {
        return INSTRUMENTS.size();
    }

    /**
     * Gets the number of counterparties in the simulation.
     */
    public int getCounterpartyCount() {
        return COUNTERPARTIES.size();
    }

    /**
     * Gets the current enabled state of the simulation.
     */
    public boolean isEnabled() {
        return enabled.get();
    }

    /**
     * Sets the enabled state of the simulation.
     */
    public void setEnabled(boolean state) {
        enabled.set(state);
    }

    /**
     * Generates and sends simulated trade messages periodically.
     */
    @Scheduled(fixedRateString = "${simulation.trade-generation-interval:5000}")
    public void generateTrade() {
        if (!enabled.get()) {
            return;
        }
        TradeMessage trade = generateRandomTrade();
        sendTrade(trade);
    }

    /**
     * Generates a random trade message with realistic data.
     */
    private TradeMessage generateRandomTrade() {
        TradeMessage trade = new TradeMessage();
        trade.setMessageId(UUID.randomUUID().toString());
        trade.setTradeId(UUID.randomUUID().toString());
        trade.setInstrument(getRandomElement(INSTRUMENTS));
        trade.setCurrency(getRandomElement(CURRENCIES));
        trade.setCounterparty(getRandomElement(COUNTERPARTIES));
        trade.setQuantity(generateRandomQuantity());
        trade.setPrice(generateRandomPrice());
        trade.setTradeDate(LocalDateTime.now());
        trade.setReceivedTimestamp(LocalDateTime.now());
        
        return trade;
    }

    /**
     * Sends a trade message to Kafka.
     */
    private void sendTrade(TradeMessage trade) {
        try {
            kafkaTemplate.send(tradeTopic, trade.getTradeId(), trade).get();
            logger.info("Simulated trade sent: {}", trade.getTradeId());
        } catch (Exception e) {
            logger.error("Error sending simulated trade: {}", e.getMessage());
        }
    }

    /**
     * Generates a random quantity between 100 and 10000.
     */
    private BigDecimal generateRandomQuantity() {
        return BigDecimal.valueOf(random.nextInt(9901) + 100);
    }

    /**
     * Generates a random price between 10 and 1000.
     */
    private BigDecimal generateRandomPrice() {
        return BigDecimal.valueOf(random.nextDouble() * 990 + 10)
            .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Gets a random element from a list.
     */
    private <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Generates and sends a burst of trades for load testing.
     * @param count Number of trades to generate
     */
    public void generateTradeBurst(int count) {
        logger.info("Generating burst of {} trades", count);
        for (int i = 0; i < count; i++) {
            TradeMessage trade = generateRandomTrade();
            sendTrade(trade);
            try {
                Thread.sleep(100); // Small delay to prevent overwhelming the system
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info("Completed generating {} trades", count);
    }
}