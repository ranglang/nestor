package org.gotson.nestor.interfaces.web

import org.gotson.nestor.domain.model.PlannedClass
import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.persistence.PlannedClassRepository
import org.gotson.nestor.domain.persistence.StudioRepository
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

@RestController
@RequestMapping("studios")
class StudioController(
    private val studioRepository: StudioRepository,
    private val plannedClassRepository: PlannedClassRepository
) {
  @GetMapping
  fun getAll(): Iterable<Studio> =
      studioRepository.findAll()

  @GetMapping("/{id}")
  fun get(@PathVariable id: Long): Studio =
      studioRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun add(@Valid @RequestBody newStudio: Studio): Studio {
    if (!studioRepository.existsByUrl(newStudio.url)) {
      return studioRepository.save(newStudio)
    } else {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, "A studio with this url already exists")
    }
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable id: Long) {
    if (studioRepository.existsById(id))
      studioRepository.deleteById(id)
    else
      throw ResponseStatusException(HttpStatus.NOT_FOUND)
  }

  @GetMapping("/{id}/classes")
  fun getPlannedClasses(@PathVariable id: Long): Iterable<PlannedClassDto> =
      plannedClassRepository.findByStudio_Id(id).map { it.toDto() }
}

data class PlannedClassDto(
    val id: Long,
    val date: LocalDate,
    val time: LocalTime,
    val instructor: String,
    val location: String,
    val type: String,
    val studioId: Long
)

fun PlannedClass.toDto() =
    PlannedClassDto(
        id = id!!,
        date = date,
        time = time,
        instructor = instructor,
        location = location,
        type = type,
        studioId = studio.id!!
    )