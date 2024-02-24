package com.itavgur.omul.appointment.dao

import com.itavgur.omul.appointment.config.TransportCustomerMockConfig
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnBean(TransportCustomerMockConfig::class)
class CustomerDaoMock : CustomerDao {

    override fun checkCustomerExist(customerId: Int): Boolean {
        return customerId % 2 == 0
    }

    override fun createTemporaryUser(phone: String?, email: String?): Int? {
        return 1
    }
}