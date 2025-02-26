package com.example.trade.validation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for market monitoring.
 * Includes settings for update intervals, thresholds,
 * and visualization parameters.
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "market.monitor")
public class MarketMonitorConfig {
    // Update intervals
    private long stateUpdateInterval = 1000; // 1 second
    private long priceUpdateInterval = 500;  // 500ms
    private long eventCheckInterval = 5000;  // 5 seconds
    
    // Price movement thresholds
    private double significantPriceChange = 0.01;   // 1%
    private double volatilityThreshold = 0.02;      // 2%
    private double extremePriceChange = 0.05;       // 5%
    
    // Market event configuration
    private int eventDuration = 300;               // 5 minutes in seconds
    private double eventVolatilityMultiplier = 2.0; // Double volatility during events
    
    // Historical data
    private int priceHistorySize = 100;            // Keep last 100 price points
    private long historyRetentionPeriod = 3600;    // 1 hour in seconds

    // Getters and Setters
    public long getStateUpdateInterval() {
        return stateUpdateInterval;
    }

    public void setStateUpdateInterval(long stateUpdateInterval) {
        this.stateUpdateInterval = stateUpdateInterval;
    }

    public long getPriceUpdateInterval() {
        return priceUpdateInterval;
    }

    public void setPriceUpdateInterval(long priceUpdateInterval) {
        this.priceUpdateInterval = priceUpdateInterval;
    }

    public long getEventCheckInterval() {
        return eventCheckInterval;
    }

    public void setEventCheckInterval(long eventCheckInterval) {
        this.eventCheckInterval = eventCheckInterval;
    }

    public double getSignificantPriceChange() {
        return significantPriceChange;
    }

    public void setSignificantPriceChange(double significantPriceChange) {
        this.significantPriceChange = significantPriceChange;
    }

    public double getVolatilityThreshold() {
        return volatilityThreshold;
    }

    public void setVolatilityThreshold(double volatilityThreshold) {
        this.volatilityThreshold = volatilityThreshold;
    }

    public double getExtremePriceChange() {
        return extremePriceChange;
    }

    public void setExtremePriceChange(double extremePriceChange) {
        this.extremePriceChange = extremePriceChange;
    }

    public int getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(int eventDuration) {
        this.eventDuration = eventDuration;
    }

    public double getEventVolatilityMultiplier() {
        return eventVolatilityMultiplier;
    }

    public void setEventVolatilityMultiplier(double eventVolatilityMultiplier) {
        this.eventVolatilityMultiplier = eventVolatilityMultiplier;
    }

    public int getPriceHistorySize() {
        return priceHistorySize;
    }

    public void setPriceHistorySize(int priceHistorySize) {
        this.priceHistorySize = priceHistorySize;
    }

    public long getHistoryRetentionPeriod() {
        return historyRetentionPeriod;
    }

    public void setHistoryRetentionPeriod(long historyRetentionPeriod) {
        this.historyRetentionPeriod = historyRetentionPeriod;
    }
}