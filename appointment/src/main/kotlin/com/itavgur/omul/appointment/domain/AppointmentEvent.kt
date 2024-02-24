package com.itavgur.omul.appointment.domain

import java.time.LocalDateTime

data class AppointmentEvent(
    var appointmentEventId: Int? = null,
    var appointmentId: Int,
    val status: AppointmentStatus,
    val description: String? = null,
    var created: LocalDateTime? = null
) : Cloneable {

    public override fun clone(): AppointmentEvent = AppointmentEvent(
        appointmentId = appointmentId,
        status = status,
        description = description,
    )
}