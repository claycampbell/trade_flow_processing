package com.example.trade.enrichment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents reference data for a financial instrument.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceData {
    private String instrument;
    private String description;
    private String assetClass;
    private String issuer;
    private String currency;
    private BigDecimal parValue;
    private LocalDate issueDate;
    private LocalDate maturityDate;
    private String countryOfIssue;
    private String isin;
    private String cusip;
    private String sedol;
    private BigDecimal couponRate;
    private String paymentFrequency;

    // Getters and Setters
    public String getInstrument() { return instrument; }
    public void setInstrument(String instrument) { this.instrument = instrument; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAssetClass() { return assetClass; }
    public void setAssetClass(String assetClass) { this.assetClass = assetClass; }

    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getParValue() { return parValue; }
    public void setParValue(BigDecimal parValue) { this.parValue = parValue; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getMaturityDate() { return maturityDate; }
    public void setMaturityDate(LocalDate maturityDate) { this.maturityDate = maturityDate; }

    public String getCountryOfIssue() { return countryOfIssue; }
    public void setCountryOfIssue(String countryOfIssue) { this.countryOfIssue = countryOfIssue; }

    public String getIsin() { return isin; }
    public void setIsin(String isin) { this.isin = isin; }

    public String getCusip() { return cusip; }
    public void setCusip(String cusip) { this.cusip = cusip; }

    public String getSedol() { return sedol; }
    public void setSedol(String sedol) { this.sedol = sedol; }

    public BigDecimal getCouponRate() { return couponRate; }
    public void setCouponRate(BigDecimal couponRate) { this.couponRate = couponRate; }

    public String getPaymentFrequency() { return paymentFrequency; }
    public void setPaymentFrequency(String paymentFrequency) { this.paymentFrequency = paymentFrequency; }
}