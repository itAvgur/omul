package com.itavgur.omul.customer.auth

import org.slf4j.event.Level
import org.springframework.http.HttpStatus

class ForbiddenException(
    override val message: String?,
    val httpCode: HttpStatus? = null,
    val logLevel: Level = Level.ERROR
) : RuntimeException(message)