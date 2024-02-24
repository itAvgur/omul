package com.itavgur.omul.appointment.workflow.steps

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.itavgur.omul.appointment.dao.AppointmentDao
import com.itavgur.omul.appointment.dao.AppointmentEventDao
import com.itavgur.omul.appointment.domain.Appointment
import com.itavgur.omul.appointment.domain.AppointmentEvent
import com.itavgur.omul.appointment.domain.AppointmentStatus
import com.itavgur.omul.appointment.workflow.WorkflowContext
import com.itavgur.omul.appointment.workflow.WorkflowStep
import com.itavgur.omul.appointment.workflow.dao.WorkflowDao
import com.itavgur.omul.appointment.workflow.dao.WorkflowStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CreateAppointmentStep(
    @Autowired private val appointmentDao: AppointmentDao,
    @Autowired private val appointmentEventDao: AppointmentEventDao,
    @Autowired private val workflowDao: WorkflowDao
) : WorkflowStep {

    companion object {
        val INITIAL_APPOINTMENT_STATUS = AppointmentStatus.CREATED
    }

    override fun run(ctx: WorkflowContext) {
        val newAppointment = Appointment(
            correlationId = ctx.correlationId,
            customerId = ctx.customerId,
            timeSlotId = ctx.timeSlotId,
            created = LocalDateTime.now()
        )

        val result = appointmentDao.createAppointment(newAppointment)
        ctx.appointmentId = result.appointmentId
        ctx.status = INITIAL_APPOINTMENT_STATUS

        val newEvent = AppointmentEvent(appointmentId = ctx.appointmentId!!, status = INITIAL_APPOINTMENT_STATUS)
        newEvent.created = LocalDateTime.now()
        appointmentEventDao.addEvent(newEvent)

        ctx.isLastStepSuccessful = true
        ctx.stepResults.add("timeSlot ${ctx.timeSlotId} has been reserved")

        ctx.workFlowId?.let {
            workflowDao.updateWorkflow(
                ctx.workFlowId!!,
                WorkflowStatus.APPOINTMENT_CREATED,
                jacksonObjectMapper().writeValueAsString(ctx)
            )
        }

    }
}