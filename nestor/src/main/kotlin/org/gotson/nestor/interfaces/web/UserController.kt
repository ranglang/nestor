package org.gotson.nestor.interfaces.web

import org.gotson.nestor.domain.model.User
import org.gotson.nestor.domain.persistence.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid

@RestController
@RequestMapping("user")
class UserController(
        private val userRepository: UserRepository
) {
    @GetMapping
    fun getAll(): Iterable<User> =
            userRepository.findAll()

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): User =
            userRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addOne(@Valid @RequestBody newUser: User): User {
        if (!userRepository.existsByEmail(newUser.email)) {
            userRepository.save(newUser)
            return newUser
        } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "A user with this email already exists")
        }
    }
}