package com.itavgur.omul.customer.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "ResponseError", description = "Wrapper for the error response")
data class GeneralErrorResponse<T>(

    @Schema(name = "httpCode", required = true)
    val httpCode: Int,
    @Schema(name = "message", required = true)
    @JsonInclude(NON_NULL)
    val message: String? = null,
    @Schema(name = "response", required = true)
    @JsonInclude(NON_NULL)
    val response: T? = null,
    @JsonInclude(NON_NULL)
    var stackTrace: Array<out StackTraceElement>? = null
)