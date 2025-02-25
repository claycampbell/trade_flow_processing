package com.example.trade.enrichment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

/**
 * Represents a trade enriched with market and reference data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnrichedTrade {
    private String tradeId;
    private String instrument;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal tradeValue;
    private MarketData marketData;
    private ReferenceData referenceData;

    // Getters and Setters
    public String getTradeId() { return tradeId; }
    public void setTradeId(String tradeId) { this.tradeId = tradeId; }

    public String getInstrument() { return instrument; }
    public void setInstrument(String instrument) { this.instrument = instrument; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getTradeValue() { return tradeValue; }
    public void setTradeValue(BigDecimal tradeValue) { this.tradeValue = tradeValue; }

    public MarketData getMarketData() { return marketData; }
    public void setMarketData(MarketData marketData) { this.marketData = marketData; }

    public ReferenceData getReferenceData() { return referenceData; }
    public void setReferenceData(ReferenceData referenceData) { this.referenceData = referenceData; }
}