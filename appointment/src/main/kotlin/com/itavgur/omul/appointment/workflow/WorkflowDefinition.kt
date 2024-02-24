package com.itavgur.omul.appointment.workflow

class WorkflowDefinition(
    val steps: List<WorkflowStepDefinition>,
)

class WorkflowStepDefinition(
    val workflowStep: WorkflowStep,
    val successNextStep: WorkflowStepDefinition?,
    val failedNextStep: WorkflowStepDefinition?
)