package kron.utils

import kotlinx.datetime.*


public fun LocalDateTime.plus(value: Int, unit: DateTimeUnit, zone: TimeZone): LocalDateTime {
    return this.toInstant(zone).plus(value, unit, zone).toLocalDateTime(zone)
}
