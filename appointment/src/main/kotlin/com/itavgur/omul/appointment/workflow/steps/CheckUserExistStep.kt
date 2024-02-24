package com.itavgur.omul.appointment.workflow.steps

import com.itavgur.omul.appointment.dao.CustomerDao
import com.itavgur.omul.appointment.workflow.WorkflowContext
import com.itavgur.omul.appointment.workflow.WorkflowStep
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CheckUserExistStep(
    @Autowired private val customerDao: CustomerDao
) : WorkflowStep {


    override fun run(ctx: WorkflowContext) {

        if (ctx.customerId == null) {
            ctx.stepResults.add("customerId is null")
            ctx.isLastStepSuccessful = false
        } else {
            val result = customerDao.checkCustomerExist(ctx.customerId!!)
            if (result) {
                ctx.isLastStepSuccessful = true
                ctx.stepResults.add("customerId ${ctx.customerId} exists")
            } else {
                ctx.isLastStepSuccessful = false
                ctx.stepResults.add("customerId ${ctx.customerId} is absent")
            }
        }

    }

}