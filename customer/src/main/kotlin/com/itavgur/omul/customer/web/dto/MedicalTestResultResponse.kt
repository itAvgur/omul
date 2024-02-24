package com.itavgur.omul.customer.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.itavgur.omul.customer.domain.MedicalTestResult
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import java.time.LocalDateTime

@Schema(name = "CustomerTestResultResponse", description = "Customer medical test result response")
data class MedicalTestResultResponse(

    val result: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val date: LocalDateTime? = null

) : Serializable {
    companion object {
        fun from(test: MedicalTestResult): MedicalTestResultResponse {
            return MedicalTestResultResponse(
                result = test.result,
                date = test.date,
            )
        }
    }

}