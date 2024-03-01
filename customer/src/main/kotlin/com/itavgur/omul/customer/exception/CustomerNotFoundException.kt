package com.itavgur.omul.customer.exception

import org.slf4j.event.Level

class CustomerNotFoundException(message: String, val logLevel: Level = Level.ERROR) : RuntimeException(message)