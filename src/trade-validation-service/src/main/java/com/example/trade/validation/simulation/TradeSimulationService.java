package com.example.trade.validation.simulation;

import com.example.trade.validation.metrics.SimulationMetrics;
import com.example.trade.validation.model.Trade;
import com.example.trade.validation.simulation.market.MarketSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simulates trade generation with realistic market conditions.
 * Uses market simulation to generate realistic price movements and
 * trading patterns based on current market state.
 */
@Service
public class TradeSimulationService {
    private final KafkaTemplate<String, Trade> kafkaTemplate;
    private final SimulationMetrics metrics;
    private final MarketSimulator marketSimulator;
    private final Random random;
    private final AtomicBoolean isRunning;

    private static final String[] SYMBOLS = {"AAPL", "GOOGL", "MSFT", "AMZN", "META"};
    private static final String[] CURRENCIES = {"USD", "EUR", "GBP", "JPY"};
    private static final double BASE_TRADE_AMOUNT = 10000.0;
    private static final String KAFKA_TOPIC = "trades";

    @Autowired
    public TradeSimulationService(
            KafkaTemplate<String, Trade> kafkaTemplate,
            SimulationMetrics metrics,
            MarketSimulator marketSimulator) {
        this.kafkaTemplate = kafkaTemplate;
        this.metrics = metrics;
        this.marketSimulator = marketSimulator;
        this.random = new Random();
        this.isRunning = new AtomicBoolean(false);

        // Initialize market conditions for each symbol
        for (String symbol : SYMBOLS) {
            // Set initial price and volatility for each symbol
            double basePrice = 100.0 + random.nextDouble() * 900.0; // $100-$1000
            marketSimulator.getNextPrice(symbol, basePrice);
            marketSimulator.setSymbolVolatility(symbol, 0.2 + random.nextDouble() * 0.3); // 20-50% volatility
        }
    }

    /**
     * Starts the trade simulation.
     */
    public void startSimulation() {
        isRunning.set(true);
        metrics.recordSimulationStart();
    }

    /**
     * Stops the trade simulation.
     */
    public void stopSimulation() {
        isRunning.set(false);
        metrics.recordSimulationStop();
    }

    /**
     * Generates and sends trades at regular intervals.
     */
    @Scheduled(fixedRate = 1000) // Generate trade every second
    public void generateTrade() {
        if (!isRunning.get()) {
            return;
        }

        try {
            Trade trade = generateRandomTrade();
            kafkaTemplate.send(KAFKA_TOPIC, trade.getSymbol(), trade);
            metrics.recordTradeGenerated();
        } catch (Exception e) {
            metrics.recordError();
        }
    }

    /**
     * Generates a random trade with realistic market-driven prices.
     */
    private Trade generateRandomTrade() {
        String symbol = SYMBOLS[random.nextInt(SYMBOLS.length)];
        String currency = CURRENCIES[random.nextInt(CURRENCIES.length)];
        
        // Get market-driven price
        double marketPrice = marketSimulator.getNextPrice(symbol, 100.0);
        
        // Generate trade amount based on market conditions
        double baseAmount = BASE_TRADE_AMOUNT * (0.5 + random.nextDouble());
        double volumeAdjustment = marketSimulator.getCurrentMarketCondition().getTradingVolume();
        double tradeAmount = baseAmount * volumeAdjustment;

        return Trade.builder()
                .id(UUID.randomUUID().toString())
                .symbol(symbol)
                .price(marketPrice)
                .amount(tradeAmount)
                .currency(currency)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * Simulates a market event (e.g., earnings announcement, economic news)
     */
    public void simulateMarketEvent(String description) {
        double eventVolatility = 0.6 + random.nextDouble() * 0.4; // 60-100% volatility
        marketSimulator.simulateMarketEvent(
            eventVolatility,
            random.nextBoolean() 
                ? MarketCondition.MarketState.VOLATILE 
                : MarketCondition.MarketState.MARKET_EVENT
        );
        metrics.recordMarketEvent(description);
    }

    /**
     * Gets the current market condition.
     */
    public MarketCondition getCurrentMarketCondition() {
        return marketSimulator.getCurrentMarketCondition();
    }

    /**
     * Gets the current price for a symbol.
     */
    public double getCurrentPrice(String symbol) {
        return marketSimulator.getCurrentPrice(symbol, 100.0);
    }
}