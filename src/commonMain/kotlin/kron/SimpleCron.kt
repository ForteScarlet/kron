package kron

import kotlinx.datetime.*
import kron.utils.PreviewIterator
import kron.utils.asPreview


/**
 *
 * [Cron] 的基础实现。
 *
 * @author ForteScarlet
 */
internal class SimpleCron(
    override val second: Cron.Value,
    override val minute: Cron.Value,
    override val hour: Cron.Value,
    override val dayOfMonth: Cron.Value,
    override val month: Cron.Value,
    override val dayOfWeek: Cron.Value = AnyValue.DayOfWeek,
    // year?
    // val startYear: Int = nowYear(),
    override val expression: String = "${second.literal} ${minute.literal} ${hour.literal} ${dayOfMonth.literal} ${month.literal} ${dayOfWeek.literal}",
) : Cron {

    override fun contains(localDateTime: LocalDateTime): Boolean {
        return localDateTime.second in second &&
                localDateTime.minute in minute &&
                localDateTime.hour in hour &&
                localDateTime.dayOfMonth in dayOfMonth &&
                localDateTime.monthNumber in month &&
                localDateTime.dayOfWeek.isoDayNumber in dayOfWeek
    }

    override fun executor(startTime: Instant, endTime: Instant?, timeZone: TimeZone): Cron.Executor {
        return Executor(startTime, endTime, timeZone)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    internal inner class Executor(
        override val startTime: Instant,
        override val endTime: Instant?,
        val timeZone: TimeZone,
    ) : Cron.Executor {
        private val startLocalDateTime = startTime.toLocalDateTime(timeZone)
        private var year = startLocalDateTime.year

        // private var dateTimeOfYear = startLocalDateTime.toDateTime()
        // private var _next: LocalDateTime?

        private var dateTimeIter: DateTimeIter = newDateTimeIter(startLocalDateTime.toDateTime())


        private fun newDateTimeIter(startDateTime: DateTime? = null) = DateTimeIter(
            startDateTime,
            // year,
            month,
            dayOfMonth,
            hour,
            minute,
            second
        )

        private fun nextRound() {
            dateTimeIter = newDateTimeIter()
            nextYear()
        }


        override fun next(): Instant {
            // var times = 1
            // while (!dateTimeIter.hasNext()) {
            //     nextRound()
            //     if (times++ > 100) {
            //         throw NoSuchElementException("No more element.")
            //     }
            // }
            val next = dateTimeIter.next()
            return next.toInstant(year, timeZone)
            // val n = _next ?: throw NoSuchElementException("No more element.")
            // return n.toInstant(timeZone).also { toNext() }
        }

        override fun hasNext(): Boolean {
            var times = 1
            while (!dateTimeIter.hasNext()) {
                nextRound()
                if (times++ > 100) {
                    return false
                }
            }
            return true
        } // true // No year, always true now.


        private fun nextYear() {
            year++
        }


    }

}


// fun DateTimeIter(
//     // 月份所对应的年
//     year: Int,
//     month: String, //.Month,
//     dayOfMonth: String, //.Day.OfMonth,
//     dayOfWeek: String, //.Day.OfWeek,
//     hour: String, //.Hour,
//     minute: String, //.Minute,
//     second: String, //.Second,
//     initDateTime: DateTime? = null,
// ): DateTimeIter = DateTimeIter(
//     year,
//     cronMonth(month),
// )


// 月 日 时 分 秒
// TODO 暂时不支持 星期
@Suppress("CanBeParameter")
class DateTimeIter(
    // 月份所对应的年
    initDateTime: DateTime? = null,
    //private val year: Int,
    private val month: Cron.Value, //.Month,
    private val dayOfMonth: Cron.Value, //.Day.OfMonth,
    private val hour: Cron.Value, //.Hour,
    private val minute: Cron.Value, //.Minute,
    private val second: Cron.Value, //.Second,
    // dayOfWeek: Cron.Value? = null, //.Day.OfWeek,
) {
    private val monthIter: PreviewIterator<Int> = month.iterator().asPreview()

    // private var nextMonth: Int? = null
    private var lastMonth: Int? = null

    private var dayOfMonthIter: PreviewIterator<Int> = dayOfMonth.iterator().asPreview()

    // private var nextDayOfMonth: Int? = null
    private var lastDayOfMonth: Int? = null


    private var hourIter: PreviewIterator<Int> = hour.iterator().asPreview()

    // private var nextHour: Int? = null
    private var lastHour: Int? = null

    private var minuteIter: PreviewIterator<Int> = minute.iterator().asPreview()

    // private var nextMinute: Int? = null
    private var lastMinute: Int? = null

    private var secondIter: PreviewIterator<Int> = second.iterator().asPreview()
    // private var nextSecond: Int? = null
    // private var lastSecond: Int

    init {

        if (initDateTime != null) {
            // init date.
            // month
            val initMonth = initDateTime.month.number
            while (monthIter.hasNext()) {
                val pre = monthIter.preNext!!
                if (pre >= initMonth) {
                    break
                }
                monthIter.next()
            }

            // day of month
            val initDayOfMonth = initDateTime.day
            while (dayOfMonthIter.hasNext()) {
                val pre = dayOfMonthIter.preNext!!
                if (pre >= initDayOfMonth) {
                    break
                }
                dayOfMonthIter.next()
            }

            // hour
            val initHour = initDateTime.hour
            while (hourIter.hasNext()) {
                val pre = hourIter.preNext!!
                if (pre >= initHour) {
                    break
                }
                hourIter.next()
            }

            // minute
            val initMinute = initDateTime.minute
            while (minuteIter.hasNext()) {
                val pre = minuteIter.preNext!!
                if (pre >= initMinute) {
                    break
                }
                minuteIter.next()
            }

            // second
            val initSecond = initDateTime.second
            while (secondIter.hasNext()) {
                val pre = secondIter.preNext!!
                if (pre >= initSecond) {
                    break
                }
                secondIter.next()
            }

        }

    }

    // Preview

    //region hasNext
    private fun hasNextMonth(): Boolean {
        return monthIter.hasNext()
    }

    private fun hasNextDayOfMonth(): Boolean {
        // return hasNextMonth() && nextDayOfMonth != null
        if (dayOfMonthIter.hasNext()) {
            return true
        }
        if (hasNextMonth()) {
            nextMonth()
            dayOfMonthIter = dayOfMonth.iterator().asPreview()
            return true
        }
        return false
    }

    private fun hasNextHour(): Boolean {
        // return hourIter.hasNext()
        // return hasNextDayOfMonth() && nextHour != null
        if (hourIter.hasNext()) {
            return true
        }
        if (hasNextDayOfMonth()) {
            nextDayOfMonth()
            hourIter = hour.iterator().asPreview()
            return true
        }
        return false
    }

    private fun hasNextMinute(): Boolean {
        // return hasNextHour() && minuteIter.hasNext()
        if (minuteIter.hasNext()) {
            return true
        }
        if (hasNextHour()) {
            nextHour()
            minuteIter = minute.iterator().asPreview()
            return true
        }
        return false
    }

    private fun hasNextSecond(): Boolean {
        // return hasNextMinute() && nextSecond != null
        if (secondIter.hasNext()) {
            return true
        }
        if (hasNextMinute()) {
            nextMinute()
            secondIter = second.iterator().asPreview()
            return true
        }
        return false
    }
    //endregion


    //region nextValue
    private fun nextMonth(): Int {
        return monthIter.next().also { lastMonth = it }
    }

    private fun nextDayOfMonth(): Int {
        if (hasNextDayOfMonth()) {
            return dayOfMonthIter.next().also { lastDayOfMonth = it }
        }
        throw NoSuchElementException("No more day of month.")
    }

    private fun nextHour(): Int {
        if (hasNextHour()) {
            return hourIter.next().also { lastHour = it }
        }
        throw NoSuchElementException("No more hour.")
    }

    private fun nextMinute(): Int {
        if (hasNextMinute()) {
            return minuteIter.next().also { lastMinute = it }
        }
        throw NoSuchElementException("No more minute.")
    }


    private fun nextSecond(): Int {

        if (hasNextSecond()) {
            return secondIter.next()
        }
        throw NoSuchElementException("No more second.")
    }
    //endregion

    //region lastValue

    private fun lastMonth(): Int = lastMonth ?: nextMonth().also { lastMonth = it }
    private fun lastDayOfMonth(): Int = lastDayOfMonth ?: nextDayOfMonth().also { lastDayOfMonth = it }
    private fun lastHour(): Int = lastHour ?: nextHour().also { lastHour = it }
    private fun lastMinute(): Int = lastMinute ?: nextMinute().also { lastMinute = it }
    // private fun lastSecond(): Int = lastSecond

    //endregion


    fun hasNext(): Boolean {
        return hasNextSecond()
    }


    fun next(): DateTime {
        // val nextSecond = nextSecond()
        return dateTime(lastMonth(), lastDayOfMonth(), lastHour(), lastMinute(), nextSecond())
    }

}



