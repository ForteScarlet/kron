package kron

import kotlinx.datetime.*


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
    override val expression: String = "${second.literal} ${minute.literal} ${hour.literal} ${dayOfMonth.literal} ${month.literal} ${dayOfWeek.literal}",
) : Cron {

    override fun contains(epochMilliseconds: Instant): Boolean {
        TODO("Not yet implemented")
    }

    override fun executor(startTime: Instant, endTime: Instant?, timeZone: TimeZone): Cron.Executor {
        return Executor(startTime, endTime, timeZone)
    }

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
    private val month: Cron.Value.Month,
    private val dayOfMonth: Cron.Value.Day.OfMonth,
    private val dayOfWeek: Cron.Value.Day.OfWeek,
    private val hour: Cron.Value.Hour,
    private val minute: Cron.Value.Minute,
    private val second: Cron.Value.Second,
    private val initDateTime: DateTime? = null,
) {
    private val monthIter: Iterator<Int> = month.iterator()
    private var nextMonth: Int? = null
    private var lastMonth: Int

    private var dayOfMonthIter: Iterator<Int> = dayOfMonth.iterator()
    private var nextDayOfMonth: Int? = null
    private var lastDayOfMonth: Int


    private var hourIter: Iterator<Int> = hour.iterator()
    private var nextHour: Int? = null
    private var lastHour: Int

    private var minuteIter: Iterator<Int> = minute.iterator()
    private var nextMinute: Int? = null
    private var lastMinute: Int

    private var secondIter: Iterator<Int> = second.iterator()
    private var nextSecond: Int? = null
    // private var lastSecond: Int

    init {
        lastMonth = monthIter.next()
        lastDayOfMonth = dayOfMonthIter.next()
        lastHour = hourIter.next()
        lastMinute = minuteIter.next()
        nextSecond = secondIter.next()


        // if (!secondIter.hasNext()) {
        //     secondIter = second.iterator()
        // }
        // nextSecond = secondIter.next()

        if (!minuteIter.hasNext()) {
            minuteIter = minute.iterator()
        }
        nextMinute = minuteIter.next()

        if (!hourIter.hasNext()) {
            hourIter = hour.iterator()
        }
        nextHour = hourIter.next()

        if (!dayOfMonthIter.hasNext()) {
            dayOfMonthIter = dayOfMonth.iterator()
        }
        nextDayOfMonth = dayOfMonthIter.next()

        if (monthIter.hasNext()) {
            nextMonth = monthIter.next()
        }



    }



    //region hasNext
    private fun hasNextMonth(): Boolean {
        return nextMonth != null
    }

    private fun hasNextDayOfMonth(): Boolean {
        return hasNextMonth() && nextDayOfMonth != null
        // if (nextDayOfMonth == null) {
        //     return false
        // }
        //
        // if (hasNextMonth()) {
        //     val nowMonth = lastMonth()
        //
        //
        //     return false
        // }
        //
    }

    private fun hasNextHour(): Boolean {
        return hasNextDayOfMonth() && nextHour != null
        // if (hourIter.hasNext()) {
        //     return true
        // }
        // if (hasNextDayOfMonth()) {
        //     nextDayOfMonth()
        //     hourIter = hour.iterator()
        //     return true
        // }
        // return false
    }

    private fun hasNextMinute(): Boolean {
        return hasNextHour() && nextMinute != null
        // if (minuteIter.hasNext()) {
        //     return true
        // }
        // if (hasNextHour()) {
        //     nextHour()
        //     minuteIter = minute.iterator()
        //     return true
        // }
        // return false
    }

    private fun hasNextSecond(): Boolean {
        return hasNextMinute() && nextSecond != null
        // if (secondIter.hasNext()) {
        //     return true
        // }
        // if (hasNextMinute()) {
        //     nextMinute()
        //     secondIter = second.iterator()
        //     return true
        // }
        // return false
    }
    //endregion


    //region nextValue
    private fun nextMonth(): Int {
        return nextMonth?.also {
            lastMonth = it
            nextMonth = if (monthIter.hasNext()) monthIter.next() else null
        } ?: throw NoSuchElementException("No more month.")
    }

    private fun nextDayOfMonth(): Int {
        return nextDayOfMonth?.also {
            lastDayOfMonth = it
            nextDayOfMonth = if (dayOfMonthIter.hasNext()) dayOfMonthIter.next() else {
                var n: Int? = null
                while (hasNextMonth()) {
                    nextMonth()
                    val newIter = dayOfMonth.iterator()
                    if (newIter.hasNext()) {
                        n = newIter.next()
                        dayOfMonthIter = newIter
                        break
                    }
                }
                n
            }
        } ?: throw NoSuchElementException("No more day of month.")
        // val nd = nextDayOfMonth
        // if (nd != null) {
        //     TODO()
        //     return nd
        // }
        //
        // if (hasNextDayOfMonth()) {
        //     return dayOfMonthIter.next().also { lastDayOfMonth = it }
        // }
        // throw NoSuchElementException("No more day of month.")
    }

    private fun nextHour(): Int {
        return nextHour?.also {
            lastHour = it
            nextHour = if (hourIter.hasNext()) hourIter.next() else {
                var n: Int? = null
                while (hasNextDayOfMonth()) {
                    nextDayOfMonth()
                    val newIter = hour.iterator()
                    if (newIter.hasNext()) {
                        n = newIter.next()
                        hourIter = newIter
                        break
                    }
                }
                n
            }
        } ?: throw NoSuchElementException("No more hour.")
        // if (hasNextHour()) {
        //     return hourIter.next().also { lastHour = it }
        // }
        // throw NoSuchElementException("No more hour.")
    }

    private fun nextMinute(): Int {
        return nextMinute?.also {
            lastMinute = it
            nextMinute = if (minuteIter.hasNext()) minuteIter.next() else {
                var n: Int? = null
                while (hasNextHour()) {
                    nextHour()
                    val newIter = minute.iterator()
                    if (newIter.hasNext()) {
                        n = newIter.next()
                        minuteIter = newIter
                        break
                    }
                }
                n
            }


        } ?: throw NoSuchElementException("No more minute.")
        // if (hasNextMinute()) {
        //     return minuteIter.next().also { lastMinute = it }
        // }
        // throw NoSuchElementException("No more minute.")
    }


    private fun nextSecond(): Int {
        return nextSecond?.also {
            // lastSecond = it
            nextSecond = if (secondIter.hasNext()) secondIter.next() else {
                var n: Int? = null
                while (hasNextMinute()) {
                    nextMinute()
                    val newIter = second.iterator()
                    if (newIter.hasNext()) {
                        n = newIter.next()
                        secondIter = newIter
                        break
                    }
                }
                n
            }

        } ?: throw NoSuchElementException("No more second.")

        // if (hasNextSecond()) {
        //     return secondIter.next()
        // }
        // throw NoSuchElementException("No more second.")
    }
    //endregion

    //region lastValue

    private fun lastMonth(): Int = lastMonth
    private fun lastDayOfMonth(): Int = lastDayOfMonth
    private fun lastHour(): Int = lastHour
    private fun lastMinute(): Int = lastMinute
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



