package org.gotson.nestor.infrastructure.messaging.sns

import com.amazonaws.services.sns.AmazonSNSClient
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.domain.service.Destination
import org.gotson.nestor.infrastructure.messaging.MessagePublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@ConditionalOnProperty(name = ["amazon.sns.enabled"])
@Profile("!nosns")
@Service
class SnsMessagePublisher @Autowired constructor(
        private val amazonSnsClient: AmazonSNSClient,
        private val mapper: ObjectMapper,
        private val snsConfigProps: SnsConfigurationProperties

) : MessagePublisher {

    private fun send(message: String, topicArn: String) {
        val result = amazonSnsClient.publish(topicArn, message)
        logger.info { "Message sent on SNS, messageId: ${result.messageId}" }
    }

    override fun send(bookingRequest: WishedClassDated, destination: Destination) {
        val topicArn = snsConfigProps.topics.find { it.destination == destination }?.arn
                ?: throw Exception("No topicArn found for destination: $destination")
        send(mapper.writeValueAsString(bookingRequest), topicArn)
    }
}
