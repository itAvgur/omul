package com.itavgur.omul.appointment.dao

import com.itavgur.omul.appointment.domain.Appointment
import java.util.*

interface AppointmentDao {

    fun getAppointmentById(appointmentId: Int): Appointment?

    fun getAppointmentByUUID(uuid: UUID): Appointment?

    fun createAppointment(appointment: Appointment): Appointment

    fun updateAppointment(appointment: Appointment): Appointment

    fun deleteAppointment(appointmentId: Int): Int

}