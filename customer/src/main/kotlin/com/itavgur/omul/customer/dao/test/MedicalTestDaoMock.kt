package com.itavgur.omul.customer.dao.test

import com.itavgur.omul.customer.config.MockDBConfig
import com.itavgur.omul.customer.domain.MedicalTest
import com.itavgur.omul.customer.domain.MedicalTestResult
import com.itavgur.omul.customer.exception.DatabaseConstraintException
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicInteger

@Repository
@ConditionalOnBean(MockDBConfig::class)
class MedicalTestDaoMock : MedicalTestDao {

    companion object {
        const val INITIAL_SQL_TABLE_SEQUENCE_VALUE = 0
    }

    private val sequenceCounter: AtomicInteger = AtomicInteger(INITIAL_SQL_TABLE_SEQUENCE_VALUE)
    private val sequenceCounterTestResults: AtomicInteger = AtomicInteger(INITIAL_SQL_TABLE_SEQUENCE_VALUE)

    private val sqlTable: MutableSet<MedicalTest> = mutableSetOf()
    private val sqlTableTestResults: MutableSet<MedicalTestResult> = mutableSetOf()
    override fun getTestsByCustomerId(id: Int): List<MedicalTest> {
        val tests = sqlTable.filter { it.customerId == id }.toList()
        for (test in tests) {
            enrichWithResults(test)
        }
        return tests
    }

    override fun getTestById(testId: Int): MedicalTest? =
        sqlTable.firstOrNull {
            it.testId == testId
        }

    private fun enrichWithResults(test: MedicalTest) {

        test.results = sqlTableTestResults.filter { it.testId == test.testId }.toList()
    }

    override fun addTest(test: MedicalTest): MedicalTest {

        val newTest = test.clone()
        newTest.testId = sequenceCounter.getAndIncrement()
        checkConstraints(newTest)

        sqlTable.add(newTest)
        return newTest
    }

    override fun addResult(testResult: MedicalTestResult): MedicalTestResult {

        val newTestResult = testResult.clone()
        newTestResult.testResultId = sequenceCounterTestResults.getAndIncrement()
        checkConstraints(newTestResult)

        sqlTableTestResults.add(newTestResult)
        return newTestResult
    }

    @Throws(DatabaseConstraintException::class)
    private fun checkConstraints(test: MedicalTest) {
        sqlTable.firstOrNull { e -> test.testId == e.testId }
            ?.let { throw DatabaseConstraintException("medical test with id ${test.testId} already exists") }
    }

    @Throws(DatabaseConstraintException::class)
    private fun checkConstraints(testResult: MedicalTestResult) {
        sqlTableTestResults.firstOrNull { e -> testResult.testResultId == e.testResultId }
            ?.let { throw DatabaseConstraintException("medical test result with id ${testResult.testId} already exists") }
    }
}