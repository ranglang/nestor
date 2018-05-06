package org.gotson.nestor.infrastructure.messaging

import com.amazonaws.services.sns.AmazonSNSClient
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.gotson.nestor.domain.model.WishedClassDated
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Profile("!nosns")
@Service
class SnsMessagePublisher @Autowired constructor(
        @Value("\${amazon.sns.topic}")
        private val topicArn: String,
        private val amazonSnsClient: AmazonSNSClient,
        private val mapper: ObjectMapper
) : MessagePublisher {
    private fun send(message: String) {
        val result = amazonSnsClient.publish(topicArn, message)
        logger.info { "Message sent on SNS, messageId: ${result.messageId}" }
    }

    override fun send(bookingRequest: WishedClassDated) {
        send(mapper.writeValueAsString(bookingRequest))
    }
}
