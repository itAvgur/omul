package com.itavgur.omul.customer.service

import com.itavgur.omul.customer.auth.JwtService
import com.itavgur.omul.customer.dao.test.MedicalTestDao
import com.itavgur.omul.customer.domain.MedicalTest
import com.itavgur.omul.customer.exception.CustomerNotFoundException
import com.itavgur.omul.customer.web.dto.MedicalTestRequest
import com.itavgur.omul.customer.web.dto.MedicalTestResponse
import com.itavgur.omul.customer.web.dto.MedicalTestResultRequest
import com.itavgur.omul.customer.web.dto.MedicalTestResultResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MedicalTestService(
    @Autowired private val customerService: CustomerService,
    @Autowired private val medicalTestDao: MedicalTestDao,
    @Autowired private val jwtService: JwtService
) {

    fun createTest(request: MedicalTestRequest): MedicalTestResponse {
        if (customerService.checkCustomer(request.customerId)) {
            val dateTime: LocalDateTime = request.date ?: LocalDateTime.now()

            val newTest = medicalTestDao.addTest(request.toMedicalTest(dateTime))
            return MedicalTestResponse.from(newTest)
        }

        throw CustomerNotFoundException("customer with id ${request.customerId} is absent",)
    }

    fun checkMedicalTest(testId: Int): Boolean {
        medicalTestDao.getTestById(testId)?.let {
            return true
        }
        return false
    }

    fun getMedicalTests(customerId: Int): List<MedicalTestResponse> {
        jwtService.validateIdWithJwt(customerId)

        if (customerService.checkCustomer(customerId)) {

            return medicalTestDao.getTestsByCustomerId(customerId)
                .map { MedicalTestResponse.from(it) }
                .toList()
        }
        throw CustomerNotFoundException("customer with id $customerId is absent",)
    }

    fun getMedicalTest(testId: Int): MedicalTest {

        medicalTestDao.getTestById(testId)?.let {
            jwtService.validateIdWithJwt(it.customerId)

            return it
        }
        throw CustomerNotFoundException("medical test with id $testId is absent",)
    }

    fun addResultToMedicalTest(request: MedicalTestResultRequest): MedicalTestResultResponse {

        val customerId = getMedicalTest(request.testId).customerId
        jwtService.validateIdWithJwt(customerId)

        if (customerService.checkCustomer(customerId)
            && checkMedicalTest(request.testId)
        ) {
            val dateTime: LocalDateTime = request.date ?: LocalDateTime.now()

            val newProcedure = medicalTestDao.addResult(request.toMedicalTestResult(dateTime))
            return MedicalTestResultResponse.from(newProcedure)
        }

        throw CustomerNotFoundException("customer with id $customerId is absent",)
    }

}