package org.gotson.nestor.interfaces.web

import org.gotson.nestor.domain.model.WeeklyClassRequest
import org.gotson.nestor.domain.persistence.MembershipRepository
import org.gotson.nestor.domain.persistence.WeeklyClassRequestRepository
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
import java.time.DayOfWeek
import java.time.LocalTime
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/weeklyclassrequests")
class WeeklyClassRequestController(
    private val classRepository: WeeklyClassRequestRepository,
    private val membershipRepository: MembershipRepository
) {
  @GetMapping
  fun getAll(): Iterable<WeeklyClassRequestDto> =
      classRepository.findAll().map { it.toDto() }

  @GetMapping("/{id}")
  fun get(@PathVariable id: Long): WeeklyClassRequestDto =
      classRepository.findByIdOrNull(id)?.toDto() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun addOne(@Valid @RequestBody classRequest: WeeklyClassRequestCreationDto): WeeklyClassRequestDto {
    val membership = membershipRepository.findByIdOrNull(classRequest.membershipId)
        ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Membership not found")
    return classRepository.save(
        WeeklyClassRequest(
            membership = membership,
            time = classRequest.time,
            day = classRequest.day,
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

data class WeeklyClassRequestCreationDto(
    val membershipId: Long,
    @get:NotNull val time: LocalTime,
    @get:NotNull val day: DayOfWeek,
    @get:NotBlank val location: String,
    @get:NotBlank val type: String
)

data class WeeklyClassRequestDto(
    val id: Long,
    val membershipId: Long,
    val time: LocalTime,
    val day: DayOfWeek,
    val location: String,
    val type: String
)

fun WeeklyClassRequest.toDto() =
    WeeklyClassRequestDto(
        id = id!!,
        membershipId = membership.id!!,
        time = time,
        day = day,
        location = location,
        type = type
    )
