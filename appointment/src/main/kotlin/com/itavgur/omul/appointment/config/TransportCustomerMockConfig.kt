package com.itavgur.omul.appointment.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty("transport.customer.type", havingValue = "mock", matchIfMissing = false)
class TransportCustomerMockConfig