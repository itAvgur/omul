package com.itavgur.omul.appointment.workflow.steps

import com.itavgur.omul.appointment.dao.ScheduleDao
import com.itavgur.omul.appointment.workflow.WorkflowContext
import com.itavgur.omul.appointment.workflow.WorkflowStep
import com.itavgur.omul.appointment.workflow.dao.WorkflowDao
import com.itavgur.omul.appointment.workflow.dao.WorkflowStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ReserveTimeSlotStep(
    @Autowired private val scheduleDao: ScheduleDao, @Autowired private val workflowDao: WorkflowDao
) : WorkflowStep {

    override fun run(ctx: WorkflowContext) {
        val result = scheduleDao.reserveTimeSlot(ctx.timeSlotId, ctx.customerId!!)
        if (result) {
            ctx.isLastStepSuccessful = true
            ctx.stepResults.add("timeSlot ${ctx.timeSlotId} has been reserved")
        } else {
            ctx.isLastStepSuccessful = false
            ctx.stepResults.add("timeSlot ${ctx.timeSlotId} hasn't been reserved")
            markWorkFlowAsError(ctx)
        }
    }

    private fun markWorkFlowAsError(ctx: WorkflowContext) {
        ctx.workFlowId?.let {
            workflowDao.updateWorkflowStatus(ctx.workFlowId!!, WorkflowStatus.ERROR)
        }
    }
}