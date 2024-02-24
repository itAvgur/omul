package com.itavgur.omul.customer.dao.customer

import com.itavgur.omul.customer.config.MockDBConfig
import com.itavgur.omul.customer.domain.Customer
import com.itavgur.omul.customer.exception.DatabaseConstraintException
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicInteger

@Repository
@ConditionalOnBean(MockDBConfig::class)
class CustomerDaoMock : CustomerDao {

    companion object {
        const val INITIAL_SQL_TABLE_SEQUENCE_VALUE = 0
    }

    private val sequenceCounter: AtomicInteger = AtomicInteger(INITIAL_SQL_TABLE_SEQUENCE_VALUE)

    private val sqlTable: MutableSet<Customer> = mutableSetOf()

    override fun getCustomerById(customerId: Int): Customer? = sqlTable.firstOrNull { it.customerId == customerId }

    override fun getCustomerEmail(email: String): Customer? = sqlTable.firstOrNull { it.email == email }

    override fun createCustomer(customer: Customer): Customer {

        checkConstraints(customer)

        val newCustomer = customer.clone()
        newCustomer.customerId = sequenceCounter.getAndIncrement()
        sqlTable.add(newCustomer)
        return newCustomer
    }

    override fun updateCustomer(customer: Customer): Customer {

        sqlTable.firstOrNull { it.customerId == customer.customerId }
            ?.let {
                sqlTable.remove(it)
                sqlTable.add(customer)
            }

        return customer.clone()
    }

    override fun deleteCustomer(customerId: Int): Int {

        var deletedEntries = 0
        sqlTable
            .firstOrNull { it.customerId == customerId }
            ?.let {
                sqlTable.remove(it)
                ++deletedEntries
            }
        return deletedEntries
    }

    @Throws(DatabaseConstraintException::class)
    private fun checkConstraints(customer: Customer) {
        sqlTable.firstOrNull { e -> customer.customerId == e.customerId || customer.email == e.email }
            ?.let {
                throw DatabaseConstraintException(
                    "customer with id ${customer.customerId} or" +
                            "email ${customer.email} already exists"
                )
            }
    }

}