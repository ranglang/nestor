package org.gotson.nestor.interfaces.lambda

import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import org.gotson.nestor.domain.service.BookingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function

private val logger = KotlinLogging.logger {}

@Configuration
class Functions @Autowired constructor(
        private val bookingService: BookingService,
        private val mapper: ObjectMapper
) {

    @Bean
    fun bookPure(): Function<SNSEvent, String> =
            Function {
                val responses = mutableListOf<String>()
                it.records.forEach {
                    responses += bookingService.bookPureYoga(mapper.readValue(it.sns.message))
                }
                responses.toString()
            }
}