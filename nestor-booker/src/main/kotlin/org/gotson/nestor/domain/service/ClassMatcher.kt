package org.gotson.nestor.domain.service

import mu.KotlinLogging
import org.gotson.nestor.domain.model.PlannedClass
import org.gotson.nestor.domain.model.WishedClassDated

private val logger = KotlinLogging.logger {}

fun WishedClassDated.matches(plannedClass: PlannedClass): Boolean {
    logger.debug { "Matching classes:\nPlanned: $plannedClass\nWished: ${this.summary()}" }

    val dateMatch = dateTime == plannedClass.dateTime
    logger.debug { "Dates matching: $dateMatch" }

    val typeMatch = type.isNotEmpty()
            && plannedClass.type.contains(type, ignoreCase = true)
    logger.debug { "Types matching: $typeMatch" }

    val locationMatch = location.isNotEmpty()
            && plannedClass.location.contains(location, ignoreCase = true)
    logger.debug { "Locations matching: $locationMatch" }

    return dateMatch && typeMatch && locationMatch
}