package com.itavgur.omul.customer

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@OpenAPIDefinition(
    info = Info(
        title = "Dental clinic, customer service", description = "Web-application for dental clinic",
        contact = Contact(name = "itAvgur", email = "itavgur@gmail.com"),
        version = "0.0.1"
    )
)
@SpringBootApplication(
    exclude = [DataSourceAutoConfiguration::class, DataSourceTransactionManagerAutoConfiguration::class,
        SecurityAutoConfiguration::class]
)
class CustomerApplication

fun main(args: Array<String>) {
    runApplication<CustomerApplication>(*args)
}