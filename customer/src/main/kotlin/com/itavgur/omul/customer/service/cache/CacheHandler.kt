package com.itavgur.omul.customer.service.cache

interface CacheHandler {

    fun name(): String

    fun initCache()

    fun warmCache(usersId: Set<Int>): Boolean

    fun evictCache(usersId: Set<Int>): Boolean

    fun getState(): CacheState
}