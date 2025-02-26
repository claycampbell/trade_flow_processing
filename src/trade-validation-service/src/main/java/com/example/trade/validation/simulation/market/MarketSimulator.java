package com.example.trade.validation.simulation.market;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simulates market conditions and price movements for financial instruments.
 * Maintains current market state and generates realistic price movements
 * based on configurable market conditions.
 */
@Service
public class MarketSimulator {
    private final PriceMovementGenerator priceGenerator;
    private final Random random;
    private final Map<String, Double> currentPrices;
    private final AtomicReference<MarketCondition> currentMarketCondition;
    private final Map<String, Double> volatilityOverrides;

    // Constants for market simulation
    private static final double STATE_CHANGE_PROBABILITY = 0.05; // 5% chance per check
    private static final double MIN_PRICE = 0.01;
    private static final long STATE_CHECK_INTERVAL = 60000; // 1 minute
    private long lastStateCheck;

    public MarketSimulator(PriceMovementGenerator priceGenerator) {
        this.priceGenerator = priceGenerator;
        this.random = new Random();
        this.currentPrices = new ConcurrentHashMap<>();
        this.volatilityOverrides = new HashMap<>();
        this.currentMarketCondition = new AtomicReference<>(new MarketCondition());
        this.lastStateCheck = System.currentTimeMillis();
    }

    /**
     * Updates market condition based on time and random events.
     * Should be called periodically to maintain realistic market behavior.
     */
    public void updateMarketState() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastStateCheck < STATE_CHECK_INTERVAL) {
            return;
        }

        lastStateCheck = currentTime;
        if (random.nextDouble() < STATE_CHANGE_PROBABILITY) {
            MarketCondition.MarketState[] states = MarketCondition.MarketState.values();
            MarketCondition.MarketState newState = states[random.nextInt(states.length)];
            currentMarketCondition.set(MarketCondition.forState(newState));
        }
    }

    /**
     * Gets the next price for a given instrument based on current market conditions.
     *
     * @param symbol The trading instrument symbol
     * @param basePrice The base price to start from if no current price exists
     * @return The next simulated price
     */
    public double getNextPrice(String symbol, double basePrice) {
        updateMarketState();
        
        double currentPrice = currentPrices.getOrDefault(symbol, basePrice);
        MarketCondition condition = getCurrentMarketCondition();

        // Apply symbol-specific volatility if exists
        if (volatilityOverrides.containsKey(symbol)) {
            condition = new MarketCondition(
                condition.getState(),
                volatilityOverrides.get(symbol),
                condition.getTrendStrength(),
                condition.getTradingVolume()
            );
        }

        double movement = priceGenerator.generatePriceMovement(condition);
        double newPrice = Math.max(MIN_PRICE, currentPrice * movement);
        
        currentPrices.put(symbol, newPrice);
        return newPrice;
    }

    /**
     * Sets a symbol-specific volatility override.
     * Useful for simulating different volatility levels for different instruments.
     */
    public void setSymbolVolatility(String symbol, double volatility) {
        volatilityOverrides.put(symbol, Math.min(1.0, Math.max(0.0, volatility)));
    }

    /**
     * Removes a symbol-specific volatility override.
     */
    public void removeSymbolVolatility(String symbol) {
        volatilityOverrides.remove(symbol);
    }

    /**
     * Gets the current market condition.
     */
    public MarketCondition getCurrentMarketCondition() {
        return currentMarketCondition.get();
    }

    /**
     * Forces a specific market condition for simulation purposes.
     */
    public void setMarketCondition(MarketCondition condition) {
        currentMarketCondition.set(condition);
    }

    /**
     * Gets the current price for a symbol.
     */
    public double getCurrentPrice(String symbol, double defaultPrice) {
        return currentPrices.getOrDefault(symbol, defaultPrice);
    }

    /**
     * Simulates a market event by temporarily increasing volatility
     * and potentially changing market state.
     */
    public void simulateMarketEvent(double eventVolatility, 
                                  MarketCondition.MarketState eventState) {
        MarketCondition eventCondition = new MarketCondition(
            eventState,
            Math.min(1.0, eventVolatility),
            random.nextDouble() * 2 - 1, // Random trend between -1 and 1
            1.5 // Increased trading volume
        );
        currentMarketCondition.set(eventCondition);
    }
}