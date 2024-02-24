package com.itavgur.omul.schedule.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties

@ConditionalOnBean(DatabasePostgresConfig::class)
@ConfigurationProperties(prefix = "db.postgres")
class DatabasePostgresProperties(

    val driverClassName: String = "org.postgresql.Driver",
    val url: String,
    val username: String,
    val password: String
)