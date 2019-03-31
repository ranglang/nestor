package org.gotson.nestor.domain.model

import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
data class User(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @get:Email
    val email: String,

    @get:NotBlank
    val firstName: String,

    @get:NotBlank
    val lastName: String,

    @ElementCollection(fetch = FetchType.EAGER)
    val icalCalendars: List<String> = emptyList()
)