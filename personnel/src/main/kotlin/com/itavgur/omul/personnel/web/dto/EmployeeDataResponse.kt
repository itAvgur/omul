package com.itavgur.omul.personnel.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.itavgur.omul.personnel.domain.Employee
import com.itavgur.omul.personnel.domain.Gender
import java.io.Serializable
import java.time.LocalDate

data class EmployeeDataResponse(

    val employeeId: Int,
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
    val email: String,
    val qualification: String

) : Serializable {
    companion object {
        fun from(employee: Employee): EmployeeDataResponse {
            return EmployeeDataResponse(
                employeeId = employee.employeeId!!,
                firstName = employee.firstName,
                lastName = employee.lastName,
                gender = employee.gender,
                documentId = employee.documentedId,
                birthDay = employee.birthDate,
                phone = employee.phone,
                email = employee.email,
                qualification = employee.qualification

            )
        }
    }
}