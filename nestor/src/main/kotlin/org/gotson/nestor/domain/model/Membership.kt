package org.gotson.nestor.domain.model

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.NotBlank

@Entity
data class Membership(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @ManyToOne(fetch = FetchType.EAGER)
        val user: User,

        @ManyToOne(fetch = FetchType.EAGER)
        val studio: Studio,

        @get:NotBlank
        val login: String,

        @get:NotBlank
        val password: String
) {
        fun redacted() = this.copy(password = "*******")

        override fun toString(): String =
                redacted().toString()
}