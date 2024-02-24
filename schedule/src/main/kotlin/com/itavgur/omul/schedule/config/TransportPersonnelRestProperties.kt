package com.itavgur.omul.schedule.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties

@ConditionalOnBean(TransportPersonnelRestConfig::class)
@ConfigurationProperties(prefix = "transport.personnel.rest")
class TransportPersonnelRestProperties(
    val url: String
)