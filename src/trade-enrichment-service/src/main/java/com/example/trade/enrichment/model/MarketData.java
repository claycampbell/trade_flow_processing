package com.example.trade.enrichment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents market data for a financial instrument.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketData {
    private String instrument;
    private BigDecimal lastPrice;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal volume;
    private LocalDateTime timestamp;
    private String exchange;

    // Getters and Setters
    public String getInstrument() { return instrument; }
    public void setInstrument(String instrument) { this.instrument = instrument; }

    public BigDecimal getLastPrice() { return lastPrice; }
    public void setLastPrice(BigDecimal lastPrice) { this.lastPrice = lastPrice; }

    public BigDecimal getBidPrice() { return bidPrice; }
    public void setBidPrice(BigDecimal bidPrice) { this.bidPrice = bidPrice; }

    public BigDecimal getAskPrice() { return askPrice; }
    public void setAskPrice(BigDecimal askPrice) { this.askPrice = askPrice; }

    public BigDecimal getHigh() { return high; }
    public void setHigh(BigDecimal high) { this.high = high; }

    public BigDecimal getLow() { return low; }
    public void setLow(BigDecimal low) { this.low = low; }

    public BigDecimal getVolume() { return volume; }
    public void setVolume(BigDecimal volume) { this.volume = volume; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }
}