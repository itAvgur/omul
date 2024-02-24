package com.itavgur.omul.appointment.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.itavgur.omul.appointment.domain.Appointment
import com.itavgur.omul.appointment.domain.AppointmentStatus
import java.io.Serializable
import java.util.*

data class AppointmentCustomerResponse(

    @JsonInclude(NON_NULL)
    val idempotentKey: UUID? = null,
    @JsonInclude(NON_NULL)
    val appointmentId: Int? = null,
    @JsonInclude(NON_NULL)
    val customerId: Int? = null,
    @JsonInclude(NON_NULL)
    val timeSlotId: Long? = null,
    @JsonInclude(NON_NULL)
    val status: AppointmentStatus? = null,

    ) : Serializable {
    companion object {
        fun from(appointment: Appointment, status: AppointmentStatus): AppointmentCustomerResponse {
            return AppointmentCustomerResponse(
                idempotentKey = appointment.correlationId,
                appointmentId = appointment.appointmentId,
                customerId = appointment.customerId,
                timeSlotId = appointment.timeSlotId,
                status = status
            )
        }
    }

}