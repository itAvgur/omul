package com.itavgur.omul.auth.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty("transport.type", havingValue = "mock", matchIfMissing = false)
class TransportMockConfig