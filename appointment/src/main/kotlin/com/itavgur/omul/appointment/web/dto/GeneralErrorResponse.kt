package com.itavgur.omul.appointment.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "ResponseError", description = "Wrapper for the error response")
data class GeneralErrorResponse<T>(

    @Schema(name = "httpCode", required = true)
    val httpCode: Int,
    @Schema(name = "message", required = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val message: String? = null,
    @Schema(name = "response", required = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val response: T? = null
)