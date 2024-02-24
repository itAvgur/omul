package com.itavgur.omul.customer.domain

import java.time.LocalDate

data class Customer(
    var customerId: Int? = null,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val gender: Gender? = null,
    val birthDate: LocalDate? = null,
    val documentedId: String? = null,
    val phone: String? = null
) : Cloneable {

    public override fun clone(): Customer = Customer(
        customerId = customerId,
        firstName = firstName,
        lastName = lastName,
        gender = gender,
        birthDate = birthDate,
        documentedId = documentedId,
        phone = phone,
        email = email,
    )
}