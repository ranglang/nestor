package org.gotson.nestor.domain.model

data class User(
        val id: String? = null,
        val email: String,
        val firstName: String,
        val lastName: String
)