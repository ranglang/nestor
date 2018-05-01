package org.gotson.nestor.interfaces.lambda.api.dto

import org.gotson.nestor.domain.model.User

data class CreateUserDto(
        val email: String,
        val firstName: String,
        val lastName: String
) {
    fun toDomain(): User =
            User(email = email, firstName = firstName, lastName = lastName)
}