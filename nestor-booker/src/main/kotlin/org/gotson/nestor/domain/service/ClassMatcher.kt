package org.gotson.nestor.domain.service

import org.gotson.nestor.domain.model.PlannedClass
import org.gotson.nestor.domain.model.WishedClassDated

fun WishedClassDated.matches(plannedClass: PlannedClass): Boolean {
    return dateTime == plannedClass.dateTime
            && type.isNotEmpty()
            && plannedClass.type.contains(type, ignoreCase = true)
            && location.isNotEmpty()
            && plannedClass.location.contains(location, ignoreCase = true)
}