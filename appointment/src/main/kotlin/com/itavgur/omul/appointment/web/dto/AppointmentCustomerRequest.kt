package com.itavgur.omul.appointment.web.dto

import com.itavgur.omul.appointment.domain.AppointmentStatus
import java.util.*

data class AppointmentCustomerRequest(

    val idempotentKey: UUID? = null,
    val appointmentId: Int? = null,
    val customerId: Int? = null,
    val timeSlotId: Long? = null,
    val status: AppointmentStatus? = null
)