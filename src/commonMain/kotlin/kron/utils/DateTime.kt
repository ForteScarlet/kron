package kron.utils

import kotlinx.datetime.*


/**
 *
 * @author ForteScarlet
 */
internal data class DateTime(val month: Month, val day: Int, val hour: Int, val minute: Int, val second: Int) :
    Comparable<DateTime> {
    init {

        val monthMaxDay = month.maxDay(true)
        require(day in 1..monthMaxDay) { "Day must in 1..$monthMaxDay, but $day" }
        require(hour in 0..23) { "Hour must in 0..23, but $hour" }
        require(minute in 0..23) { "Hour must in 0..59, but $minute" }
        require(second in 0..23) { "Hour must in 0..59, but $second" }
    }

    override fun compareTo(other: DateTime): Int {
        if (this === other) return 0
        if (month != other.month) return month.compareTo(other.month)
        if (day != other.day) return day.compareTo(other.day)
        if (hour != other.hour) return hour.compareTo(other.hour)
        if (minute != other.minute) return minute.compareTo(other.minute)
        if (second != other.second) return second.compareTo(other.second)

        return 0
    }

}

internal fun DateTime.toLocalDateTime(year: Int): LocalDateTime =
    LocalDateTime(year, month, day, hour, minute, second)

internal fun DateTime.toInstant(year: Int, timeZone: TimeZone): Instant =
    toLocalDateTime(year).toInstant(timeZone)

internal fun DateTime(month: Int, day: Int, hour: Int, minute: Int, second: Int): DateTime =
    DateTime(Month(month), day, hour, minute, second)

internal fun DateTime(localDateTime: LocalDateTime): DateTime =
    DateTime(localDateTime.month,
        localDateTime.dayOfMonth,
        localDateTime.hour,
        localDateTime.minute,
        localDateTime.second)

internal operator fun DateTime.contains(localDateTime: LocalDateTime): Boolean {
    return month == localDateTime.month &&
            day == localDateTime.dayOfMonth &&
            hour == localDateTime.hour &&
            minute == localDateTime.minute &&
            second == localDateTime.second
}


internal fun DateTime.contains(instant: Instant, timeZone: TimeZone): Boolean =
    instant.toLocalDateTime(timeZone) in this