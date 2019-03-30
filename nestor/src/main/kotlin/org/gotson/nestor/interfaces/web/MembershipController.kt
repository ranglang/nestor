package org.gotson.nestor.interfaces.web

import org.gotson.nestor.domain.model.Membership
import org.gotson.nestor.domain.persistence.MembershipRepository
import org.gotson.nestor.domain.persistence.StudioRepository
import org.gotson.nestor.domain.persistence.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("membership")
class MembershipController(
    private val membershipRepository: MembershipRepository,
    private val userRepository: UserRepository,
    private val studioRepository: StudioRepository
) {
  @GetMapping("/{id}")
  fun get(@PathVariable id: Long): Membership =
      membershipRepository.findByIdOrNull(id)?.redacted() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun add(@Valid @RequestBody newMembership: MemberShipCreationDto): Membership {
    if (!membershipRepository.existsByUserIdAndStudioId(newMembership.userId, newMembership.studioId)) {
      val user = userRepository.findByIdOrNull(newMembership.userId)
      val studio = studioRepository.findByIdOrNull(newMembership.studioId)
      when {
        user != null && studio != null -> return membershipRepository.save(Membership(
            user = user,
            studio = studio,
            login = newMembership.login,
            password = newMembership.password
        )).redacted()
        user == null -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist")
        studio == null -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Studio doesn't exist")
        else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
      }
    } else {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, "A membership for this user and studio already exists")
    }
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable id: Long) {
    if (membershipRepository.existsById(id))
      membershipRepository.deleteById(id)
    else
      throw ResponseStatusException(HttpStatus.NOT_FOUND)
  }
}

data class MemberShipCreationDto(
    val userId: Long,
    val studioId: Long,
    @get:NotBlank
    val login: String,
    @get:NotBlank
    val password: String
)