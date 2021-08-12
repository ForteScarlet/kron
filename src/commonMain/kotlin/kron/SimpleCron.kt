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
    override val dayOfWeek: Cron.Value,
    // year?
    val year: Cron.Value = AnyValue.Second,
    override val expression: String = "${second.literal} ${minute.literal} ${hour.literal} ${dayOfMonth.literal} ${month.literal} ${dayOfWeek.literal}",
) : Cron {

    override fun contains(epochMilliseconds: Instant): Boolean {
        TODO("Not yet implemented")
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

        private var dateTimeOfYear = startLocalDateTime.toDateTime()
        private var _next: LocalDateTime?

        init {
            _next = initNext()
        }


        override fun next(): Instant {
            val n = _next ?: throw NoSuchElementException("No more element.")

            return n.toInstant(timeZone).also { toNext() }
        }

        override fun hasNext(): Boolean = _next != null


        private fun nextYear() {
            year++
        }

        /**
         * 初始化 [_next]
         */
        private fun initNext(): LocalDateTime {
            // 从 startDate开始，寻找最近的一个值。
            // 年 月 日 时 分 秒
            // 先从月开始找


            TODO()
        }


        /**
         * 得到下一个时间，并置于 [_next] 中。
         */
        private fun toNext() {


        }

    }

}


// 月 日 时 分 秒
class DateTimeIter(
    // 月份所对应的年
    private val year: Int,
    private val month: Cron.Value.Month,
    private val dayOfMonth: Cron.Value.Day.OfMonth,
    private val dayOfWeek: Cron.Value.Day.OfWeek,
    private val hour: Cron.Value.Hour,
    private val minute: Cron.Value.Minute,
    private val second: Cron.Value.Second,
    initDateTime: DateTime? = null,
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

        // lastMonth = monthIter.next()
        // lastDayOfMonth = dayOfMonthIter.next()
        // lastHour = hourIter.next()
        // lastMinute = minuteIter.next()
        // // if (!secondIter.hasNext()) {
        // //     secondIter = second.iterator()
        // // }
        // // nextSecond = secondIter.next()
        //
        // if (!minuteIter.hasNext()) {
        //     minuteIter = minute.iterator().asPreview()
        // }
        // nextMinute = minuteIter.next()
        //
        // if (!hourIter.hasNext()) {
        //     hourIter = hour.iterator().asPreview()
        // }
        // nextHour = hourIter.next()
        //
        // if (!dayOfMonthIter.hasNext()) {
        //     dayOfMonthIter = dayOfMonth.iterator().asPreview()
        // }
        // nextDayOfMonth = dayOfMonthIter.next()
        //
        // if (monthIter.hasNext()) {
        //     nextMonth = monthIter.next()
        // }

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
        // return nextMonth?.also {
        //     lastMonth = it
        //     nextMonth = if (monthIter.hasNext()) monthIter.next() else null
        // } ?: throw NoSuchElementException("No more month.")
    }

    private fun nextDayOfMonth(): Int {
        // return nextDayOfMonth?.also {
        //     lastDayOfMonth = it
        //     nextDayOfMonth = if (dayOfMonthIter.hasNext()) dayOfMonthIter.next() else {
        //         var n: Int? = null
        //         while (hasNextMonth()) {
        //             nextMonth()
        //             val newIter = dayOfMonth.iterator().asPreview()
        //             if (newIter.hasNext()) {
        //                 n = newIter.next()
        //                 dayOfMonthIter = newIter
        //                 break
        //             }
        //         }
        //         n
        //     }
        // } ?: throw NoSuchElementException("No more day of month.")
        // val nd = nextDayOfMonth
        // if (nd != null) {
        //     TODO()
        //     return nd
        // }
        if (hasNextDayOfMonth()) {
            return dayOfMonthIter.next().also { lastDayOfMonth = it }
        }
        throw NoSuchElementException("No more day of month.")
    }

    private fun nextHour(): Int {
        // return nextHour?.also {
        //     lastHour = it
        //     nextHour = if (hourIter.hasNext()) hourIter.next() else {
        //         var n: Int? = null
        //         while (hasNextDayOfMonth()) {
        //             nextDayOfMonth()
        //             val newIter = hour.iterator().asPreview()
        //             if (newIter.hasNext()) {
        //                 n = newIter.next()
        //                 hourIter = newIter
        //                 break
        //             }
        //         }
        //         n
        //     }
        // } ?: throw NoSuchElementException("No more hour.")
        if (hasNextHour()) {
            return hourIter.next().also { lastHour = it }
        }
        throw NoSuchElementException("No more hour.")
    }

    private fun nextMinute(): Int {
        // return nextMinute?.also {
        //     lastMinute = it
        //     nextMinute = if (minuteIter.hasNext()) minuteIter.next() else {
        //         var n: Int? = null
        //         while (hasNextHour()) {
        //             nextHour()
        //             val newIter = minute.iterator().asPreview()
        //             if (newIter.hasNext()) {
        //                 n = newIter.next()
        //                 minuteIter = newIter
        //                 break
        //             }
        //         }
        //         n
        //     }
        // } ?: throw NoSuchElementException("No more minute.")
        if (hasNextMinute()) {
            return minuteIter.next().also { lastMinute = it }
        }
        throw NoSuchElementException("No more minute.")
    }


    private fun nextSecond(): Int {
        // return nextSecond?.also {
        //     // lastSecond = it
        //     nextSecond = if (secondIter.hasNext()) secondIter.next() else {
        //         var n: Int? = null
        //         while (hasNextMinute()) {
        //             nextMinute()
        //             val newIter = second.iterator().asPreview()
        //             if (newIter.hasNext()) {
        //                 n = newIter.next()
        //                 secondIter = newIter
        //                 break
        //             }
        //         }
        //         n
        //     }
        //
        // } ?: throw NoSuchElementException("No more second.")

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
        return DateTime(lastMonth(), lastDayOfMonth(), lastHour(), lastMinute(), nextSecond())
    }

}



