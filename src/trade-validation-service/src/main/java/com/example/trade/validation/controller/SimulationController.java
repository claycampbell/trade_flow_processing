package com.example.trade.validation.controller;

import com.example.trade.validation.metrics.SimulationMetrics;
import com.example.trade.validation.simulation.TradeSimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST controller for managing trade simulation operations.
 */
@RestController
@RequestMapping("/api/v1/simulation")
public class SimulationController {
    private static final Logger logger = LoggerFactory.getLogger(SimulationController.class);
    
    private final TradeSimulationService simulationService;
    private final SimulationMetrics metrics;

    @Autowired
    public SimulationController(TradeSimulationService simulationService, SimulationMetrics metrics) {
        this.simulationService = simulationService;
        this.metrics = metrics;
    }

    /**
     * Triggers a burst of simulated trades.
     * @param count Number of trades to generate
     * @return Response indicating success or failure
     */
    @PostMapping("/trades/burst")
    public ResponseEntity<Map<String, String>> generateTradeBurst(
            @RequestParam(defaultValue = "10") int count) {
        try {
            if (count <= 0 || count > 1000) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Count must be between 1 and 1000"));
            }

            logger.info("Received request to generate {} trades", count);
            simulationService.generateTradeBurst(count);
            
            return ResponseEntity.ok()
                .body(Map.of("message", String.format("Generated %d trades", count)));
        } catch (Exception e) {
            logger.error("Error generating trade burst: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to generate trades: " + e.getMessage()));
        }
    }

    /**
     * Gets the current simulation metrics and status.
     * @return Current metrics and configuration status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSimulationStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        
        // Basic status
        status.put("status", simulationService.isEnabled() ? "active" : "inactive");
        status.put("mode", "scheduled");
        
        // Configuration
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("instruments", simulationService.getInstrumentCount());
        config.put("counterparties", simulationService.getCounterpartyCount());
        status.put("configuration", config);

        // Metrics
        Map<String, Object> metricsMap = new LinkedHashMap<>();
        metricsMap.put("tradesGenerated", metrics.getTradesGenerated());
        metricsMap.put("tradesSent", metrics.getTradesSent());
        metricsMap.put("sendFailures", metrics.getTradesSendFailed());
        metricsMap.put("meanGenerationTimeMs", String.format("%.2f", metrics.getMeanGenerationTime()));
        metricsMap.put("meanSendTimeMs", String.format("%.2f", metrics.getMeanSendTime()));
        status.put("metrics", metricsMap);

        return ResponseEntity.ok(status);
    }

    /**
     * Updates simulation configuration.
     * @param enabled Whether to enable/disable simulation
     * @return Updated configuration status
     */
    @PostMapping("/config")
    public ResponseEntity<Map<String, String>> updateConfig(
            @RequestParam boolean enabled) {
        simulationService.setEnabled(enabled);
        String status = enabled ? "enabled" : "disabled";
        logger.info("Simulation {}", status);
        return ResponseEntity.ok(Map.of("status", status));
    }

    /**
     * Gets detailed metrics about the simulation performance.
     */
    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        Map<String, Object> metricsMap = new LinkedHashMap<>();
        
        // Generation metrics
        Map<String, Object> generation = new LinkedHashMap<>();
        generation.put("total", metrics.getTradesGenerated());
        generation.put("meanTimeMs", String.format("%.2f", metrics.getMeanGenerationTime()));
        metricsMap.put("generation", generation);
        
        // Sending metrics
        Map<String, Object> sending = new LinkedHashMap<>();
        sending.put("successful", metrics.getTradesSent());
        sending.put("failed", metrics.getTradesSendFailed());
        sending.put("meanTimeMs", String.format("%.2f", metrics.getMeanSendTime()));
        metricsMap.put("sending", sending);
        
        // Calculate success rate
        double total = metrics.getTradesSent() + metrics.getTradesSendFailed();
        double successRate = total > 0 ? (metrics.getTradesSent() / total) * 100 : 0;
        metricsMap.put("successRate", String.format("%.2f%%", successRate));

        return ResponseEntity.ok(metricsMap);
    }

    /**
     * Endpoint for health checks.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "healthy"));
    }
}