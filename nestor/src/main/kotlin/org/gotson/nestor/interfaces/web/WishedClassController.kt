package org.gotson.nestor.interfaces.web

import org.gotson.nestor.domain.model.RecurringWishedClass
import org.gotson.nestor.domain.persistence.MembershipRepository
import org.gotson.nestor.domain.persistence.RecurringWishedClassRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
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
@RequestMapping("/wishedclass")
class WishedClassController(
    private val classRepository: RecurringWishedClassRepository,
    private val membershipRepository: MembershipRepository
) {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun addOne(@Valid @RequestBody wishedClass: RecurringWishedClassCreationDto): RecurringWishedClassDto {
    val membership = membershipRepository.findByIdOrNull(wishedClass.membershipId)
        ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Membership not found")
    return classRepository.save(
        RecurringWishedClass(
            membership = membership,
            time = wishedClass.time,
            day = wishedClass.day,
            location = wishedClass.location,
            type = wishedClass.type
        )
    ).toDto()
  }
}

data class RecurringWishedClassCreationDto(
    val membershipId: Long,
    @get:NotNull val time: LocalTime,
    @get:NotNull val day: DayOfWeek,
    @get:NotBlank val location: String,
    @get:NotBlank val type: String
)

data class RecurringWishedClassDto(
    val id: Long,
    val membershipId: Long,
    val time: LocalTime,
    val day: DayOfWeek,
    val location: String,
    val type: String
)

fun RecurringWishedClass.toDto() =
    RecurringWishedClassDto(
        id = id!!,
        membershipId = membership.id!!,
        time = time,
        day = day,
        location = location,
        type = type
    )
