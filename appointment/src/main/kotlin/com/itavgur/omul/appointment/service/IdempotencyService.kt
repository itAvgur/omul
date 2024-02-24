package com.itavgur.omul.appointment.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import java.util.*

@Service
class IdempotencyService(
    @Autowired(required = false) private val cacheManager: CacheManager?
) {

    companion object {
        const val IDEMPOTENCE_KEY_CACHE_NAME = "appointment_idemp_key"
    }

    fun putKey(key: UUID?) {
        if (cacheManager == null) return

        key?.let {
            cacheManager.getCache(IDEMPOTENCE_KEY_CACHE_NAME)?.put(key, key)
        }
    }

    fun isKeyStored(key: UUID?): Boolean {
        if (cacheManager == null) return false

        if (key == null) return false
        cacheManager.getCache(IDEMPOTENCE_KEY_CACHE_NAME)?.get(key) ?: return false
        return true
    }
}