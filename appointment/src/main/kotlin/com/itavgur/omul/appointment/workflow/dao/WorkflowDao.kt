package com.itavgur.omul.appointment.workflow.dao

import java.util.*

interface WorkflowDao {

    fun getWorkflowsFiltered(status: WorkflowStatus, name: String, limit: Int? = null): List<Workflow>

    fun addWorkflow(workflow: Workflow): Workflow

    fun updateWorkflow(workflow: Workflow): Workflow

    fun updateWorkflow(id: Int, status: WorkflowStatus, context: String): Boolean

    fun updateWorkflowStatus(id: Int, status: WorkflowStatus): Boolean

    fun updateWorkflowStatus(correlationId: UUID, status: WorkflowStatus): Boolean

}