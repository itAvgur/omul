package com.itavgur.omul.personnel.service

import com.itavgur.omul.personnel.auth.JwtService
import com.itavgur.omul.personnel.dao.customer.EmployeeDao
import com.itavgur.omul.personnel.exception.EmployeeNotFoundException
import com.itavgur.omul.personnel.web.dto.EmployeeDataRequest
import com.itavgur.omul.personnel.web.dto.EmployeeDataResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmployeeService(
    @Autowired private val employeeDao: EmployeeDao,
    @Autowired private val jwtService: JwtService
) {
    fun getEmployee(id: Int): EmployeeDataResponse {
        jwtService.validateIdWithJwt(id)

        employeeDao.getEmployeeById(id)
            ?.let { return EmployeeDataResponse.from(it) }
        throw EmployeeNotFoundException("employee with id $id is absent")
    }

    fun saveEmployee(request: EmployeeDataRequest): EmployeeDataResponse {
        request.employeeId?.let {
            employeeDao.getEmployeeById(request.employeeId)
                ?.let {

                    jwtService.validateIdWithJwt(request.employeeId)

                    return EmployeeDataResponse.from(
                        employeeDao.updateEmployee(employee = request.toEmployee())
                    )
                }
            throw EmployeeNotFoundException("employee with id ${request.employeeId} is absent")
        }

        val newEmployee = employeeDao.createEmployee(employee = request.toEmployee())
        return EmployeeDataResponse.from(newEmployee)
    }

    fun deleteEmployee(id: Int): Any {
        jwtService.validateIdWithJwt(id)

        employeeDao.getEmployeeById(id)
            ?.let {
                return employeeDao.deleteEmployee(id)
            }
        throw EmployeeNotFoundException("employee with id $id missed")
    }

}