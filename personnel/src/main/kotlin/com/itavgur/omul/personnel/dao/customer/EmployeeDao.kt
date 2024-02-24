package com.itavgur.omul.personnel.dao.customer

import com.itavgur.omul.personnel.domain.Employee

interface EmployeeDao {

    fun getEmployeeById(id: Int): Employee?

    fun createEmployee(employee: Employee): Employee

    fun updateEmployee(employee: Employee): Employee

    fun deleteEmployee(id: Int): Int
}