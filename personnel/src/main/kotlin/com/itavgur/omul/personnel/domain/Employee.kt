package com.itavgur.omul.personnel.domain

import java.time.LocalDate

data class Employee(
    var employeeId: Int? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: Gender,
    val birthDate: LocalDate,
    val documentedId: String,
    val phone: String,
    val qualification: String
) : Cloneable {

    public override fun clone(): Employee = Employee(
        employeeId = employeeId,
        firstName = firstName,
        lastName = lastName,
        gender = gender,
        birthDate = birthDate,
        documentedId = documentedId,
        phone = phone,
        email = email,
        qualification = qualification
    )
}