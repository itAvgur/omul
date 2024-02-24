package com.itavgur.omul.appointment.domain

import java.time.LocalDateTime
import java.util.*

data class Appointment(
    var appointmentId: Int? = null,
    var correlationId: UUID? = null,
    val customerId: Int? = null,
    val timeSlotId: Long? = null,
    val created: LocalDateTime? = null,
    val events: List<AppointmentEvent>? = null
) : Cloneable {

    public override fun clone(): Appointment = Appointment(
        appointmentId = appointmentId,
        customerId = customerId,
        timeSlotId = timeSlotId,
        events = events,
        correlationId = correlationId,
        created = created
    )
}