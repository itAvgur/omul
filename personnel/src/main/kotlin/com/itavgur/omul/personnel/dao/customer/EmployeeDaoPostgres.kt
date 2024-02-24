package com.itavgur.omul.personnel.dao.customer

import com.itavgur.omul.personnel.config.DatabasePostgresConfig
import com.itavgur.omul.personnel.domain.Employee
import com.itavgur.omul.personnel.domain.Gender
import com.itavgur.omul.personnel.util.DBUtil.Companion.getDateValue
import com.itavgur.omul.personnel.util.DBUtil.Companion.getIntValue
import com.itavgur.omul.personnel.util.DBUtil.Companion.getStringValue
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet


@Primary
@Repository
@ConditionalOnBean(DatabasePostgresConfig::class)
class EmployeeDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : EmployeeDao {

    companion object {

        const val QUERY_GET_EMPLOYEE_BY_ID =
            """SELECT e.employee_id ,e.first_name, e.last_name, e.gender, 
                e.birth_date, e.document_id, e.phone, e.email,e.qualification
                FROM employees e
                WHERE e.employee_id = :employeeId"""

        const val QUERY_INSERT_EMPLOYEE =
            """INSERT INTO employees (first_name, last_name, gender, birth_date, document_id, 
                phone, email, qualification)
                VALUES (:firstName, :lastName, :gender, :birthDate, :documentId, :phone, :email, :qualification)"""

        const val QUERY_UPDATE_EMPLOYEE =
            """UPDATE employees
                SET first_name = :firstName, last_name = :lastName, gender = :gender, birth_date = :birthDate, 
                document_id = :documentId, phone = :phone, email = :email, qualification = :qualification
                WHERE employees.employee_id = :employeeId"""
    }

    override fun getEmployeeById(id: Int): Employee? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_GET_EMPLOYEE_BY_ID, MapSqlParameterSource("employeeId", id), EmployeeRowMapper()
            )
        return result.firstOrNull()
    }

    override fun createEmployee(employee: Employee): Employee {
        val map = MapSqlParameterSource(
            mapOf(
                "firstName" to employee.firstName,
                "lastName" to employee.lastName,
                "gender" to employee.gender.toString(),
                "birthDate" to employee.birthDate,
                "documentId" to employee.documentedId,
                "phone" to employee.phone,
                "email" to employee.email,
                "qualification" to employee.qualification,
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_EMPLOYEE, map, generatedKeyHolder)
        employee.employeeId = generatedKeyHolder.keyList.first()["employee_id"] as Int?

        return employee
    }

    override fun updateEmployee(employee: Employee): Employee {

        val map = MapSqlParameterSource(
            mapOf(
                "employeeId" to employee.employeeId,
                "firstName" to employee.firstName,
                "lastName" to employee.lastName,
                "gender" to employee.gender.toString(),
                "birthDate" to employee.birthDate,
                "documentId" to employee.documentedId,
                "phone" to employee.phone,
                "email" to employee.email,
                "qualification" to employee.qualification,
            )
        )
        namedParameterJdbcTemplate.update(QUERY_UPDATE_EMPLOYEE, map)

        return employee
    }

    override fun deleteEmployee(id: Int): Int {
        TODO("Not yet implemented")
    }

    class EmployeeRowMapper : RowMapper<Employee> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Employee {

            return Employee(
                employeeId = getIntValue("employee_id", rs),
                firstName = getStringValue("first_name", rs)!!,
                lastName = getStringValue("last_name", rs)!!,
                gender = Gender.valueOf(getStringValue("gender", rs)!!),
                birthDate = getDateValue("birth_date", rs)!!,
                documentedId = getStringValue("document_id", rs)!!,
                phone = getStringValue("phone", rs)!!,
                email = getStringValue("email", rs)!!,
                qualification = getStringValue("qualification", rs)!!
            )

        }
    }

}