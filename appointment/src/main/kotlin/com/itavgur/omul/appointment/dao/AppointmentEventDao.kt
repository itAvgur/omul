package com.itavgur.omul.appointment.dao

import com.itavgur.omul.appointment.domain.AppointmentEvent

interface AppointmentEventDao {

    fun addEvent(event: AppointmentEvent): AppointmentEvent?

    fun getEventById(eventId: Int): AppointmentEvent?

    fun getLastEvent(appointmentId: Int): AppointmentEvent

}