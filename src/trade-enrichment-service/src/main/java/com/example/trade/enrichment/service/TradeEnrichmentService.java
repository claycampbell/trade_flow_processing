package com.example.trade.enrichment.service;

import com.example.trade.enrichment.model.EnrichedTrade;
import com.example.trade.enrichment.model.MarketData;
import com.example.trade.enrichment.model.ReferenceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service responsible for enriching trades with market and reference data.
 */
@Service
public class TradeEnrichmentService {
    private static final Logger logger = LoggerFactory.getLogger(TradeEnrichmentService.class);
    
    private final CacheService cacheService;
    
    @Value("${trade-enrichment.cache.market-data-ttl}")
    private long marketDataTtl;
    
    @Value("${trade-enrichment.cache.reference-data-ttl}")
    private long referenceDataTtl;

    @Autowired
    public TradeEnrichmentService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * Enriches a trade with market and reference data.
     * @param tradeId The trade ID to enrich
     * @param instrument The instrument identifier
     * @param quantity The trade quantity
     * @param price The trade price
     * @return EnrichedTrade containing the enriched trade data
     */
    public Optional<EnrichedTrade> enrichTrade(String tradeId, String instrument, 
            BigDecimal quantity, BigDecimal price) {
        try {
            logger.debug("Enriching trade: {}", tradeId);

            // Get market data from cache or external service
            Optional<MarketData> marketData = getMarketData(instrument);
            if (marketData.isEmpty()) {
                logger.error("Failed to retrieve market data for instrument: {}", instrument);
                return Optional.empty();
            }

            // Get reference data from cache or external service
            Optional<ReferenceData> referenceData = getReferenceData(instrument);
            if (referenceData.isEmpty()) {
                logger.error("Failed to retrieve reference data for instrument: {}", instrument);
                return Optional.empty();
            }

            EnrichedTrade enrichedTrade = new EnrichedTrade();
            enrichedTrade.setTradeId(tradeId);
            enrichedTrade.setInstrument(instrument);
            enrichedTrade.setQuantity(quantity);
            enrichedTrade.setPrice(price);
            enrichedTrade.setMarketData(marketData.get());
            enrichedTrade.setReferenceData(referenceData.get());
            
            // Calculate trade value using market data
            BigDecimal tradeValue = calculateTradeValue(quantity, price, marketData.get());
            enrichedTrade.setTradeValue(tradeValue);

            logger.info("Successfully enriched trade: {}", tradeId);
            return Optional.of(enrichedTrade);
        } catch (Exception e) {
            logger.error("Error enriching trade {}: {}", tradeId, e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<MarketData> getMarketData(String instrument) {
        // Try to get from cache first
        Optional<MarketData> cachedData = cacheService.get(
            "marketData", 
            instrument, 
            MarketData.class
        );

        if (cachedData.isPresent()) {
            return cachedData;
        }

        // If not in cache, fetch from external service (simulated here)
        try {
            MarketData marketData = fetchMarketDataFromExternalService(instrument);
            cacheService.put("marketData", instrument, marketData, marketDataTtl);
            return Optional.of(marketData);
        } catch (Exception e) {
            logger.error("Error fetching market data for {}: {}", instrument, e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<ReferenceData> getReferenceData(String instrument) {
        // Try to get from cache first
        Optional<ReferenceData> cachedData = cacheService.get(
            "referenceData", 
            instrument, 
            ReferenceData.class
        );

        if (cachedData.isPresent()) {
            return cachedData;
        }

        // If not in cache, fetch from external service (simulated here)
        try {
            ReferenceData referenceData = fetchReferenceDataFromExternalService(instrument);
            cacheService.put("referenceData", instrument, referenceData, referenceDataTtl);
            return Optional.of(referenceData);
        } catch (Exception e) {
            logger.error("Error fetching reference data for {}: {}", instrument, e.getMessage());
            return Optional.empty();
        }
    }

    // Simulated external service calls (to be implemented with actual service integration)
    private MarketData fetchMarketDataFromExternalService(String instrument) {
        // TODO: Implement actual market data service integration
        MarketData data = new MarketData();
        data.setInstrument(instrument);
        // Add mock data
        return data;
    }

    private ReferenceData fetchReferenceDataFromExternalService(String instrument) {
        // TODO: Implement actual reference data service integration
        ReferenceData data = new ReferenceData();
        data.setInstrument(instrument);
        // Add mock data
        return data;
    }

    private BigDecimal calculateTradeValue(BigDecimal quantity, BigDecimal price, 
            MarketData marketData) {
        // TODO: Implement actual trade value calculation using market data
        return quantity.multiply(price);
    }
}