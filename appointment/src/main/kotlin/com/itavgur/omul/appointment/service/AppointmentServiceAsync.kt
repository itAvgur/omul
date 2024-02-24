package com.itavgur.omul.appointment.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.itavgur.omul.appointment.auth.JwtService
import com.itavgur.omul.appointment.config.TransportTypeAsyncConfig
import com.itavgur.omul.appointment.dao.AppointmentDao
import com.itavgur.omul.appointment.dao.AppointmentEventDao
import com.itavgur.omul.appointment.domain.Appointment
import com.itavgur.omul.appointment.domain.AppointmentEvent
import com.itavgur.omul.appointment.domain.AppointmentStatus
import com.itavgur.omul.appointment.exception.AppointmentNotFoundException
import com.itavgur.omul.appointment.exception.InvalidRequestException
import com.itavgur.omul.appointment.web.dto.AppointmentCustomerRequest
import com.itavgur.omul.appointment.web.dto.AppointmentCustomerResponse
import com.itavgur.omul.appointment.web.dto.AppointmentNewCustomerRequest
import com.itavgur.omul.appointment.workflow.dao.Workflow
import com.itavgur.omul.appointment.workflow.dao.WorkflowDao
import com.itavgur.omul.appointment.workflow.dao.WorkflowStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Primary
@Service
@ConditionalOnBean(TransportTypeAsyncConfig::class)
class AppointmentServiceAsync(
    @Autowired private val eventDao: AppointmentEventDao,
    @Autowired private val appointmentDao: AppointmentDao,
    @Autowired private val jwtService: JwtService,
    @Autowired private val workflowDao: WorkflowDao,
) : AppointmentService {

    companion object {
        const val WORKFLOW_CREATE_APPOINTMENT_NAME = "create_appointment"
        val objectMapper = jacksonObjectMapper()
    }


    override fun createAppointmentRegisteredCustomer(request: AppointmentCustomerRequest): AppointmentCustomerResponse {

        val order = Workflow(
            correlationId = request.idempotentKey,
            status = WorkflowStatus.CREATED,
            context = objectMapper.writeValueAsString(request),
            name = WORKFLOW_CREATE_APPOINTMENT_NAME
        )
        workflowDao.addWorkflow(order)

        return AppointmentCustomerResponse(
            customerId = request.customerId, idempotentKey = request.idempotentKey, status = AppointmentStatus.PENDING
        )
    }

    override fun createAppointmentAnonymousCustomer(request: AppointmentNewCustomerRequest): AppointmentCustomerResponse {

        val order = Workflow(
            correlationId = request.idempotentKey,
            status = WorkflowStatus.CREATED,
            context = objectMapper.writeValueAsString(request),
            name = WORKFLOW_CREATE_APPOINTMENT_NAME
        )
        workflowDao.addWorkflow(order)

        return AppointmentCustomerResponse(
            idempotentKey = request.idempotentKey, status = AppointmentStatus.PENDING
        )
    }

    override fun getCustomerAppointment(appointmentId: Int?, correlationId: UUID?): AppointmentCustomerResponse {

        appointmentId?.let {
            jwtService.validateIdWithJwt(appointmentId)
        }

        if (appointmentId != null) {
            appointmentDao.getAppointmentById(appointmentId)?.let {
                return AppointmentCustomerResponse.from(it, eventDao.getLastEvent(appointmentId).status)
            }
        } else if (correlationId != null) {
            appointmentDao.getAppointmentByUUID(correlationId)?.let {
                return AppointmentCustomerResponse.from(it, eventDao.getLastEvent(it.appointmentId!!).status)
            }
        }

        throw AppointmentNotFoundException("appointment with id $appointmentId and key $correlationId is absent")
    }

    override fun updateCustomerAppointment(request: AppointmentCustomerRequest): AppointmentCustomerResponse {

        if (request.status == null) {
            throw InvalidRequestException("statusId is absent")
        }

        val appointment: Appointment? = if (request.appointmentId != null) {
            appointmentDao.getAppointmentById(request.appointmentId)
        } else if (request.idempotentKey != null) {
            appointmentDao.getAppointmentByUUID(request.idempotentKey)
        } else {
            throw InvalidRequestException("both appointmentId and idempotentKey is absent")
        }
        appointment ?: throw InvalidRequestException("appointment is absent")

        val lastEvent = eventDao.getLastEvent(appointment.appointmentId!!)

        if (lastEvent.status.order > request.status.order) {
            throw InvalidRequestException("declared appointment status is invalid")
        }

        val newEvent = AppointmentEvent(appointmentId = appointment.appointmentId!!, status = request.status)
        newEvent.created = LocalDateTime.now()

        eventDao.addEvent(newEvent)

        processWorkflow(lastEvent.status, request.status, appointment)

        return AppointmentCustomerResponse(
            idempotentKey = request.idempotentKey,
            appointmentId = request.appointmentId,
            customerId = request.customerId,
            timeSlotId = request.timeSlotId,
            status = request.status
        )
    }

    private fun processWorkflow(oldStatus: AppointmentStatus, newStatus: AppointmentStatus?, appointment: Appointment) {

        if (newStatus == AppointmentStatus.CANCELED && oldStatus != AppointmentStatus.CANCELED) {
            workflowDao.updateWorkflowStatus(appointment.correlationId!!, WorkflowStatus.CANCELED)
        }

    }

    override fun deleteCustomerAppointment(appointmentId: Int): Int {
        TODO("Not yet implemented")
    }

}