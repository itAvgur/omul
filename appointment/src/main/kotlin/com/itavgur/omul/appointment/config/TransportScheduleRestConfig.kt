package com.itavgur.omul.appointment.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient

@Configuration
@EnableConfigurationProperties(TransportScheduleRestProperties::class)
@ConditionalOnProperty("transport.schedule.type", havingValue = "rest", matchIfMissing = true)
class TransportScheduleRestConfig(
    val props: TransportScheduleRestProperties
) {

    @Bean("scheduleRestClient")
    fun scheduleRestClient(): RestClient {
        return RestClient.builder()
            .requestFactory(HttpComponentsClientHttpRequestFactory())
            .baseUrl(props.url)
            .build()
    }

}