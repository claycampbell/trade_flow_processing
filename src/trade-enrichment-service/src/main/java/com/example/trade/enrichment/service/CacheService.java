package com.example.trade.enrichment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Service for managing cache operations in the Trade Enrichment Service.
 */
@Service
public class CacheService {
    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheManager cacheManager;

    @Autowired
    public CacheService(RedisTemplate<String, Object> redisTemplate, CacheManager cacheManager) {
        this.redisTemplate = redisTemplate;
        this.cacheManager = cacheManager;
    }

    /**
     * Gets a value from the specified cache.
     * @param cacheName The name of the cache to query
     * @param key The cache key
     * @param type The expected type of the cached value
     * @return Optional containing the cached value, or empty if not found
     */
    public <T> Optional<T> get(String cacheName, String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(formatKey(cacheName, key));
            if (value != null && type.isInstance(value)) {
                return Optional.of(type.cast(value));
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error retrieving value from cache {}: {}", cacheName, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Puts a value in the specified cache.
     * @param cacheName The name of the cache to use
     * @param key The cache key
     * @param value The value to cache
     * @param ttlMillis Time-to-live in milliseconds
     */
    public void put(String cacheName, String key, Object value, long ttlMillis) {
        try {
            String fullKey = formatKey(cacheName, key);
            redisTemplate.opsForValue().set(fullKey, value, ttlMillis, TimeUnit.MILLISECONDS);
            logger.debug("Cached value for key {} in cache {}", key, cacheName);
        } catch (Exception e) {
            logger.error("Error caching value for key {} in cache {}: {}", 
                key, cacheName, e.getMessage());
        }
    }

    /**
     * Removes a value from the specified cache.
     * @param cacheName The name of the cache
     * @param key The cache key to remove
     */
    public void remove(String cacheName, String key) {
        try {
            redisTemplate.delete(formatKey(cacheName, key));
            logger.debug("Removed key {} from cache {}", key, cacheName);
        } catch (Exception e) {
            logger.error("Error removing key {} from cache {}: {}", 
                key, cacheName, e.getMessage());
        }
    }

    /**
     * Clears all entries from the specified cache.
     * @param cacheName The name of the cache to clear
     */
    public void clearCache(String cacheName) {
        try {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                logger.info("Cleared cache {}", cacheName);
            }
        } catch (Exception e) {
            logger.error("Error clearing cache {}: {}", cacheName, e.getMessage());
        }
    }

    /**
     * Checks if a key exists in the specified cache.
     * @param cacheName The name of the cache to check
     * @param key The cache key to look for
     * @return true if the key exists, false otherwise
     */
    public boolean exists(String cacheName, String key) {
        try {
            Boolean hasKey = redisTemplate.hasKey(formatKey(cacheName, key));
            return Boolean.TRUE.equals(hasKey);
        } catch (Exception e) {
            logger.error("Error checking key existence in cache {}: {}", 
                cacheName, e.getMessage());
            return false;
        }
    }

    private String formatKey(String cacheName, String key) {
        return String.format("%s::%s", cacheName, key);
    }
}