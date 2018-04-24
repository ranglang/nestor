package org.gotson.nestor.interfaces.lambda.dto

import org.gotson.nestor.domain.model.Studio

data class CreateStudioDto(
        val name: String,
        val url: String
) {
    fun toDomain(): Studio =
            Studio(name = name, url = url)
}