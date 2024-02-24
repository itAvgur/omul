package com.itavgur.omul.customer.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import java.io.Serializable

data class CustomerTemporaryDataResponse(
    val customerId: Int,
    @JsonInclude(NON_NULL)
    val phone: String? = null,
    val email: String
) : Serializable {

    companion object {

        fun from(response: CustomerDataResponse): CustomerTemporaryDataResponse {
            return CustomerTemporaryDataResponse(
                customerId = response.customerId,
                email = response.email,
                phone = response.phone
            )
        }
    }
}