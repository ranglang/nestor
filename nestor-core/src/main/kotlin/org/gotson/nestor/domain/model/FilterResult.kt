package org.gotson.nestor.domain.model

data class FilterResult(
    val filtered: Boolean,
    val busyTime: BusyTime?
)