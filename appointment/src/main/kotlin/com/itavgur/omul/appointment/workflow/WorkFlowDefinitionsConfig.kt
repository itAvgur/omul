package com.itavgur.omul.appointment.workflow

import com.itavgur.omul.appointment.workflow.steps.*
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WorkFlowDefinitionsConfig {

    @Bean
    fun createAppointmentWorkflow(ctx: ApplicationContext): WorkflowDefinition {
        val checkTimeSlotStep = ctx.getBean(CheckTimeSlotStep::class.java)
        val checkUserExistStep = ctx.getBean(CheckUserExistStep::class.java)
        val createTemporaryUserStep = ctx.getBean(CreateTemporaryUserStep::class.java)
        val reserveTimeSlotStep = ctx.getBean(ReserveTimeSlotStep::class.java)
        val createAppointmentStep = ctx.getBean(CreateAppointmentStep::class.java)

        val createAppointment = WorkflowStepDefinition(createAppointmentStep, null, null)
        val reserveTimeSlot = WorkflowStepDefinition(reserveTimeSlotStep, createAppointment, null)
        val createTemporaryUser = WorkflowStepDefinition(createTemporaryUserStep, reserveTimeSlot, null)
        val checkUserExist = WorkflowStepDefinition(checkUserExistStep, reserveTimeSlot, createTemporaryUser)

        return WorkflowDefinition(
            listOf(
                WorkflowStepDefinition(checkTimeSlotStep, checkUserExist, null),
                WorkflowStepDefinition(checkUserExistStep, reserveTimeSlot, createTemporaryUser),
                WorkflowStepDefinition(createTemporaryUserStep, null, reserveTimeSlot),
                WorkflowStepDefinition(reserveTimeSlotStep, createAppointment, null),
                WorkflowStepDefinition(createAppointmentStep, null, null)
            )
        )
    }

    @Bean
    fun cancelAppointmentWorkflow(ctx: ApplicationContext): WorkflowDefinition {
        val releaseTimeSlotStep = ctx.getBean(ReleaseTimeSlotStep::class.java)

        return WorkflowDefinition(
            listOf(
                WorkflowStepDefinition(releaseTimeSlotStep, null, null)
            )
        )
    }

    @Bean
    fun errorAppointmentWorkflow(ctx: ApplicationContext): WorkflowDefinition {
        val releaseTimeSlotStep = ctx.getBean(ReleaseTimeSlotStep::class.java)

        return WorkflowDefinition(
            listOf(
                WorkflowStepDefinition(releaseTimeSlotStep, null, null)
            )
        )
    }

}