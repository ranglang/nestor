package org.gotson.nestor.infrastructure.messaging

import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.domain.service.Destination

interface MessagePublisher {
    fun send(bookingRequest: WishedClassDated, destination: Destination)
}