package com.itavgur.omul.appointment.workflow

interface WorkflowStep {

    fun run(ctx: WorkflowContext)
}