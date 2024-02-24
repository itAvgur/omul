package com.itavgur.omul.customer.dao.customer

import com.itavgur.omul.customer.domain.Customer

interface CustomerDao {

    fun getCustomerById(customerId: Int): Customer?

    fun getCustomerEmail(email: String): Customer?

    fun createCustomer(customer: Customer): Customer

    fun updateCustomer(customer: Customer): Customer

    fun deleteCustomer(customerId: Int): Int

}