package org.gotson.nestor.interfaces.lambda.dto

data class CreateMembershipDto(
        val userId: String,
        val studioId: String,
        val login: String,
        val password: String
)