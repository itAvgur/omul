package com.itavgur.omul.schedule.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient

@Configuration
@EnableConfigurationProperties(TransportPersonnelRestProperties::class)
@ConditionalOnProperty("transport.personnel.type", havingValue = "rest", matchIfMissing = true)
class TransportPersonnelRestConfig(
    val props: TransportPersonnelRestProperties
) {

    @Bean
    fun personnelRestClient(): RestClient {
        return RestClient.builder()
            .requestFactory(HttpComponentsClientHttpRequestFactory())
            .baseUrl(props.url)
            .build()
    }

}