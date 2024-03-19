package com.itavgur.omul.schedule.util

import org.modelmapper.ModelMapper
import org.modelmapper.config.Configuration
import java.util.stream.Collectors

class ModelMapperUtils private constructor() {

    companion object {
        private val MAPPER: ModelMapper = ModelMapper()

        internal inline fun <reified T> mapList(source: List<Any>): List<T> {
            return source
                .stream()
                .map { element -> map<T>(element) }
                .collect(Collectors.toList<T>())
        }

        internal inline fun <reified T> map(source: Any): T {

            MAPPER.configuration.setFieldMatchingEnabled(true).setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)

            return MAPPER.map(source, T::class.java)
        }

    }

}