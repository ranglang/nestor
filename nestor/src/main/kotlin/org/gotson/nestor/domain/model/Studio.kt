package org.gotson.nestor.domain.model

import org.hibernate.validator.constraints.URL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class Studio(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @get:NotBlank
        val name: String,

        @get:URL
        val url: String
)