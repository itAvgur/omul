package com.itavgur.omul.customer.web.dto

import com.itavgur.omul.customer.domain.Customer
import com.itavgur.omul.customer.domain.Gender
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

@Schema(name = "CustomerDataRequest", description = "Customer data request (create/update)")
data class CustomerDataRequest(

    val customerId: Int? = null,
    @field:NotBlank
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val gender: Gender? = null,
    val documentId: String? = null,
    val birthDay: LocalDate? = null,
    val phone: String? = null
) {
    fun toCustomer(): Customer {

        return Customer(
            customerId = this.customerId,
            firstName = this.firstName,
            lastName = this.lastName,
            gender = this.gender,
            documentedId = this.documentId,
            birthDate = this.birthDay,
            phone = this.phone,
            email = this.email
        )

    }

    companion object {
        fun from(request: CustomerTemporaryDataRequest): CustomerDataRequest {
            return CustomerDataRequest(
                email = request.email,
                phone = request.phone
            )
        }
    }
}
