package org.gotson.nestor.interfaces.lambda.api.dto

import org.gotson.nestor.domain.model.Membership

data class MembershipDto(
        val id: String,
        val user: UserDto,
        val studio: StudioDto
)

fun Membership.toDto(): MembershipDto =
        MembershipDto(
                id!!,
                user.toDto(),
                studio.toDto()
        )