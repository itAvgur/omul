package com.itavgur.omul.customer.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.itavgur.omul.customer.domain.MedicalProcedure
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import java.time.LocalDateTime

@Schema(name = "CustomerProcedureResponse", description = "Customer medical procedure response")
data class CustomerProcedureResponse(

    val procedureId: Int,
    val customerId: Int,
    val summary: String,
    @JsonInclude(NON_NULL)
    val description: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val date: LocalDateTime

) : Serializable {
    companion object {
        fun from(medicalProcedure: MedicalProcedure): CustomerProcedureResponse {
            return CustomerProcedureResponse(
                procedureId = medicalProcedure.procedureId!!,
                customerId = medicalProcedure.customerId,
                summary = medicalProcedure.summary,
                description = medicalProcedure.description,
                date = medicalProcedure.date

            )
        }
    }
}