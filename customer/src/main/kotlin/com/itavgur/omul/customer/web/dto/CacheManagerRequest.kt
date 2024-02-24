package com.itavgur.omul.customer.web.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "cacheManagerRequest", description = "Request for cache manager")
data class CacheManagerRequest(

    @Schema(name = "cacheName", required = true)
    var cacheName: String,
    @Schema(name = "ids", required = false)
    var ids: Set<Int> = emptySet()

)