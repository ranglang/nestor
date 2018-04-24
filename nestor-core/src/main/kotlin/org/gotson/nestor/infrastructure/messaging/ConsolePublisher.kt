package org.gotson.nestor.infrastructure.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.gotson.nestor.domain.model.WishedClassDated
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Profile("nosns")
@Service
class ConsolePublisher @Autowired constructor(
        private val mapper: ObjectMapper
) : MessagePublisher {

    override fun send(bookingRequest: WishedClassDated) {
        logger.info { mapper.writeValueAsString(bookingRequest) }
    }
}
