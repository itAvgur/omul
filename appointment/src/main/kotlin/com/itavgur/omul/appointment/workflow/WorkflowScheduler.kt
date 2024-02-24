package com.itavgur.omul.appointment.workflow

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.itavgur.omul.appointment.config.TransportTypeAsyncConfig
import com.itavgur.omul.appointment.dao.AppointmentDao
import com.itavgur.omul.appointment.service.AppointmentServiceAsync.Companion.WORKFLOW_CREATE_APPOINTMENT_NAME
import com.itavgur.omul.appointment.util.logger
import com.itavgur.omul.appointment.workflow.dao.WorkflowDao
import com.itavgur.omul.appointment.workflow.dao.WorkflowStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
@ConditionalOnBean(TransportTypeAsyncConfig::class)
@EnableAsync
@EnableScheduling
class WorkflowScheduler(
    @Autowired val workflowDao: WorkflowDao,
    @Autowired val createAppointmentWorkflow: WorkflowDefinition,
    @Autowired val cancelAppointmentWorkflow: WorkflowDefinition,
    @Autowired val errorAppointmentWorkflow: WorkflowDefinition
) {

    @Value("\${transport.async.rate:1}")
    private val batchRate: Long = 1

    @Value("\${transport.async.batchSize:100}")
    val batchSize: Int = 10

    companion object {
        val LOG by logger()
        const val SCHEDULER_RATE_SECONDS = 1L
    }

    @Scheduled(fixedRate = SCHEDULER_RATE_SECONDS, timeUnit = TimeUnit.SECONDS)
    fun createAppointmentWorkflow() {
        LOG.debug("Start createAppointmentWorkflow()")

        workflowDao.getWorkflowsFiltered(
            WorkflowStatus.CREATED,
            WORKFLOW_CREATE_APPOINTMENT_NAME, batchSize
        )
            .forEach {
                workflowDao.updateWorkflowStatus(it.id!!, WorkflowStatus.IN_PROGRESS)
                val context = jacksonObjectMapper().readValue(it.context, WorkflowContext::class.java)
                context.workFlowId = it.id
                runProcess(context, createAppointmentWorkflow)
            }

        LOG.debug("Finish createAppointmentWorkflow()")
    }

    @Scheduled(fixedRate = SCHEDULER_RATE_SECONDS, timeUnit = TimeUnit.SECONDS)
    fun cancelAppointmentWorkflow() {
        LOG.debug("Start cancelAppointmentWorkflow()")

        workflowDao.getWorkflowsFiltered(WorkflowStatus.CANCELED, WORKFLOW_CREATE_APPOINTMENT_NAME, batchSize)
            .forEach {
                val context = jacksonObjectMapper().readValue(it.context, WorkflowContext::class.java)
                context.workFlowId = it.id
                runProcess(context, cancelAppointmentWorkflow)
            }

        LOG.debug("Finish cancelAppointmentWorkflow()")
    }

    @Scheduled(fixedRate = SCHEDULER_RATE_SECONDS, timeUnit = TimeUnit.SECONDS)
    fun errorAppointmentWorkflow() {
        LOG.debug("Start errorAppointmentWorkflow()")

        workflowDao.getWorkflowsFiltered(WorkflowStatus.ERROR, WORKFLOW_CREATE_APPOINTMENT_NAME, batchSize)
            .forEach {
                val context = jacksonObjectMapper().readValue(it.context, WorkflowContext::class.java)
                context.workFlowId = it.id
                runProcess(context, errorAppointmentWorkflow)
            }

        LOG.debug("Finish errorAppointmentWorkflow()")
    }

    @Async
    fun runProcess(ctx: WorkflowContext, workflow: WorkflowDefinition) {

        var stepDefinition: WorkflowStepDefinition? = workflow.steps[0]
        while (stepDefinition != null) {

            stepDefinition.workflowStep.run(ctx)
            stepDefinition = if (ctx.isLastStepSuccessful) {
                stepDefinition.successNextStep
            } else {
                stepDefinition.failedNextStep
            }
        }
    }

}