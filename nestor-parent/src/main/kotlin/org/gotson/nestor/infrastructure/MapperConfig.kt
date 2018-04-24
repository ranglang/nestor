package org.gotson.nestor.infrastructure

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy::class)
abstract class ScheduledEventMixIn

@Configuration
class MapperConfig {

    @Bean(name = ["mapper"])
    fun getMapper(): ObjectMapper =
            jacksonObjectMapper()
                    .registerModule(JavaTimeModule())
                    .registerModule(JodaModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    .addMixIn(ScheduledEvent::class.java, ScheduledEventMixIn::class.java)

    @Bean
    fun getGson(): Gson {
        val builder = GsonBuilder()
        com.fatboyindustrial.gsonjodatime.Converters.registerAll(builder)
        com.fatboyindustrial.gsonjavatime.Converters.registerAll(builder)
        return builder.create()
    }
}