package com.itavgur.omul.appointment.workflow.steps

import com.itavgur.omul.appointment.dao.ScheduleDao
import com.itavgur.omul.appointment.workflow.WorkflowContext
import com.itavgur.omul.appointment.workflow.WorkflowStep
import com.itavgur.omul.appointment.workflow.dao.WorkflowDao
import com.itavgur.omul.appointment.workflow.dao.WorkflowStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ReleaseTimeSlotStep(
    @Autowired private val scheduleDao: ScheduleDao, @Autowired private val workflowDao: WorkflowDao
) : WorkflowStep {

    override fun run(ctx: WorkflowContext) {
        if (scheduleDao.releaseTimeSlot(ctx.timeSlotId)) {
            ctx.isLastStepSuccessful = true
            ctx.stepResults.add("timeSlot ${ctx.timeSlotId} has been released")
            markWorkFlowAsFinished(ctx)
        } else {
            ctx.isLastStepSuccessful = false
            ctx.stepResults.add("timeSlot ${ctx.timeSlotId} hasn't been released")
        }
    }

    private fun markWorkFlowAsFinished(ctx: WorkflowContext) {
        ctx.workFlowId?.let {
            workflowDao.updateWorkflowStatus(ctx.workFlowId!!, WorkflowStatus.FINISHED)
        }
    }
}