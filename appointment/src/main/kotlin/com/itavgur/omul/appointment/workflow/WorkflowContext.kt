package com.itavgur.omul.appointment.workflow

import com.fasterxml.jackson.annotation.JsonProperty
import com.itavgur.omul.appointment.domain.AppointmentStatus
import java.util.*

class WorkflowContext(
    @JsonProperty("idempotentKey")
    val correlationId: UUID? = null,
    var appointmentId: Int? = null,
    var status: AppointmentStatus? = null,
    var customerId: Int? = null,
    val phone: String? = null,
    val timeSlotId: Long,
    val email: String? = null,
    var isLastStepSuccessful: Boolean = true,
    val stepResults: MutableList<Any> = mutableListOf(),
    var workFlowId: Int? = null
)