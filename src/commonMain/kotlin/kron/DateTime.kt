package kron

import kotlinx.datetime.*
import kron.utils.maxDay
import kron.utils.nowYear


/**
 *
 * @author ForteScarlet
 */
data class DateTime(val month: Month, val day: Int = 1, val hour: Int = 0, val minute: Int = 0, val second: Int = 0) :
    Comparable<DateTime> {
    init {

        // val monthMaxDay = month.maxDay(true)
        require(day in 1..31) { "Day must in 1..31, but $day" }
        require(hour in 0..23) { "Hour must in 0..23, but $hour" }
        require(minute in 0..59) { "Minute must in 0..59, but $minute" }
        require(second in 0..59) { "Second must in 0..59, but $second" }
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

fun LocalDateTime.toDateTime(): DateTime = DateTime(month, dayOfMonth, hour, minute, second)

/**
 * @throws DateTimeException If year is not a leap year but date is 'February 29'
 */
@Suppress("KDocUnresolvedReference")
fun DateTime.toLocalDateTime(
    year: Int = nowYear(),
): LocalDateTime =
    LocalDateTime(year, month, day, hour, minute, second)

/**
 * Will check leap year. if [year] is a leap year but date is 'February 29', return null.
 */
fun DateTime.toLocalDateTimeOrNull(
    year: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year,
): LocalDateTime? {
    if (day > month.maxDay(year)) {
        return null
    }
    return LocalDateTime(year, month, day, hour, minute, second)
}

fun DateTime.toInstant(year: Int, timeZone: TimeZone): Instant =
    toLocalDateTime(year).toInstant(timeZone)

fun DateTime(month: Int, day: Int = 1, hour: Int = 0, minute: Int = 0, second: Int = 0): DateTime =
    DateTime(Month(month), day, hour, minute, second)

fun DateTime(localDateTime: LocalDateTime): DateTime =
    DateTime(localDateTime.month,
        localDateTime.dayOfMonth,
        localDateTime.hour,
        localDateTime.minute,
        localDateTime.second)

operator fun DateTime.contains(localDateTime: LocalDateTime): Boolean {
    return month == localDateTime.month &&
            day == localDateTime.dayOfMonth &&
            hour == localDateTime.hour &&
            minute == localDateTime.minute &&
            second == localDateTime.second
}


fun DateTime.contains(instant: Instant, timeZone: TimeZone): Boolean =
    instant.toLocalDateTime(timeZone) in this