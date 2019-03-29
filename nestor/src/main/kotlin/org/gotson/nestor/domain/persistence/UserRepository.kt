package org.gotson.nestor.domain.persistence

import org.gotson.nestor.domain.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun existsByEmail(email: String): Boolean
}