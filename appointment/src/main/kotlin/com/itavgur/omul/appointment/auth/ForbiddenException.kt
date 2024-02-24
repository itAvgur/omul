package com.itavgur.omul.appointment.auth

import org.springframework.http.HttpStatus

class ForbiddenException(override val message: String?, val httpCode: HttpStatus? = null) : RuntimeException(message)