package com.itavgur.omul.appointment.service

import com.itavgur.omul.appointment.auth.JwtService
import com.itavgur.omul.appointment.config.TransportTypeSyncConfig
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
import com.itavgur.omul.appointment.workflow.WorkflowContext
import com.itavgur.omul.appointment.workflow.WorkflowDefinition
import com.itavgur.omul.appointment.workflow.WorkflowStepDefinition
import com.itavgur.omul.appointment.workflow.dao.WorkflowDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
@ConditionalOnBean(TransportTypeSyncConfig::class)
class AppointmentServiceSync(
    @Autowired val eventDao: AppointmentEventDao,
    @Autowired val appointmentDao: AppointmentDao,
    @Autowired val workflowDao: WorkflowDao,
    @Autowired val jwtService: JwtService,
    @Autowired val createAppointmentWorkflow: WorkflowDefinition,
    @Autowired val cancelAppointmentWorkflow: WorkflowDefinition
) : AppointmentService {

    override fun createAppointmentRegisteredCustomer(request: AppointmentCustomerRequest): AppointmentCustomerResponse {

        if (request.timeSlotId == null) {
            throw InvalidRequestException("timeSlotId is empty")
        }

        val workflowContext = WorkflowContext(
            correlationId = request.idempotentKey,
            customerId = request.customerId,
            timeSlotId = request.timeSlotId,
        )

        return runCreateAppointmentWorkflow(workflowContext)
    }

    override fun createAppointmentAnonymousCustomer(request: AppointmentNewCustomerRequest): AppointmentCustomerResponse {
        if (request.timeSlotId == null) {
            throw InvalidRequestException("timeSlotId is empty")
        }

        val workflowContext = WorkflowContext(
            correlationId = request.idempotentKey,
            phone = request.phone,
            timeSlotId = request.timeSlotId,
            email = request.email,
        )

        return runCreateAppointmentWorkflow(workflowContext)
    }

    private fun runCreateAppointmentWorkflow(ctx: WorkflowContext): AppointmentCustomerResponse {

        var stepDefinition: WorkflowStepDefinition? = createAppointmentWorkflow.steps[0]
        while (stepDefinition != null) {

            stepDefinition.workflowStep.run(ctx)
            stepDefinition = if (ctx.isLastStepSuccessful) {
                stepDefinition.successNextStep
            } else {
                stepDefinition.failedNextStep
            }
        }

        val lastResult = ctx.stepResults.last()

        if (ctx.isLastStepSuccessful) {
            return AppointmentCustomerResponse(
                idempotentKey = ctx.correlationId,
                appointmentId = ctx.appointmentId,
                customerId = ctx.customerId,
                timeSlotId = ctx.timeSlotId,
                status = ctx.status,
            )

        } else {
            throw InvalidRequestException(lastResult as String)
        }
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

        val appointment: Appointment?
        if (request.appointmentId != null) {
            appointment = appointmentDao.getAppointmentById(request.appointmentId)
        } else if (request.idempotentKey != null) {
            appointment = appointmentDao.getAppointmentByUUID(request.idempotentKey)
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

        val workflowContext = WorkflowContext(
            correlationId = request.idempotentKey,
            timeSlotId = appointment.timeSlotId!!
        )

        if (request.status == AppointmentStatus.CANCELED) {
            runCancelAppointmentWorkflow(workflowContext)
        }

        eventDao.addEvent(newEvent)

        return AppointmentCustomerResponse(
            idempotentKey = request.idempotentKey,
            appointmentId = request.appointmentId,
            customerId = request.customerId,
            timeSlotId = request.timeSlotId,
            status = request.status
        )
    }

    private fun runCancelAppointmentWorkflow(ctx: WorkflowContext): AppointmentCustomerResponse {

        var stepDefinition: WorkflowStepDefinition? = cancelAppointmentWorkflow.steps[0]
        while (stepDefinition != null) {

            stepDefinition.workflowStep.run(ctx)
            stepDefinition = if (ctx.isLastStepSuccessful) {
                stepDefinition.successNextStep
            } else {
                stepDefinition.failedNextStep
            }
        }

        val lastResult = ctx.stepResults.last()

        if (ctx.isLastStepSuccessful) {
            return AppointmentCustomerResponse(
                idempotentKey = ctx.correlationId,
                appointmentId = ctx.appointmentId,
                customerId = ctx.customerId,
                timeSlotId = ctx.timeSlotId,
                status = ctx.status,
            )

        } else {
            throw InvalidRequestException(lastResult as String)
        }
    }

    override fun deleteCustomerAppointment(appointmentId: Int): Int {
        TODO("Not yet implemented")
    }

}