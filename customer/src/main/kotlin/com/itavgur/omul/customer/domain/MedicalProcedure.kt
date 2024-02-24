package com.itavgur.omul.customer.domain

import java.time.LocalDateTime

data class MedicalProcedure(
    var procedureId: Int? = null,
    var customerId: Int,
    val summary: String,
    val description: String? = null,
    val date: LocalDateTime
) : Cloneable {

    public override fun clone(): MedicalProcedure = MedicalProcedure(
        procedureId = procedureId,
        customerId = customerId,
        summary = summary,
        description = description,
        date = date
    )
}