package com.itavgur.omul.appointment.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class ExternalDaoException(message: String) : RuntimeException(message)