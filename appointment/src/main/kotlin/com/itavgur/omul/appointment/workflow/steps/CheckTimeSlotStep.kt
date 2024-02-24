package com.itavgur.omul.appointment.workflow.steps

import com.itavgur.omul.appointment.dao.ScheduleDao
import com.itavgur.omul.appointment.workflow.WorkflowContext
import com.itavgur.omul.appointment.workflow.WorkflowStep
import com.itavgur.omul.appointment.workflow.dao.WorkflowDao
import com.itavgur.omul.appointment.workflow.dao.WorkflowStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CheckTimeSlotStep(
    @Autowired private val scheduleDao: ScheduleDao,
    @Autowired private val workflowDao: WorkflowDao
) : WorkflowStep {

    override fun run(ctx: WorkflowContext) {

        val result = scheduleDao.checkTimeSlotIsFree(ctx.timeSlotId)
        if (result) {
            ctx.isLastStepSuccessful = true
            ctx.stepResults.add("timeSlot ${ctx.timeSlotId} is free")
        } else {
            ctx.isLastStepSuccessful = false
            ctx.stepResults.add("timeSlot ${ctx.timeSlotId} isn't free")
            markWorkFlowAsError(ctx)
        }
    }

    private fun markWorkFlowAsError(ctx: WorkflowContext) {
        ctx.workFlowId?.let {
            workflowDao.updateWorkflowStatus(ctx.workFlowId!!, WorkflowStatus.ERROR)
        }
    }
}