package com.itavgur.omul.appointment.web.hander

import com.itavgur.omul.appointment.auth.ForbiddenException
import com.itavgur.omul.appointment.exception.AppointmentNotFoundException
import com.itavgur.omul.appointment.exception.InvalidRequestException
import com.itavgur.omul.appointment.util.logger
import com.itavgur.omul.appointment.web.dto.GeneralErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
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

    @ExceptionHandler(AppointmentNotFoundException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleCustomerNotFoundException(
        req: HttpServletRequest,
        exception: AppointmentNotFoundException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - CustomerNotFoundException : ${exception.message}")
        val response = GeneralErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.message, null)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadableException(
        req: HttpServletRequest,
        exception: HttpMessageNotReadableException
    ): ResponseEntity<Any> {
        LOG.error("Bad API - HttpMessageNotReadableException : ${exception.message}")
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