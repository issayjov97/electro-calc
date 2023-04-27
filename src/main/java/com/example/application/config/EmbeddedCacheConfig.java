package com.example.application.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class EmbeddedCacheConfig {

    public static final String FIRMS_CACHE = "firms";

    @Bean
    CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(FIRMS_CACHE);
    }

}