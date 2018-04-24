package org.gotson.nestor.infrastructure.persistence

import org.gotson.nestor.domain.model.Membership
import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.model.User
import org.gotson.nestor.domain.model.WishedClass
import org.gotson.nestor.infrastructure.encryption.EncryptionService
import org.gotson.nestor.infrastructure.persistence.dto.MembershipDynamo
import org.gotson.nestor.infrastructure.persistence.dto.StudioDynamo
import org.gotson.nestor.infrastructure.persistence.dto.UserDynamo
import org.gotson.nestor.infrastructure.persistence.dto.WishedClassDynamo
import org.gotson.nestor.infrastructure.persistence.repository.MembershipRepository
import org.gotson.nestor.infrastructure.persistence.repository.StudioRepository
import org.gotson.nestor.infrastructure.persistence.repository.UserRepository
import org.gotson.nestor.infrastructure.persistence.repository.WishedClassRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.DayOfWeek

@Service
class PersistenceService @Autowired constructor(
        private val membershipRepository: MembershipRepository,
        private val wishedClassRepository: WishedClassRepository,
        private val studioRepository: StudioRepository,
        private val userRepository: UserRepository,
        private val encryptionService: EncryptionService
) {

    fun findWishedClassByDay(day: DayOfWeek): List<WishedClass> =
            wishedClassRepository
                    .findByDay(day)
                    .map { it.toDomain() }

    fun findAllWishedClass(): List<WishedClass> =
            wishedClassRepository
                    .findAll()
                    .map { it.toDomain() }

    fun findOneMembership(id: String): Membership =
            membershipRepository.findOne(id).toDomain()

    fun findOneStudio(id: String): Studio =
            studioRepository.findOne(id).toDomain()

    fun findOneUser(id: String): User =
            userRepository.findOne(id).toDomain()

    fun save(user: User): UserDynamo? =
            userRepository.save(UserDynamo.from(user))

    fun save(studio: Studio): StudioDynamo? =
            studioRepository.save(StudioDynamo.from(studio))

    fun save(membership: Membership): MembershipDynamo? {
        val dto = MembershipDynamo.from(membership)
        dto.login = encryptionService.encrypt(dto.login!!)
        dto.password = encryptionService.encrypt(dto.password!!)
        return membershipRepository.save(dto)
    }

    fun save(wishedClass: WishedClass): WishedClassDynamo? =
            wishedClassRepository.save(WishedClassDynamo.from(wishedClass))

    fun WishedClassDynamo.toDomain(): WishedClass {
        return WishedClass(
                id,
                membershipRepository.findOne(membershipId).toDomain(),
                time!!,
                day!!,
                location!!,
                type!!
        )
    }

    fun MembershipDynamo.toDomain(): Membership =
            Membership(
                    id,
                    userRepository.findOne(userId).toDomain(),
                    studioRepository.findOne(studioId).toDomain(),
                    login!!,
                    password!!
            )
}