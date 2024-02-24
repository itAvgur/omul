package com.itavgur.omul.customer.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.itavgur.omul.customer.domain.MedicalTestResult
import java.time.LocalDateTime

data class MedicalTestResultRequest(

    val testId: Int,
    val result: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val date: LocalDateTime? = null

) {
    fun toMedicalTestResult(date: LocalDateTime): MedicalTestResult {

        return MedicalTestResult(
            testId = testId,
            result = result,
            date = date,
        )

    }

}