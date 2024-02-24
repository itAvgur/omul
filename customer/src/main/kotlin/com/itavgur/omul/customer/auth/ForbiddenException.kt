package com.itavgur.omul.customer.auth

import org.springframework.http.HttpStatus

class ForbiddenException(override val message: String?, val httpCode: HttpStatus? = null) : RuntimeException(message)