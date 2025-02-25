package com.example.trade.validation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a trade message received from Kafka.
 * Uses Jackson annotations for JSON serialization/deserialization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeMessage {
    private String messageId;
    private String tradeId;
    private String instrument;
    private BigDecimal quantity;
    private BigDecimal price;
    private String currency;
    private LocalDateTime tradeDate;
    private String counterparty;
    private LocalDateTime receivedTimestamp;

    // Getters and Setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getTradeId() { return tradeId; }
    public void setTradeId(String tradeId) { this.tradeId = tradeId; }

    public String getInstrument() { return instrument; }
    public void setInstrument(String instrument) { this.instrument = instrument; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDateTime getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDateTime tradeDate) { this.tradeDate = tradeDate; }

    public String getCounterparty() { return counterparty; }
    public void setCounterparty(String counterparty) { this.counterparty = counterparty; }

    public LocalDateTime getReceivedTimestamp() { return receivedTimestamp; }
    public void setReceivedTimestamp(LocalDateTime receivedTimestamp) { this.receivedTimestamp = receivedTimestamp; }

    /**
     * Converts this message to a Trade object for validation.
     * @return Trade object populated with data from this message
     */
    public Trade toTrade() {
        Trade trade = new Trade();
        trade.setTradeId(this.tradeId);
        trade.setInstrument(this.instrument);
        trade.setQuantity(this.quantity);
        trade.setPrice(this.price);
        trade.setCurrency(this.currency);
        trade.setTradeDate(this.tradeDate);
        trade.setCounterparty(this.counterparty);
        return trade;
    }
}