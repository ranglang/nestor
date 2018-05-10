package org.gotson.nestor.domain.service

import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.infrastructure.messaging.MessagePublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookingSender @Autowired constructor(
        private val messagePublisher: MessagePublisher
) {
    fun send(bookingRequest: WishedClassDated) {
        messagePublisher.send(bookingRequest, Destination.BOOKING)
    }
}

enum class Destination {
    BOOKING, FILTER_CALENDAR
}