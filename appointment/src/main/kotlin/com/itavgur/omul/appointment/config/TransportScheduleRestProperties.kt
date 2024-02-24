package com.itavgur.omul.appointment.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties

@ConditionalOnBean(TransportScheduleRestConfig::class)
@ConfigurationProperties(prefix = "transport.schedule.rest")
class TransportScheduleRestProperties(
    val url: String
)