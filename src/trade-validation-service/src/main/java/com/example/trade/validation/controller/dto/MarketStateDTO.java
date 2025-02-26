package com.example.trade.validation.controller.dto;

import com.example.trade.validation.simulation.market.MarketCondition;
import java.util.Map;

/**
 * Data Transfer Object for market state information.
 * Used by visualization endpoints to provide market monitoring data.
 */
public class MarketStateDTO {
    private MarketCondition.MarketState state;
    private double volatility;
    private double trendStrength;
    private double tradingVolume;
    private Map<String, Double> currentPrices;
    private Map<String, Double> priceChanges;
    private long timestamp;

    // Getters and Setters
    public MarketCondition.MarketState getState() {
        return state;
    }

    public void setState(MarketCondition.MarketState state) {
        this.state = state;
    }

    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public double getTrendStrength() {
        return trendStrength;
    }

    public void setTrendStrength(double trendStrength) {
        this.trendStrength = trendStrength;
    }

    public double getTradingVolume() {
        return tradingVolume;
    }

    public void setTradingVolume(double tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    public Map<String, Double> getCurrentPrices() {
        return currentPrices;
    }

    public void setCurrentPrices(Map<String, Double> currentPrices) {
        this.currentPrices = currentPrices;
    }

    public Map<String, Double> getPriceChanges() {
        return priceChanges;
    }

    public void setPriceChanges(Map<String, Double> priceChanges) {
        this.priceChanges = priceChanges;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Creates a MarketStateDTO from a MarketCondition and price data.
     */
    public static MarketStateDTO fromMarketCondition(
            MarketCondition condition,
            Map<String, Double> prices,
            Map<String, Double> changes) {
        MarketStateDTO dto = new MarketStateDTO();
        dto.setState(condition.getState());
        dto.setVolatility(condition.getVolatility());
        dto.setTrendStrength(condition.getTrendStrength());
        dto.setTradingVolume(condition.getTradingVolume());
        dto.setCurrentPrices(prices);
        dto.setPriceChanges(changes);
        dto.setTimestamp(condition.getTimestamp());
        return dto;
    }
}