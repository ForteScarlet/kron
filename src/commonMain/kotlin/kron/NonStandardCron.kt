package kron

import kotlinx.datetime.*
import kron.utils.plus

/*
 @yearly : Run once a year, ie. "0 0 1 1 *".
 @annually : Run once a year, ie. "0 0 1 1 *".
 @monthly : Run once a month, ie. "0 0 1 * *".
 @weekly : Run once a week, ie. "0 0 * * 0".
 @daily : Run once a day, ie. "0 0 * * *".
 @hourly : Run once an hour, ie. "0 * * * *".
 */

/**
 * `@yearly` : Run once a year, i.e. "0 0 1 1 *".
 */
open class Yearly(
    m: Month,
    d: Int,
    h: Int,
    min: Int,
    s: Int,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) : Cron {
    override val expression: String = "$s $min $h $d ${m.number} *"

    override val second: Cron.Value = FixedValue.Second(s)
    override val minute: Cron.Value = FixedValue.Minute(min)
    override val hour: Cron.Value = FixedValue.Hour(h)
    override val dayOfMonth: Cron.Value = FixedValue.DayOfMonth(d)
    override val month: Cron.Value = FixedValue.Month(m)
    override val dayOfWeek: Cron.Value get() = AnyValue.DayOfWeek

    override fun contains(localDateTime: LocalDateTime): Boolean {
        return localDateTime.second in second &&
                localDateTime.minute in minute &&
                localDateTime.hour in hour &&
                localDateTime.dayOfMonth in dayOfMonth &&
                localDateTime.monthNumber in month &&
                localDateTime.dayOfWeek.isoDayNumber in dayOfWeek
    }

    private val dateTime = DateTime(m, d, h, min, s)

    // override fun contains(epochMilliseconds: Instant): Boolean = dateTime.contains(epochMilliseconds, timeZone)

    override fun executor(startTime: Instant, endTime: Instant?, timeZone: TimeZone): Cron.Executor =
        YearlyExecutor(startTime, endTime, timeZone)

    @Suppress("PropertyName")
    private inner class YearlyExecutor(
        override val startTime: Instant,
        override val endTime: Instant?,
        private val timeZone: TimeZone,
    ) :
        Cron.Executor {
        private val endDateTime = endTime?.toLocalDateTime(timeZone)

        private var noMore = false
        var _next: LocalDateTime

        init {
            val st = startTime.toLocalDateTime(timeZone)
            val year = st.year
            val dateTime = dateTime.toLocalDateTime(year)

            _next = dateTime
            if (st > dateTime) {
                toNext()
            }
        }

        private fun toNext() {
            val n = _next.plus(1, DateTimeUnit.YEAR, timeZone)
            if (endDateTime != null) {
                if (n > endDateTime) {
                    noMore = true
                }
            }
            _next = n
        }

        override fun next(): Instant {
            if (noMore) {
                throw NoSuchElementException("No more element.")
            }
            return _next.toInstant(timeZone).also { toNext() }

        }

        override fun hasNext(): Boolean = !noMore
    }

    override fun toString(): String {
        return "@Yearly(dateTime=$dateTime)"
    }

    companion object : Yearly(Month.JANUARY, 1, 0, 0, 0)
}


/**
 * `@annually` : Run once a year, i.e. "0 0 1 1 *".
 */
open class Annually(m: Month, d: Int, h: Int, min: Int, s: Int) : Yearly(m, d, h, min, s) {
    companion object : Annually(Month.JANUARY, 1, 0, 0, 0)
}