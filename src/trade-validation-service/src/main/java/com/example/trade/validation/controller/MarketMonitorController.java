package com.example.trade.validation.controller;

import com.example.trade.validation.controller.dto.MarketStateDTO;
import com.example.trade.validation.simulation.TradeSimulationService;
import com.example.trade.validation.simulation.market.MarketCondition;
import com.example.trade.validation.simulation.market.MarketSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller for market monitoring endpoints.
 * Provides real-time market state information and historical data
 * for visualization purposes.
 */
@RestController
@RequestMapping("/api/v1/market")
@CrossOrigin
public class MarketMonitorController {
    private final TradeSimulationService simulationService;
    private final MarketSimulator marketSimulator;
    private static final List<String> SYMBOLS = Arrays.asList("AAPL", "GOOGL", "MSFT", "AMZN", "META");

    @Autowired
    public MarketMonitorController(
            TradeSimulationService simulationService,
            MarketSimulator marketSimulator) {
        this.simulationService = simulationService;
        this.marketSimulator = marketSimulator;
    }

    /**
     * Gets current market state including prices and conditions.
     */
    @GetMapping("/state")
    public ResponseEntity<MarketStateDTO> getMarketState() {
        MarketCondition condition = marketSimulator.getCurrentMarketCondition();
        
        // Get current prices
        Map<String, Double> prices = new HashMap<>();
        Map<String, Double> changes = new HashMap<>();
        
        // Calculate current prices and changes
        for (String symbol : SYMBOLS) {
            double currentPrice = marketSimulator.getCurrentPrice(symbol, 100.0);
            prices.put(symbol, currentPrice);
            // Calculate 24h change (simplified for now)
            changes.put(symbol, (currentPrice - 100.0) / 100.0);
        }

        MarketStateDTO stateDTO = MarketStateDTO.fromMarketCondition(
            condition, prices, changes);

        return ResponseEntity.ok(stateDTO);
    }

    /**
     * Gets detailed information for a specific symbol.
     */
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<Map<String, Object>> getSymbolDetails(
            @PathVariable String symbol) {
        if (!SYMBOLS.contains(symbol)) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> details = new HashMap<>();
        double currentPrice = marketSimulator.getCurrentPrice(symbol, 100.0);
        
        details.put("symbol", symbol);
        details.put("price", currentPrice);
        details.put("change", (currentPrice - 100.0) / 100.0);
        details.put("marketState", marketSimulator.getCurrentMarketCondition().getState());
        details.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(details);
    }

    /**
     * Lists all available trading symbols.
     */
    @GetMapping("/symbols")
    public ResponseEntity<List<String>> getSymbols() {
        return ResponseEntity.ok(SYMBOLS);
    }

    /**
     * Trigger a market event for testing visualization.
     */
    @PostMapping("/event")
    public ResponseEntity<Map<String, Object>> triggerMarketEvent(
            @RequestParam(defaultValue = "Market Event") String description) {
        simulationService.simulateMarketEvent(description);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Market event triggered: " + description);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Gets available market states for UI dropdowns.
     */
    @GetMapping("/states")
    public ResponseEntity<List<String>> getMarketStates() {
        List<String> states = Arrays.stream(MarketCondition.MarketState.values())
            .map(Enum::name)
            .collect(Collectors.toList());
        return ResponseEntity.ok(states);
    }

    /**
     * Forces a specific market state (for testing).
     */
    @PostMapping("/state")
    public ResponseEntity<Map<String, Object>> setMarketState(
            @RequestParam MarketCondition.MarketState state) {
        MarketCondition newCondition = MarketCondition.forState(state);
        marketSimulator.setMarketCondition(newCondition);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Market state set to: " + state);
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}