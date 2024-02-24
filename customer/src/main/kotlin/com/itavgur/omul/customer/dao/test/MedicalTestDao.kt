package com.itavgur.omul.customer.dao.test

import com.itavgur.omul.customer.domain.MedicalTest
import com.itavgur.omul.customer.domain.MedicalTestResult

interface MedicalTestDao {

    fun getTestsByCustomerId(id: Int): List<MedicalTest>

    fun getTestById(testId: Int): MedicalTest?

    fun addTest(test: MedicalTest): MedicalTest

    fun addResult(testResult: MedicalTestResult): MedicalTestResult

}