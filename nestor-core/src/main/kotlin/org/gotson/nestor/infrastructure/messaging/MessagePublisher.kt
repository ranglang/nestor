package org.gotson.nestor.infrastructure.messaging

import org.gotson.nestor.domain.model.WishedClassDated

interface MessagePublisher {
    fun send(bookingRequest: WishedClassDated)
}