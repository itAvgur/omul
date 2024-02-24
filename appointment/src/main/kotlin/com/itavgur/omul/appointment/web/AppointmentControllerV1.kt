package com.itavgur.omul.appointment.web

import com.itavgur.omul.appointment.exception.InvalidRequestException
import com.itavgur.omul.appointment.service.AppointmentService
import com.itavgur.omul.appointment.service.IdempotencyService
import com.itavgur.omul.appointment.web.dto.AppointmentCustomerRequest
import com.itavgur.omul.appointment.web.dto.AppointmentCustomerResponse
import com.itavgur.omul.appointment.web.dto.AppointmentNewCustomerRequest
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/appointment/")
class AppointmentControllerV1(
    @Autowired private val appointmentService: AppointmentService,
    @Autowired private val idempotencyService: IdempotencyService
) {

    @Operation(summary = "Create new appointment for customer", tags = ["customerAppointment"])
    @PostMapping("/customer")
    fun createCustomerAppointmentV1(
        @Valid @RequestBody request: AppointmentCustomerRequest,
    ): AppointmentCustomerResponse {

        checkIdempotentKey(request.idempotentKey)

        request.appointmentId?.let {
            throw InvalidRequestException("appointmentId must be empty")
        }
        return appointmentService.createAppointmentRegisteredCustomer(request)
    }

    @Operation(summary = "Get customer's appointment info", tags = ["customerAppointment"])
    @GetMapping("/customer")
    fun getCustomerAppointmentV1(
        @RequestParam(name = "appointmentId", required = false) appointmentId: Int?,
        @RequestParam(name = "idempotentKey", required = false) idempotentKey: UUID?,
    ): AppointmentCustomerResponse = appointmentService.getCustomerAppointment(appointmentId, idempotentKey)

    @Operation(summary = "Update existed customer's appointment", tags = ["customerAppointment"])
    @PatchMapping("/customer")
    fun updateCustomerAppointmentV1(
        @Valid @RequestBody request: AppointmentCustomerRequest,
    ): AppointmentCustomerResponse {
        return appointmentService.updateCustomerAppointment(request)
    }

    @Operation(summary = "Delete customer's appointment", tags = ["customerAppointment"])
    @DeleteMapping("/customer")
    @ResponseStatus(HttpStatus.OK)
    fun deleteCustomerAppointmentV1(
        @RequestParam(name = "appointmentId", required = true) appointmentId: Int,
    ) = appointmentService.deleteCustomerAppointment(appointmentId)

    @Operation(summary = "Create new appointment for new customer", tags = ["customerAppointment"])
    @PostMapping("/newCustomer")
    fun createNewCustomerAppointmentV1(
        @Valid @RequestBody request: AppointmentNewCustomerRequest,
    ): AppointmentCustomerResponse {

        checkIdempotentKey(request.idempotentKey)

        request.email?.let {
            return appointmentService.createAppointmentAnonymousCustomer(request)
        }
        throw InvalidRequestException("email is absent")
    }

    fun checkIdempotentKey(idempotentKey: UUID?) {

        if (idempotencyService.isKeyStored(idempotentKey)) {
            throw InvalidRequestException("idempotent key already stored")
        } else {
            idempotencyService.putKey(idempotentKey)
        }
    }

}