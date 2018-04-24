package org.gotson.nestor.domain.service

import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.domain.model.dated
import org.gotson.nestor.infrastructure.persistence.PersistenceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PureBooker @Autowired constructor(
        private val persistenceService: PersistenceService,

        @Value("\${nestor.pure.advanceBookingDays:#{2}}")
        private val advanceBookingDays: Long
) {

    fun findMatchingWishedClasses(
            date: LocalDate = LocalDate.now().plusDays(advanceBookingDays)
    ): List<WishedClassDated> {
        val classes = persistenceService.findWishedClassByDay(date.dayOfWeek)
        return classes.map { it.dated(date) }
    }

}