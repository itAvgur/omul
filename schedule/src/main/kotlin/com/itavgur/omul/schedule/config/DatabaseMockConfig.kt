package com.itavgur.omul.schedule.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty("db.type", havingValue = "mock", matchIfMissing = false)
class DatabaseMockConfig