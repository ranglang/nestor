package org.gotson.nestor.domain.persistence

import org.gotson.nestor.domain.model.Membership
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MembershipRepository : CrudRepository<Membership, Long> {
  fun existsByUserIdAndStudioId(userId: Long, studioId: Long): Boolean
}