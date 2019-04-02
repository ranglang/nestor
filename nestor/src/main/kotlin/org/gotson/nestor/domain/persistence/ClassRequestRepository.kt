package org.gotson.nestor.domain.persistence

import org.gotson.nestor.domain.model.ClassRequest
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ClassRequestRepository : CrudRepository<ClassRequest, Long> {
  fun findByDate(date: LocalDate): List<ClassRequest>
  fun deleteByDateBefore(date: LocalDate)
}