package com.itavgur.omul.personnel.web

import com.itavgur.omul.personnel.exception.InvalidRequestException
import com.itavgur.omul.personnel.service.EmployeeService
import com.itavgur.omul.personnel.web.dto.EmployeeDataRequest
import com.itavgur.omul.personnel.web.dto.EmployeeDataResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/employee")
class EmployeeControllerV1(
    @Autowired val employeeService: EmployeeService
) {

    @Operation(summary = "Create employee", tags = ["employeeInfo"])
    @PostMapping
    fun createCustomerV1(
        @Valid @RequestBody request: EmployeeDataRequest,
    ): EmployeeDataResponse {

        if (request.employeeId != null) {
            throw InvalidRequestException("employeeId must be empty")
        }

        return employeeService.saveEmployee(request)
    }

    @Operation(summary = "Get employee info", tags = ["employeeInfo"])
    @GetMapping
    fun getCustomerV1(
        @RequestParam(name = "employeeId", required = true) id: Int,
    ): EmployeeDataResponse = employeeService.getEmployee(id)

    @Operation(summary = "Update existed user personal data", tags = ["employeeInfo"])
    @PutMapping
    fun updateCustomerV1(
        @Valid @RequestBody request: EmployeeDataRequest,
    ): EmployeeDataResponse {
        if (request.employeeId == null) {
            throw InvalidRequestException("employeeId is empty")
        }
        request.employeeId.let {
            return employeeService.saveEmployee(request)
        }
    }

    @Operation(summary = "Delete employee", tags = ["employeeInfo"])
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun deleteCustomerV1(
        @RequestParam(name = "id", required = true) id: Int,
    ) = employeeService.deleteEmployee(id)

}