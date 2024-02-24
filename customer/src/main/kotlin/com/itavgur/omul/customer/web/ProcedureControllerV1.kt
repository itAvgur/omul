package com.itavgur.omul.customer.web

import com.itavgur.omul.customer.service.MedicalProcedureService
import com.itavgur.omul.customer.web.dto.CustomerProcedureRequest
import com.itavgur.omul.customer.web.dto.CustomerProcedureResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/procedure")
class ProcedureControllerV1(
    @Autowired
    val medicalProcedureService: MedicalProcedureService
) {

    @Operation(summary = "Create new procedure for customer", tags = ["procedureInfo"])
    @PostMapping
    fun createProcedureForCustomerV1(
        @Valid @RequestBody request: CustomerProcedureRequest,
    ): CustomerProcedureResponse {
        return medicalProcedureService.createProcedure(request)
    }

    @Operation(
        summary = "Get customer procedures",
        description = "Get customer medical data, procedures",
        tags = ["procedureInfo"]
    )
    @GetMapping
    fun getCustomerProceduresV1(
        @RequestParam(name = "customerId", required = true) customerId: Int,
    ): List<CustomerProcedureResponse> = medicalProcedureService.getProcedures(customerId)

}