package com.itavgur.omul.appointment.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties

@ConditionalOnBean(TransportCustomerRestConfig::class)
@ConfigurationProperties(prefix = "transport.customer.rest")
class TransportCustomerRestProperties(
    val url: String
)