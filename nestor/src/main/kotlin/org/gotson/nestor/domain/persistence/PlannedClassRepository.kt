package org.gotson.nestor.domain.persistence

import org.gotson.nestor.domain.model.PlannedClass
import org.gotson.nestor.domain.model.Studio
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface PlannedClassRepository : CrudRepository<PlannedClass, Long> {
  fun deleteByDateBetweenAndStudio(date: LocalDate, date2: LocalDate, studio: Studio)
  fun findByStudio_Id(studioId: Long): List<PlannedClass>
}