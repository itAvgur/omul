package com.itavgur.omul.schedule

import org.mockito.Mockito

class BaseTest {

    //https://stackoverflow.com/questions/59230041/argumentmatchers-any-must-not-be-null
    object MockitoHelper {
        fun <T> anyObject(): T {
            Mockito.any<T>()
            return uninitialized()
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T> uninitialized(): T = null as T
    }

}