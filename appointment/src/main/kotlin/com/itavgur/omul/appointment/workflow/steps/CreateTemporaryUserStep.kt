package com.itavgur.omul.appointment.workflow.steps

import com.itavgur.omul.appointment.dao.CustomerDao
import com.itavgur.omul.appointment.workflow.WorkflowContext
import com.itavgur.omul.appointment.workflow.WorkflowStep
import com.itavgur.omul.appointment.workflow.dao.WorkflowDao
import com.itavgur.omul.appointment.workflow.dao.WorkflowStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CreateTemporaryUserStep(
    @Autowired private val customerDao: CustomerDao,
    @Autowired private val workflowDao: WorkflowDao
) : WorkflowStep {

    override fun run(ctx: WorkflowContext) {
        val customerId = customerDao.createTemporaryUser(ctx.phone, ctx.email)
        if (customerId != null) {
            ctx.customerId = customerId
            ctx.isLastStepSuccessful = true
            ctx.stepResults.add("new customer with email ${ctx.email} and customerId $customerId has been created")
        } else {
            ctx.isLastStepSuccessful = false
            ctx.stepResults.add("new customer with email ${ctx.email} hasn't been created")
            markWorkFlowAsError(ctx)
        }
    }

    private fun markWorkFlowAsError(ctx: WorkflowContext) {
        ctx.workFlowId?.let {
            workflowDao.updateWorkflowStatus(ctx.workFlowId!!, WorkflowStatus.ERROR)
        }
    }

}