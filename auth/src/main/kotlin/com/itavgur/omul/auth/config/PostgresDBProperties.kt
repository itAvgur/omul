package com.itavgur.omul.auth.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties

@ConditionalOnBean(DatabasePostgresConfig::class)
@ConfigurationProperties(prefix = "db.postgres")
class PostgresDBProperties(
    val driverClassName: String = "org.postgresql.Driver",
    val url: String,
    val username: String,
    val password: String
)