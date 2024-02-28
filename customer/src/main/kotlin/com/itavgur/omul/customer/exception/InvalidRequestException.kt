package com.itavgur.omul.customer.exception

import org.slf4j.event.Level
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidRequestException(message: String, val logLevel: Level = Level.ERROR) : RuntimeException(message)