package com.example.trade.validation.simulation.market;

import org.springframework.stereotype.Component;
import java.util.Random;

/**
 * Generates realistic price movements based on market conditions.
 * Uses a combination of random walks, trends, and volatility to
 * simulate market price behavior.
 */
@Component
public class PriceMovementGenerator {
    private final Random random;
    private static final double BASE_MOVEMENT = 0.001; // 0.1% base movement

    public PriceMovementGenerator() {
        this.random = new Random();
    }

    /**
     * Generates a price movement factor based on current market conditions.
     * The factor should be multiplied with the current price to get the new price.
     *
     * @param condition Current market condition
     * @return Price movement factor (e.g., 1.01 for 1% increase)
     */
    public double generatePriceMovement(MarketCondition condition) {
        // Base random movement (random walk)
        double baseChange = generateRandomWalk(condition.getVolatility());
        
        // Add trend component
        double trendComponent = generateTrendComponent(condition.getTrendStrength());
        
        // Add volatility spikes based on market state
        double volatilityComponent = generateVolatilitySpike(condition);
        
        // Combine all components
        double totalMovement = baseChange + trendComponent + volatilityComponent;
        
        // Convert to price factor (e.g., 0.02 becomes 1.02 for 2% increase)
        return 1.0 + totalMovement;
    }

    /**
     * Generates a random walk component for price movement.
     * Uses normal distribution for more realistic movements.
     */
    private double generateRandomWalk(double volatility) {
        double randomFactor = random.nextGaussian();
        return BASE_MOVEMENT * volatility * randomFactor;
    }

    /**
     * Generates trend-based movement component.
     * @param trendStrength -1.0 to 1.0 (strong down to strong up)
     */
    private double generateTrendComponent(double trendStrength) {
        double randomVariation = (random.nextDouble() - 0.5) * 0.5;
        return BASE_MOVEMENT * trendStrength * (1.0 + randomVariation);
    }

    /**
     * Generates additional volatility based on market state.
     */
    private double generateVolatilitySpike(MarketCondition condition) {
        double spikeChance = 0.0;
        double maxSpike = 0.0;

        switch (condition.getState()) {
            case VOLATILE:
                spikeChance = 0.3;
                maxSpike = 0.03; // 3% max spike
                break;
            case MARKET_EVENT:
                spikeChance = 0.4;
                maxSpike = 0.05; // 5% max spike
                break;
            case LOW_LIQUIDITY:
                spikeChance = 0.2;
                maxSpike = 0.02; // 2% max spike
                break;
            default:
                spikeChance = 0.1;
                maxSpike = 0.01; // 1% max spike
                break;
        }

        // Generate spike based on chance
        if (random.nextDouble() < spikeChance) {
            double spikeSize = maxSpike * random.nextDouble();
            return random.nextBoolean() ? spikeSize : -spikeSize;
        }

        return 0.0;
    }

    /**
     * Adjusts price movement based on trading volume.
     * Higher volume typically means more stable prices.
     *
     * @param movement Base price movement
     * @param tradingVolume Relative trading volume (1.0 is normal)
     * @return Adjusted price movement
     */
    public double adjustForVolume(double movement, double tradingVolume) {
        if (tradingVolume <= 0) {
            return movement;
        }
        // Lower volume increases volatility, higher volume stabilizes
        double volumeAdjustment = Math.sqrt(1.0 / tradingVolume);
        return movement * volumeAdjustment;
    }

    /**
     * Simulates a series of price movements over time.
     *
     * @param startPrice Initial price
     * @param steps Number of time steps to simulate
     * @param condition Market condition to use
     * @return Array of simulated prices
     */
    public double[] simulatePriceSeries(double startPrice, int steps, 
                                      MarketCondition condition) {
        double[] prices = new double[steps];
        prices[0] = startPrice;

        for (int i = 1; i < steps; i++) {
            double movement = generatePriceMovement(condition);
            movement = adjustForVolume(movement, condition.getTradingVolume());
            prices[i] = prices[i - 1] * movement;
        }

        return prices;
    }
}