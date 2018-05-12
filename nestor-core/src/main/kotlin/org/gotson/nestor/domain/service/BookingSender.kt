package org.gotson.nestor.domain.service

import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.infrastructure.messaging.MessagePublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookingSender @Autowired constructor(
    private val messagePublisher: MessagePublisher
) {

  fun sendForce(bookingRequest: WishedClassDated, destination: Destination) {
    messagePublisher.send(bookingRequest, destination)
  }

  fun send(bookingRequest: WishedClassDated) {
    val hasCalendars = bookingRequest.user.icalCalendars.isNotEmpty()
    messagePublisher.send(
        bookingRequest,
        if (hasCalendars)
          Destination.FILTER_CALENDAR
        else
          Destination.BOOKING)
  }
}

enum class Destination {
  BOOKING, FILTER_CALENDAR
}