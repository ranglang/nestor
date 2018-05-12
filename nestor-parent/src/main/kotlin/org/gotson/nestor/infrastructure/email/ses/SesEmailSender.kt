package org.gotson.nestor.infrastructure.email.ses

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.model.Body
import com.amazonaws.services.simpleemail.model.Content
import com.amazonaws.services.simpleemail.model.Destination
import com.amazonaws.services.simpleemail.model.Message
import com.amazonaws.services.simpleemail.model.SendEmailRequest
import mu.KotlinLogging
import org.gotson.nestor.infrastructure.email.EmailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@ConditionalOnBean(AmazonSimpleEmailService::class)
@ConditionalOnProperty(name = ["amazon.ses.from"])
@Profile("!noemail")
@Service
class SesEmailSender @Autowired constructor(
    @Value("\${amazon.ses.from}")
    private val from: String,

    private val sesClient: AmazonSimpleEmailService

) : EmailSender {

  private val charset = "UTF-8"

  override fun sendEmail(subject: String, body: String, to: String) {

    sesClient.sendEmail(
        SendEmailRequest()
            .withDestination(Destination().withToAddresses(to))
            .withMessage(Message()
                .withSubject(Content()
                    .withCharset(charset)
                    .withData(subject))
                .withBody(Body()
                    .withText(Content()
                        .withCharset(charset)
                        .withData(body))))
            .withSource(from)
    )

    logger.info { "Email sent to: $to" }
  }
}