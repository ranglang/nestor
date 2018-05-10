package org.gotson.nestor.infrastructure.messaging.console

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.domain.service.Destination
import org.gotson.nestor.infrastructure.messaging.MessagePublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Profile("nosns")
@Service
class ConsolePublisher @Autowired constructor(
        private val mapper: ObjectMapper
) : MessagePublisher {

    override fun send(bookingRequest: WishedClassDated, destination: Destination) {
        logger.info {
            "Booking ${mapper.writeValueAsString(bookingRequest)}\n" +
                    "Destination: $destination"
        }
    }
}
