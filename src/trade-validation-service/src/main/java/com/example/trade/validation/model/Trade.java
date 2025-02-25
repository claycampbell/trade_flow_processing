package com.example.trade.validation.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Trade {
    private String tradeId;
    private String instrument;
    private BigDecimal quantity;
    private BigDecimal price;
    private String currency;
    private LocalDateTime tradeDate;
    private String counterparty;
    private String status;

    // Getters and Setters
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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}