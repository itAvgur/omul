package com.itavgur.omul.customer.util

import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

/**
 * https://stackoverflow.com/questions/34416869/idiomatic-way-of-logging-in-kotlin
 */

fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return ofClass.enclosingClass?.takeIf {
        ofClass.enclosingClass.kotlin.companionObject?.java == ofClass
    } ?: ofClass
}

fun <R : Any> R.logger(): Lazy<org.slf4j.Logger> {
    return lazy { LoggerFactory.getLogger(unwrapCompanionClass(this.javaClass).name) }
}