package com.itavgur.omul.customer.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.itavgur.omul.customer.domain.MedicalTest
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import java.time.LocalDateTime

@Schema(name = "CustomerTestResponse", description = "Customer medical test response")
data class MedicalTestResponse(

    val testId: Int,
    val customerId: Int,
    val summary: String,
    @JsonInclude(NON_NULL)
    val description: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val date: LocalDateTime,
    val results: List<MedicalTestResultResponse>


) : Serializable {
    companion object {
        fun from(test: MedicalTest): MedicalTestResponse {
            return MedicalTestResponse(
                testId = test.testId!!,
                customerId = test.customerId,
                summary = test.summary,
                description = test.description,
                date = test.date,
                results = test.results.map {
                    MedicalTestResultResponse.from(it)
                }.toList()

            )
        }
    }
}