package com.itavgur.omul.schedule.auth

import org.springframework.http.HttpStatus

class ForbiddenException(override val message: String?, val httpCode: HttpStatus? = null) : RuntimeException(message)