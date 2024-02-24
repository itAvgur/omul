package com.itavgur.omul.personnel.dao.customer

import com.itavgur.omul.personnel.config.DatabaseMockConfig
import com.itavgur.omul.personnel.domain.Employee
import com.itavgur.omul.personnel.exception.DatabaseConstraintException
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicInteger

@Repository
@ConditionalOnBean(DatabaseMockConfig::class)
class EmployeeDaoMock : EmployeeDao {

    companion object {
        const val INITIAL_SQL_TABLE_SEQUENCE_VALUE = 0
    }

    private val sequenceCounter: AtomicInteger = AtomicInteger(INITIAL_SQL_TABLE_SEQUENCE_VALUE)

    private val sqlTable: MutableSet<Employee> = mutableSetOf()

    override fun getEmployeeById(id: Int): Employee? = sqlTable.firstOrNull { it.employeeId == id }

    override fun createEmployee(employee: Employee): Employee {

        checkConstraints(employee)

        val newEmployee = employee.clone()
        newEmployee.employeeId = sequenceCounter.getAndIncrement()
        sqlTable.add(newEmployee)
        return newEmployee
    }

    override fun updateEmployee(employee: Employee): Employee {

        sqlTable.firstOrNull { it.employeeId == employee.employeeId }
            ?.let {
                sqlTable.remove(it)
                sqlTable.add(employee)
            }

        return employee.clone()
    }

    override fun deleteEmployee(id: Int): Int {

        var deletedEntries = 0
        sqlTable
            .firstOrNull { it.employeeId == id }
            ?.let {
                sqlTable.remove(it)
                ++deletedEntries
            }
        return deletedEntries
    }

    @Throws(DatabaseConstraintException::class)
    private fun checkConstraints(employee: Employee) {
        sqlTable.firstOrNull { e -> employee.employeeId == e.employeeId || employee.email == e.email }
            ?.let {
                throw DatabaseConstraintException(
                    "employee with id ${employee.employeeId} or" +
                            "email ${employee.email} already exists"
                )
            }
    }

}