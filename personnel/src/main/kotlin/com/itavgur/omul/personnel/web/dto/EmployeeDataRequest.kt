package com.itavgur.omul.personnel.web.dto

import com.itavgur.omul.personnel.domain.Employee
import com.itavgur.omul.personnel.domain.Gender
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(name = "EmployeeDataRequest", description = "Employee data request (create/update)")
data class EmployeeDataRequest(

    val employeeId: Int? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: Gender,
    val documentId: String,
    val birthDay: LocalDate,
    val phone: String,
    val qualification: String
) {
    fun toEmployee(): Employee {

        return Employee(
            employeeId = employeeId,
            firstName = firstName,
            lastName = lastName,
            gender = gender,
            documentedId = documentId,
            birthDate = birthDay,
            phone = phone,
            email = email,
            qualification = qualification
        )
    }

}