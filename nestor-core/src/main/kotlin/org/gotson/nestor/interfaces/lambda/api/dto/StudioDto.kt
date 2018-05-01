package org.gotson.nestor.interfaces.lambda.api.dto

import org.gotson.nestor.domain.model.Studio

data class StudioDto(
        val id: String,
        val name: String,
        val url: String
)

fun Studio.toDto(): StudioDto =
        StudioDto(id!!, name, url)