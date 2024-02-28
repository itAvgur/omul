package com.itavgur.omul.customer.service

import com.itavgur.omul.customer.auth.JwtService
import com.itavgur.omul.customer.dao.procedure.MedicalProcedureDao
import com.itavgur.omul.customer.exception.CustomerNotFoundException
import com.itavgur.omul.customer.web.dto.CustomerProcedureRequest
import com.itavgur.omul.customer.web.dto.CustomerProcedureResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MedicalProcedureService(
    @Autowired private val customerService: CustomerService,
    @Autowired private val medicalProcedureDao: MedicalProcedureDao,
    @Autowired private val jwtService: JwtService
) {

    fun createProcedure(request: CustomerProcedureRequest): CustomerProcedureResponse {
        jwtService.validateIdWithJwt(request.customerId)

        if (customerService.checkCustomer(request.customerId)) {
            val dateTime: LocalDateTime = request.date ?: LocalDateTime.now()

            val newProcedure = medicalProcedureDao.addProcedure(request.toMedicalProcedure(dateTime))
            return CustomerProcedureResponse.from(newProcedure)
        }

        throw CustomerNotFoundException("customer with id ${request.customerId} is absent",)
    }

    fun getProcedures(customerId: Int): List<CustomerProcedureResponse> {
        jwtService.validateIdWithJwt(customerId)

        if (customerService.checkCustomer(customerId)) {

            return medicalProcedureDao.getProceduresByCustomerId(customerId)
                .map { CustomerProcedureResponse.from(it) }
                .toList()
        }
        throw CustomerNotFoundException("customer with id $customerId is absent",)
    }

}