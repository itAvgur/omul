package com.itavgur.omul.appointment.web.dto

import java.util.*

data class AppointmentNewCustomerRequest(

    val idempotentKey: UUID? = null,
    val timeSlotId: Long? = null,
    val phone: String? = null,
    val email: String? = null
)