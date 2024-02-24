package com.itavgur.omul.auth.exception

import org.springframework.http.HttpStatus

class DatabaseConstraintException(override val message: String?, val httpCode: HttpStatus? = null) :
    RuntimeException(message)