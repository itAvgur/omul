package com.itavgur.omul.appointment.service

import com.itavgur.omul.appointment.web.dto.AppointmentCustomerRequest
import com.itavgur.omul.appointment.web.dto.AppointmentCustomerResponse
import com.itavgur.omul.appointment.web.dto.AppointmentNewCustomerRequest
import java.util.*

interface AppointmentService {

    fun createAppointmentRegisteredCustomer(request: AppointmentCustomerRequest): AppointmentCustomerResponse

    fun createAppointmentAnonymousCustomer(request: AppointmentNewCustomerRequest): AppointmentCustomerResponse

    fun getCustomerAppointment(appointmentId: Int?, correlationId: UUID?): AppointmentCustomerResponse

    fun updateCustomerAppointment(request: AppointmentCustomerRequest): AppointmentCustomerResponse

    fun deleteCustomerAppointment(appointmentId: Int): Int

}