package org.gotson.nestor.interfaces.lambda.scheduler

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import mu.KotlinLogging
import org.gotson.nestor.domain.service.PureBooker
import org.gotson.nestor.infrastructure.messaging.MessagePublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function

private val logger = KotlinLogging.logger {}

@Configuration
class SchedulerFunctions @Autowired constructor(
        private val pureBooker: PureBooker,
        private val messagePublisher: MessagePublisher
) {

    @Bean
    fun dailyCron(): Function<ScheduledEvent, String> =
            Function {
                val requests = pureBooker.findMatchingWishedClasses()
                requests.forEach { messagePublisher.send(it) }
                val msg = "Sent ${requests.size} event(s)"
                logger.info { msg }
                msg
            }

}

