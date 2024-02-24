package com.itavgur.omul.customer.domain

import java.time.LocalDateTime

data class MedicalTest(
    var testId: Int? = null,
    var customerId: Int,
    val summary: String,
    val description: String?,
    val date: LocalDateTime,
    var results: List<MedicalTestResult>
) : Cloneable {

    public override fun clone(): MedicalTest = MedicalTest(
        testId = testId,
        customerId = customerId,
        summary = summary,
        description = description,
        results = results,
        date = date
    )
}

data class MedicalTestResult(
    var testResultId: Int? = null, val testId: Int, val result: String, val date: LocalDateTime
) {
    fun clone(): MedicalTestResult {
        return MedicalTestResult(
            testId = testId, result = result, date = date
        )
    }
}