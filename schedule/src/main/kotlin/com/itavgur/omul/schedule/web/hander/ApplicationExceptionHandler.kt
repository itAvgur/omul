package com.itavgur.omul.schedule.web.hander

import com.itavgur.omul.customer.web.dto.GeneralErrorResponse
import com.itavgur.omul.schedule.auth.ForbiddenException
import com.itavgur.omul.schedule.exception.ExternalCallException
import com.itavgur.omul.schedule.exception.InvalidRequestException
import com.itavgur.omul.schedule.exception.TimeSlotNotFoundException
import com.itavgur.omul.schedule.util.logger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ApplicationExceptionHandler {

    companion object {
        val LOG by logger()
    }

    @ExceptionHandler(NotImplementedError::class)
    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    fun handleNotImplementedError(
        req: HttpServletRequest,
        exception: NotImplementedError
    ): ResponseEntity<Any> {
        LOG.error("Bad API - NotImplementedError : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.NOT_IMPLEMENTED.value(), exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidRequestException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleInvalidRequestException(
        req: HttpServletRequest,
        exception: InvalidRequestException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - InvalidRequestException : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(
        req: HttpServletRequest,
        exception: MethodArgumentNotValidException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - MethodArgumentNotValidException : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(TimeSlotNotFoundException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleCustomerNotFoundException(
        req: HttpServletRequest,
        exception: TimeSlotNotFoundException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - CustomerNotFoundException : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ExternalCallException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleExternalCallException(
        req: HttpServletRequest,
        exception: ExternalCallException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - ExternalCallException : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ForbiddenException::class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleForbiddenException(
        req: HttpServletRequest,
        exception: ForbiddenException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - ForbiddenException : ${exception.message}")
        val response = GeneralErrorResponse(
            exception.httpCode?.value() ?: HttpStatus.INTERNAL_SERVER_ERROR.value(),
            exception.message, null
        )
        return ResponseEntity(
            response,
            HttpStatusCode.valueOf(exception.httpCode?.value() ?: HttpStatus.INTERNAL_SERVER_ERROR.value())
        )
    }

}