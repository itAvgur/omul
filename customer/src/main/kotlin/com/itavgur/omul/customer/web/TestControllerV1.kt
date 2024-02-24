package com.itavgur.omul.customer.web

import com.itavgur.omul.customer.service.MedicalTestService
import com.itavgur.omul.customer.web.dto.MedicalTestRequest
import com.itavgur.omul.customer.web.dto.MedicalTestResponse
import com.itavgur.omul.customer.web.dto.MedicalTestResultRequest
import com.itavgur.omul.customer.web.dto.MedicalTestResultResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/test")
class TestControllerV1(
    @Autowired
    val medicalTestService: MedicalTestService
) {

    @Operation(summary = "Create new test for customer", tags = ["testInfo"])
    @PostMapping
    fun createTestForCustomerV1(
        @Valid @RequestBody request: MedicalTestRequest,
    ): MedicalTestResponse {
        return medicalTestService.createTest(request)
    }

    @Operation(
        summary = "Get customer tests",
        description = "Get customer medical data, tests",
        tags = ["testInfo"]
    )
    @GetMapping
    fun getCustomerTestsV1(
        @RequestParam(name = "customerId", required = true) id: Int,
    ): List<MedicalTestResponse> = medicalTestService.getMedicalTests(id)

    @Operation(
        summary = "Add result to test",
        tags = ["testInfo"]
    )
    @PostMapping("/result")
    fun addResultToTestV1(
        @Valid @RequestBody request: MedicalTestResultRequest,
    ): MedicalTestResultResponse = medicalTestService.addResultToMedicalTest(request)

}