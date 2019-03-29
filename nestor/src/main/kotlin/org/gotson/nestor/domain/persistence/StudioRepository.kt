package org.gotson.nestor.domain.persistence

import org.gotson.nestor.domain.model.Studio
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StudioRepository : CrudRepository<Studio, Long> {
    fun existsByUrl(url: String): Boolean
}