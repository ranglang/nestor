package org.gotson.nestor.interfaces.web

import org.gotson.nestor.domain.model.ClassRequest
import org.gotson.nestor.domain.persistence.ClassRequestRepository
import org.gotson.nestor.domain.persistence.MembershipRepository
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
import java.time.LocalDate
import java.time.LocalTime
import javax.validation.Valid
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/classrequests")
class ClassRequestController(
    private val classRepository: ClassRequestRepository,
    private val membershipRepository: MembershipRepository
) {
  @GetMapping
  fun getAll(): Iterable<ClassRequestDto> =
      classRepository.findAll().map { it.toDto() }

  @GetMapping("/{id}")
  fun get(@PathVariable id: Long): ClassRequestDto =
      classRepository.findByIdOrNull(id)?.toDto() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun addOne(@Valid @RequestBody classRequest: ClassRequestCreationDto): ClassRequestDto {
    val membership = membershipRepository.findByIdOrNull(classRequest.membershipId)
        ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Membership not found")
    return classRepository.save(
        ClassRequest(
            membership = membership,
            time = classRequest.time,
            date = classRequest.date,
            location = classRequest.location,
            type = classRequest.type
        )
    ).toDto()
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable id: Long) {
    if (classRepository.existsById(id))
      classRepository.deleteById(id)
    else
      throw ResponseStatusException(HttpStatus.NOT_FOUND)
  }
}

data class ClassRequestCreationDto(
    val membershipId: Long,
    @get:NotNull val time: LocalTime,
    @get:NotNull @get:Future val date: LocalDate,
    @get:NotBlank val location: String,
    @get:NotBlank val type: String
)

data class ClassRequestDto(
    val id: Long,
    val membershipId: Long,
    val time: LocalTime,
    val date: LocalDate,
    val location: String,
    val type: String
)

fun ClassRequest.toDto() =
    ClassRequestDto(
        id = id!!,
        membershipId = membership.id!!,
        time = time,
        date = date,
        location = location,
        type = type
    )
