package org.gotson.nestor.domain.persistence

import org.gotson.nestor.domain.model.RecurringWishedClass
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.DayOfWeek

@Repository
interface RecurringWishedClassRepository : CrudRepository<RecurringWishedClass, Long> {
  fun findByDay(dayOfWeek: DayOfWeek): List<RecurringWishedClass>
}