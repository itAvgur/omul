package com.itavgur.omul.customer.web

import com.itavgur.omul.customer.exception.InvalidRequestException
import com.itavgur.omul.customer.service.CustomerService
import com.itavgur.omul.customer.web.dto.CustomerDataRequest
import com.itavgur.omul.customer.web.dto.CustomerDataResponse
import com.itavgur.omul.customer.web.dto.CustomerTemporaryDataRequest
import com.itavgur.omul.customer.web.dto.CustomerTemporaryDataResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.slf4j.event.Level
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/customer")
class CustomerControllerV1(
    @Autowired
    val customerService: CustomerService
) {

    @Operation(summary = "Create new temporary customer", tags = ["customerInfo"])
    @PostMapping("/newby")
    fun createCustomerTemporaryV1(
        @Valid @RequestBody request: CustomerTemporaryDataRequest,
    ): CustomerTemporaryDataResponse {
        return customerService.createTemporaryCustomer(request)
    }

    @Operation(summary = "Create permanent customer", tags = ["customerInfo"])
    @PostMapping
    fun createCustomerV1(
        @Valid @RequestBody request: CustomerDataRequest,
    ): CustomerDataResponse {
        request.customerId?.let {
            throw InvalidRequestException("customerId must be empty", Level.WARN)
        }
        return customerService.createCustomer(request)
    }

    @Operation(summary = "Get customer info", tags = ["customerInfo"])
    @GetMapping
    fun getCustomerV1(
        @RequestParam(name = "customerId", required = true) id: Int,
    ): CustomerDataResponse = customerService.getCustomer(id)

    @Operation(summary = "Update existed user personal data", tags = ["customerInfo"])
    @PutMapping
    fun updateCustomerV1(
        @Valid @RequestBody request: CustomerDataRequest,
    ): CustomerDataResponse {
        request.customerId?.let {
            return customerService.saveCustomer(request)
        }
        throw InvalidRequestException("customerId is empty")
    }

    @Operation(summary = "Delete customer", tags = ["customerInfo"])
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun deleteCustomerV1(
        @RequestParam(name = "id", required = true) id: Int,
    ) = customerService.deleteCustomer(id)

}