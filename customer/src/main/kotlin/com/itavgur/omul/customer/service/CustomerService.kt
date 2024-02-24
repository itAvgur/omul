package com.itavgur.omul.customer.service

import com.itavgur.omul.customer.auth.JwtService
import com.itavgur.omul.customer.dao.customer.CustomerDao
import com.itavgur.omul.customer.exception.CredentialException
import com.itavgur.omul.customer.exception.CustomerNotFoundException
import com.itavgur.omul.customer.web.dto.CustomerDataRequest
import com.itavgur.omul.customer.web.dto.CustomerDataResponse
import com.itavgur.omul.customer.web.dto.CustomerTemporaryDataRequest
import com.itavgur.omul.customer.web.dto.CustomerTemporaryDataResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerService(
    @Autowired private val customerDao: CustomerDao,
    @Autowired private val jwtService: JwtService
) {

    fun checkCustomer(id: Int): Boolean {
        customerDao.getCustomerById(id)
            ?.let { return true }
        return false
    }

    fun getCustomer(id: Int): CustomerDataResponse {
        jwtService.validateIdWithJwt(id)

        customerDao.getCustomerById(id)
            ?.let { return CustomerDataResponse.from(it) }
        throw CustomerNotFoundException("customer with id $id is absent")
    }

    fun createTemporaryCustomer(request: CustomerTemporaryDataRequest): CustomerTemporaryDataResponse {
        if (customerDao.getCustomerEmail(request.email) != null) {
            throw CredentialException("credential for email ${request.email} already exists")
        }

        val newCustomer = saveCustomer(CustomerDataRequest.from(request))
        return CustomerTemporaryDataResponse.from(newCustomer)
    }

    fun createCustomer(request: CustomerDataRequest): CustomerDataResponse {

        if (customerDao.getCustomerEmail(request.email) != null) {
            throw CredentialException("credential for email ${request.email} already exists")
        }
        return saveCustomer(request)
    }


    fun saveCustomer(request: CustomerDataRequest): CustomerDataResponse {
        request.customerId?.let {
            customerDao.getCustomerById(request.customerId)
                ?.let {
                    jwtService.validateIdWithJwt(request.customerId)

                    return CustomerDataResponse.from(
                        customerDao.updateCustomer(customer = request.toCustomer())
                    )
                }
            throw CustomerNotFoundException("customer with id ${request.customerId} is absent")
        }

        val newCustomer = customerDao.createCustomer(customer = request.toCustomer())
        return CustomerDataResponse.from(newCustomer)
    }

    fun deleteCustomer(id: Int): Any {
        jwtService.validateIdWithJwt(id)

        customerDao.getCustomerById(id)
            ?.let {
                return customerDao.deleteCustomer(id)
            }
        throw CustomerNotFoundException("customer with id $id missed")
    }

}