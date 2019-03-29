package org.gotson.nestor.interfaces.web

import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.persistence.StudioRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid

@RestController
@RequestMapping("studio")
class StudioController(
        private val studioRepository: StudioRepository
) {
    @GetMapping
    fun getAll(): Iterable<Studio> =
            studioRepository.findAll()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Studio =
            studioRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PostMapping
    fun add(@Valid @RequestBody newStudio: Studio): Studio {
        if (!studioRepository.existsByUrl(newStudio.url)) {
            studioRepository.save(newStudio)
            return newStudio
        } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "A studio with this url already exists")
        }
    }
}