package com.example.trade.validation.simulation.market;

/**
 * Represents the current market condition and its parameters.
 * This model helps generate realistic trade data by simulating
 * different market states and their effects on prices.
 */
public class MarketCondition {
    private MarketState state;
    private double volatility;        // Range: 0.0 - 1.0
    private double trendStrength;     // Range: -1.0 (strong downtrend) to 1.0 (strong uptrend)
    private double tradingVolume;     // Relative volume, 1.0 is normal
    private long timestamp;

    public enum MarketState {
        NORMAL,          // Regular trading conditions
        VOLATILE,        // High volatility period
        TRENDING_UP,     // Clear upward trend
        TRENDING_DOWN,   // Clear downward trend
        LOW_LIQUIDITY,   // Less trading activity
        HIGH_LIQUIDITY,  // High trading activity
        MARKET_EVENT    // Special market conditions (news, earnings, etc.)
    }

    public MarketCondition() {
        this.state = MarketState.NORMAL;
        this.volatility = 0.2;        // Default moderate volatility
        this.trendStrength = 0.0;     // No trend by default
        this.tradingVolume = 1.0;     // Normal trading volume
        this.timestamp = System.currentTimeMillis();
    }

    public MarketCondition(MarketState state, double volatility, 
                         double trendStrength, double tradingVolume) {
        this.state = state;
        this.volatility = Math.min(1.0, Math.max(0.0, volatility));
        this.trendStrength = Math.min(1.0, Math.max(-1.0, trendStrength));
        this.tradingVolume = Math.max(0.0, tradingVolume);
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public MarketState getState() {
        return state;
    }

    public double getVolatility() {
        return volatility;
    }

    public double getTrendStrength() {
        return trendStrength;
    }

    public double getTradingVolume() {
        return tradingVolume;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setState(MarketState state) {
        this.state = state;
    }

    public void setVolatility(double volatility) {
        this.volatility = Math.min(1.0, Math.max(0.0, volatility));
    }

    public void setTrendStrength(double trendStrength) {
        this.trendStrength = Math.min(1.0, Math.max(-1.0, trendStrength));
    }

    public void setTradingVolume(double tradingVolume) {
        this.tradingVolume = Math.max(0.0, tradingVolume);
    }

    /**
     * Creates a new market condition for a specific state.
     */
    public static MarketCondition forState(MarketState state) {
        switch (state) {
            case VOLATILE:
                return new MarketCondition(state, 0.8, 0.0, 1.5);
            case TRENDING_UP:
                return new MarketCondition(state, 0.3, 0.7, 1.2);
            case TRENDING_DOWN:
                return new MarketCondition(state, 0.3, -0.7, 1.2);
            case LOW_LIQUIDITY:
                return new MarketCondition(state, 0.4, 0.0, 0.5);
            case HIGH_LIQUIDITY:
                return new MarketCondition(state, 0.2, 0.0, 2.0);
            case MARKET_EVENT:
                return new MarketCondition(state, 0.6, 0.0, 1.8);
            default:
                return new MarketCondition();
        }
    }

    @Override
    public String toString() {
        return String.format(
            "MarketCondition{state=%s, volatility=%.2f, trendStrength=%.2f, " +
            "tradingVolume=%.2f, timestamp=%d}",
            state, volatility, trendStrength, tradingVolume, timestamp
        );
    }
}