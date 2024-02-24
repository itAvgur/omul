package com.itavgur.omul.customer.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.itavgur.omul.customer.domain.MedicalProcedure
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "CustomerProcedureRequest", description = "Customer medical procedure request")
data class CustomerProcedureRequest(

    val customerId: Int,
    val summary: String,
    val description: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val date: LocalDateTime? = null

) {
    fun toMedicalProcedure(date: LocalDateTime): MedicalProcedure {

        return MedicalProcedure(
            customerId = this.customerId,
            summary = this.summary,
            description = this.description,
            date = date
        )

    }

}