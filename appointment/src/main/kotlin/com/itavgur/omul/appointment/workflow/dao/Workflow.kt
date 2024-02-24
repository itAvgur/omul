package com.itavgur.omul.appointment.workflow.dao

import java.util.*

data class Workflow(
    var id: Int? = null,
    var correlationId: UUID? = null,
    var name: String,
    var status: WorkflowStatus,
    val context: String
) : Cloneable {

    public override fun clone(): Workflow = Workflow(
        id = id,
        name = name,
        status = status,
        context = context
    )
}
