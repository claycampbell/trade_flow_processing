package com.example.trade.validation.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Metrics collector for trade simulation performance monitoring.
 */
@Component
public class SimulationMetrics {
    private final Counter tradesGeneratedCounter;
    private final Counter tradesSentCounter;
    private final Counter tradesSendFailedCounter;
    private final Timer tradeGenerationTimer;
    private final Timer tradeSendTimer;

    @Autowired
    public SimulationMetrics(MeterRegistry registry) {
        // Trade generation metrics
        this.tradesGeneratedCounter = Counter.builder("trades.generated")
            .description("Number of trades generated")
            .register(registry);

        this.tradesSentCounter = Counter.builder("trades.sent")
            .description("Number of trades successfully sent to Kafka")
            .register(registry);

        this.tradesSendFailedCounter = Counter.builder("trades.send.failed")
            .description("Number of trades that failed to send to Kafka")
            .register(registry);

        // Timing metrics
        this.tradeGenerationTimer = Timer.builder("trades.generation.time")
            .description("Time taken to generate trades")
            .register(registry);

        this.tradeSendTimer = Timer.builder("trades.send.time")
            .description("Time taken to send trades to Kafka")
            .register(registry);
    }

    /**
     * Records a trade generation event.
     */
    public void recordTradeGenerated() {
        tradesGeneratedCounter.increment();
    }

    /**
     * Records a successful trade send event.
     */
    public void recordTradeSent() {
        tradesSentCounter.increment();
    }

    /**
     * Records a failed trade send event.
     */
    public void recordTradeSendFailed() {
        tradesSendFailedCounter.increment();
    }

    /**
     * Records the time taken to generate a trade.
     * @param timeTaken Time taken in milliseconds
     */
    public void recordGenerationTime(long timeTaken) {
        tradeGenerationTimer.record(timeTaken, TimeUnit.MILLISECONDS);
    }

    /**
     * Records the time taken to send a trade.
     * @param timeTaken Time taken in milliseconds
     */
    public void recordSendTime(long timeTaken) {
        tradeSendTimer.record(timeTaken, TimeUnit.MILLISECONDS);
    }

    /**
     * Gets the total number of trades generated.
     */
    public double getTradesGenerated() {
        return tradesGeneratedCounter.count();
    }

    /**
     * Gets the total number of trades successfully sent.
     */
    public double getTradesSent() {
        return tradesSentCounter.count();
    }

    /**
     * Gets the total number of trades that failed to send.
     */
    public double getTradesSendFailed() {
        return tradesSendFailedCounter.count();
    }

    /**
     * Gets the mean trade generation time in milliseconds.
     */
    public double getMeanGenerationTime() {
        return tradeGenerationTimer.mean(TimeUnit.MILLISECONDS);
    }

    /**
     * Gets the mean trade send time in milliseconds.
     */
    public double getMeanSendTime() {
        return tradeSendTimer.mean(TimeUnit.MILLISECONDS);
    }
}