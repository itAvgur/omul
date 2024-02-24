package com.itavgur.omul.customer.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.itavgur.omul.customer.domain.MedicalTest
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "CustomerTestRequest", description = "Customer medical test request")
data class MedicalTestRequest(

    val customerId: Int,
    val testId: Int,
    val summary: String,
    val description: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val date: LocalDateTime? = null

) {
    fun toMedicalTest(date: LocalDateTime): MedicalTest {

        return MedicalTest(
            customerId = customerId,
            testId = testId,
            summary = summary,
            description = description,
            date = date,
            results = emptyList(),
        )

    }

}