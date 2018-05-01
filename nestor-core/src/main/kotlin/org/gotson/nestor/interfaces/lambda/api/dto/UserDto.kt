package org.gotson.nestor.interfaces.lambda.api.dto

import org.gotson.nestor.domain.model.User

data class UserDto(
        val id: String,
        val email: String,
        val firstName: String,
        val lastName: String
)

fun User.toDto(): UserDto =
        UserDto(id!!, email, firstName, lastName)