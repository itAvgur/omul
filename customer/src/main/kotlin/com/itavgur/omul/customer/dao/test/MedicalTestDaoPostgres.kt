package com.itavgur.omul.customer.dao.test

import com.itavgur.omul.customer.config.PostgresDBConfig
import com.itavgur.omul.customer.domain.MedicalTest
import com.itavgur.omul.customer.domain.MedicalTestResult
import com.itavgur.omul.customer.util.DBUtil
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Primary
@Repository
@ConditionalOnBean(PostgresDBConfig::class)
class MedicalTestDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : MedicalTestDao {

    companion object {

        const val QUERY_GET_MEDICAL_TEST_BY_CUSTOMER_ID =
            """SELECT tests.test_id, tests.customer_id ,tests.summary, tests.description, tests.date
                FROM medical_tests tests
                WHERE tests.customer_id = :customerId"""

        const val QUERY_GET_MEDICAL_TEST_BY_ID =
            """SELECT tests.test_id, tests.customer_id ,tests.summary, tests.description, tests.date
                FROM medical_tests tests
                WHERE tests.test_id = :testId"""

        const val QUERY_INSERT_MEDICAL_TEST =
            """INSERT INTO medical_tests (customer_id, summary, description, date)
                VALUES (:customerId, :summary, :description, :date)"""

        const val QUERY_INSERT_MEDICAL_TEST_RESULT =
            """INSERT INTO medical_test_results (test_id, result, date)
                VALUES (:testId, :result, :date)"""

        const val QUERY_GET_MEDICAL_TEST_RESULTS_FOR_SPECIFIC_TEST =
            """SELECT results.test_result_id, results.test_id ,results.result, results.date
                FROM medical_test_results results
                WHERE results.test_id = :testId"""
    }

    override fun getTestsByCustomerId(id: Int): List<MedicalTest> {
        val testsFound: List<MedicalTest> = namedParameterJdbcTemplate.query(
            QUERY_GET_MEDICAL_TEST_BY_CUSTOMER_ID,
            MapSqlParameterSource("customerId", id),
            MedicalTestRowMapper()
        )

        //todo N+1, remove later
        testsFound.forEach {
            it.results = getTestResults(it.testId!!)
        }

        return testsFound
    }

    override fun getTestById(testId: Int): MedicalTest? {
        val testFound = namedParameterJdbcTemplate.query(
            QUERY_GET_MEDICAL_TEST_BY_ID,
            MapSqlParameterSource("testId", testId),
            MedicalTestRowMapper()
        ).firstOrNull()

        testFound?.let {
            it.results = getTestResults(it.testId!!)
        }

        return testFound
    }

    override fun addTest(test: MedicalTest): MedicalTest {
        val map = MapSqlParameterSource(
            mapOf(
                "customerId" to test.customerId,
                "summary" to test.summary,
                "description" to test.description,
                "date" to test.date
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_MEDICAL_TEST, map, generatedKeyHolder)
        test.testId = generatedKeyHolder.keyList.first()["test_id"] as Int?

        return test
    }

    override fun addResult(testResult: MedicalTestResult): MedicalTestResult {
        val map = MapSqlParameterSource(
            mapOf(
                "testId" to testResult.testId,
                "result" to testResult.result,
                "date" to testResult.date
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_MEDICAL_TEST_RESULT, map, generatedKeyHolder)
        testResult.testResultId = generatedKeyHolder.keyList.first()["test_result_id"] as Int?

        return testResult
    }

    private fun getTestResults(testId: Int): List<MedicalTestResult> {

        return namedParameterJdbcTemplate.query(
            QUERY_GET_MEDICAL_TEST_RESULTS_FOR_SPECIFIC_TEST,
            MapSqlParameterSource("testId", testId),
            MedicalTestResultRowMapper()
        )
    }

    private class MedicalTestRowMapper : RowMapper<MedicalTest> {
        override fun mapRow(rs: ResultSet, rowNum: Int): MedicalTest {

            return MedicalTest(
                testId = DBUtil.getIntValue("test_id", rs),
                customerId = DBUtil.getIntValue("customer_id", rs)!!,
                summary = DBUtil.getStringValue("summary", rs)!!,
                description = DBUtil.getStringValue("description", rs)!!,
                date = DBUtil.getDateTimeValue("date", rs),
                results = mutableListOf()
            )
        }
    }

    private class MedicalTestResultRowMapper : RowMapper<MedicalTestResult> {
        override fun mapRow(rs: ResultSet, rowNum: Int): MedicalTestResult {

            return MedicalTestResult(
                testResultId = DBUtil.getIntValue("test_result_id", rs),
                testId = DBUtil.getIntValue("test_id", rs)!!,
                result = DBUtil.getStringValue("result", rs)!!,
                date = DBUtil.getDateTimeValue("date", rs)
            )
        }
    }
}