package org.gotson.nestor.interfaces.lambda.sns

import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.domain.service.BookingSender
import org.gotson.nestor.domain.service.BusyTimeFilterer
import org.gotson.nestor.domain.service.Destination
import org.gotson.nestor.domain.service.NotificationSender
import org.gotson.nestor.infrastructure.email.EmailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function

private val logger = KotlinLogging.logger {}

@ConditionalOnBean(EmailSender::class)
@ConditionalOnProperty(name = ["amazon.sns.enabled"])
@Configuration
class SnsFunctions @Autowired constructor(
    private val bookingSender: BookingSender,
    private val busyTimeFilterer: BusyTimeFilterer,
    private val mapper: ObjectMapper,
    private val notificationSender: NotificationSender
) {

  @Bean
  fun checkCalendar(): Function<SNSEvent, String> =
      Function {
        val responses = mutableListOf<String>()
        it.records.forEach {
          val bookingRequest = mapper.readValue<WishedClassDated>(it.sns.message)

          val filterResult = busyTimeFilterer.filterRequest(bookingRequest)
          if (filterResult.filtered) {
            val msg = "Booking is conflicting, blocking message." +
                "Reason: '${filterResult.busyTime?.summary}' " +
                "from ${filterResult.busyTime?.startDate} " +
                "to ${filterResult.busyTime?.endDate}"

            logger.info(msg)
            responses += msg
            notificationSender.notifyBookingError(filterResult, bookingRequest)
          } else {
            val msg = "Booking is valid, sending message"

            logger.info(msg)
            responses += msg
            bookingSender.sendForce(bookingRequest, Destination.BOOKING)
          }
        }
        responses.toString()
      }

}