package com.itavgur.omul.customer.dao.customer

import com.itavgur.omul.customer.config.PostgresDBConfig
import com.itavgur.omul.customer.domain.Customer
import com.itavgur.omul.customer.domain.Gender
import com.itavgur.omul.customer.util.DBUtil.Companion.getDateValue
import com.itavgur.omul.customer.util.DBUtil.Companion.getIntValue
import com.itavgur.omul.customer.util.DBUtil.Companion.getStringValue
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
class CustomerDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : CustomerDao {

    companion object {

        const val QUERY_GET_CUSTOMER_BY_ID =
            """SELECT customers.customer_id ,customers.first_name, customers.last_name, customers.gender, 
                customers.birth_date, customers.document_id, customers.email, customers.phone
                FROM customers
                WHERE customers.customer_id = :customerId"""

        const val QUERY_GET_CUSTOMER_BY_EMAIL =
            """SELECT customers.customer_id ,customers.first_name, customers.last_name, customers.gender, 
                customers.birth_date, customers.document_id, customers.email, customers.phone
                FROM customers
                WHERE customers.email = :email"""

        const val QUERY_INSERT_CUSTOMER =
            """INSERT INTO customers (first_name, last_name, birth_date, gender, document_id, email, phone)
                VALUES (:firstName, :lastName, :birthDate, :gender, :documentId, :email, :phone)"""

        const val QUERY_UPDATE_CUSTOMER =
            """UPDATE customers
                SET first_name = :firstName, last_name = :lastName, birth_date = :birthDate, gender = :gender,
                document_id = :documentId, email = :email, phone = :phone
                WHERE customers.customer_id = :customerId"""
    }

    override fun getCustomerById(customerId: Int): Customer? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_GET_CUSTOMER_BY_ID,
                MapSqlParameterSource("customerId", customerId),
                CustomerRowMapper()
            )
        return result.firstOrNull()
    }

    override fun getCustomerEmail(email: String): Customer? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_GET_CUSTOMER_BY_EMAIL,
                MapSqlParameterSource("email", email),
                CustomerRowMapper()
            )
        return result.firstOrNull()
    }

    override fun createCustomer(customer: Customer): Customer {

        val map = MapSqlParameterSource(
            mapOf(
                "firstName" to customer.firstName,
                "lastName" to customer.lastName,
                "birthDate" to customer.birthDate,
                "gender" to takeGender(customer.gender),
                "documentId" to customer.documentedId,
                "email" to customer.email,
                "phone" to customer.phone
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_CUSTOMER, map, generatedKeyHolder)
        customer.customerId = generatedKeyHolder.keyList.first()["customer_id"] as Int?

        return customer
    }

    override fun updateCustomer(customer: Customer): Customer {

        val map = MapSqlParameterSource(
            mapOf(
                "customerId" to customer.customerId,
                "firstName" to customer.firstName,
                "lastName" to customer.lastName,
                "birthDate" to customer.birthDate,
                "gender" to customer.gender.toString(),
                "documentId" to customer.documentedId,
                "email" to customer.email,
                "phone" to customer.phone
            )
        )
        namedParameterJdbcTemplate.update(QUERY_UPDATE_CUSTOMER, map)
        return customer
    }

    override fun deleteCustomer(customerId: Int): Int {
        TODO("Not yet implemented")
    }

    fun takeGender(gender: Gender?): String? {
        gender ?: return null
        return gender.toString()
    }

    private class CustomerRowMapper : RowMapper<Customer> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Customer {

            return Customer(
                customerId = getIntValue("customer_id", rs),
                firstName = getStringValue("first_name", rs),
                lastName = getStringValue("last_name", rs),
                birthDate = getDateValue("birth_date", rs),
                gender = takeGender(rs),
                documentedId = getStringValue("document_id", rs),
                phone = getStringValue("phone", rs),
                email = getStringValue("email", rs)!!
            )
        }

        fun takeGender(rs: ResultSet): Gender? {
            val value = getStringValue("gender", rs)
            value ?: return null
            return Gender.valueOf(value)
        }

    }
}