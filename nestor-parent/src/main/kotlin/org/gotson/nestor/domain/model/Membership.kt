package org.gotson.nestor.domain.model

data class Membership(
        val id: String? = null,
        val user: User,
        val studio: Studio,
        val login: String,
        val password: String
)