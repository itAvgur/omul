package com.itavgur.omul.appointment.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient

@Configuration
@EnableConfigurationProperties(TransportCustomerRestProperties::class)
@ConditionalOnProperty("transport.customer.type", havingValue = "rest", matchIfMissing = true)
class TransportCustomerRestConfig(
    val props: TransportCustomerRestProperties
) {

    @Bean("customerRestClient")
    fun customerRestClient(): RestClient {
        return RestClient.builder()
            .requestFactory(HttpComponentsClientHttpRequestFactory())
            .baseUrl(props.url)
            .build()

    }

}