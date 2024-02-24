package com.itavgur.omul.personnel.exception

import org.springframework.http.HttpStatus

class DatabaseConstraintException(override val message: String?, val httpCode: HttpStatus? = null) :
    RuntimeException(message)