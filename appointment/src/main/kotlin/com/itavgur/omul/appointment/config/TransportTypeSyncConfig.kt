package com.itavgur.omul.appointment.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty("transport.type", havingValue = "sync", matchIfMissing = true)
class TransportTypeSyncConfig