package org.gotson.nestor.domain.persistence

import org.gotson.nestor.domain.model.WeeklyClassRequest
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.DayOfWeek

@Repository
interface WeeklyClassRequestRepository : CrudRepository<WeeklyClassRequest, Long> {
  fun findByDay(dayOfWeek: DayOfWeek): List<WeeklyClassRequest>
}