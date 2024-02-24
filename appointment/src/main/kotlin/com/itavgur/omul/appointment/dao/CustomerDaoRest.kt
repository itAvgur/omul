package com.itavgur.omul.appointment.dao

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.itavgur.omul.appointment.config.TransportCustomerRestConfig
import com.itavgur.omul.appointment.exception.ExternalDaoException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestClient

@Primary
@Repository
@ConditionalOnBean(TransportCustomerRestConfig::class)
class CustomerDaoRest(
    @Qualifier("customerRestClient") @Autowired var restClient: RestClient
) : CustomerDao {

    companion object {
        const val GET_CUSTOMER_BY_ID = "/v1/customer?customerId"
        const val CREATE_TEMPORARY_CUSTOMER = "/v1/customer/newby"
    }

    override fun checkCustomerExist(customerId: Int): Boolean {

        var result = true
        restClient
            .get()
            .uri("$GET_CUSTOMER_BY_ID=$customerId")
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError) { _, _ -> result = false }
            .body(String::class.java)

        return result
    }

    override fun createTemporaryUser(phone: String?, email: String?): Int? {
        val response = restClient
            .post()
            .uri(CREATE_TEMPORARY_CUSTOMER)
            .contentType(MediaType.APPLICATION_JSON)
            .body(CreateTemporaryUserRequest(phone, email))
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ -> throw ExternalDaoException("couldn't create temporary user") }
            .body(CreateTemporaryUserResponse::class.java)

        response ?: return null
        return response.customerId
    }

    private class CreateTemporaryUserRequest(
        @JsonInclude(NON_NULL)
        val phone: String? = null,
        @JsonInclude(NON_NULL)
        val email: String? = null
    )

    private class CreateTemporaryUserResponse(
        val customerId: Int,
        @JsonInclude(NON_NULL)
        val phone: String? = null,
        @JsonInclude(NON_NULL)
        val email: String? = null
    )

}