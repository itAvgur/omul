package com.itavgur.omul.auth.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@ConditionalOnProperty("cache.global.enabled", havingValue = "true", matchIfMissing = false)
@EnableScheduling
@EnableCaching
class CacheConfig {

    @Bean("cacheManager")
    @ConditionalOnProperty("cache.global.provider", havingValue = "inner", matchIfMissing = true)
    fun innerCacheManager(): CacheManager {
        return ConcurrentMapCacheManager()
    }

    @Bean
    @ConditionalOnProperty("cache.global.provider", havingValue = "redis", matchIfMissing = true)
    fun cacheConfiguration(): RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
    }

}