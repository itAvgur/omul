package com.itavgur.omul.schedule.exception

import org.springframework.http.HttpStatus

class DatabaseConstraintException(override val message: String?, val httpCode: HttpStatus? = null) :
    RuntimeException(message)