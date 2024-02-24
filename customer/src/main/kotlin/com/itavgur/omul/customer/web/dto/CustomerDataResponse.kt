package com.itavgur.omul.customer.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.itavgur.omul.customer.domain.Customer
import com.itavgur.omul.customer.domain.Gender
import java.io.Serializable
import java.time.LocalDate

data class CustomerDataResponse(

    val customerId: Int,
    @JsonInclude(NON_NULL)
    val firstName: String?,
    @JsonInclude(NON_NULL)
    val lastName: String?,
    @JsonInclude(NON_NULL)
    val gender: Gender?,
    @JsonInclude(NON_NULL)
    val documentId: String?,
    @JsonInclude(NON_NULL)
    val birthDay: LocalDate?,
    @JsonInclude(NON_NULL)
    val phone: String?,
    val email: String

) : Serializable {
    companion object {
        fun from(customer: Customer): CustomerDataResponse {
            return CustomerDataResponse(
                customerId = customer.customerId!!,
                firstName = customer.firstName,
                lastName = customer.lastName,
                gender = customer.gender,
                documentId = customer.documentedId,
                birthDay = customer.birthDate,
                phone = customer.phone,
                email = customer.email

            )
        }
    }
}
