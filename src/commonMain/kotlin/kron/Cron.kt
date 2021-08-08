@file:Suppress("unused")

package kron

import kotlinx.datetime.*


/**
 * 一个 Cron 表达式.
 *
 * Cron表达式支持六个单位：(sec) (min) (hour) (day of month) (month) (week day)
 *
 * ### Second (0~59)
 *      - value: second value. .e.g: `0`
 *      - `*`: Any value
 *      - `,`: value list separator. .e.g: `1,2,3`
 *      - `-`: range values. .e.g: `0-5`
 *      - `/`: step values. .e.g: `0/2`
 *
 * ### Minute (0~59)
 *      - value: second value. .e.g: `0`
 *      - `*`: Any value
 *      - `,`: value list separator. .e.g: `1,2,3`
 *      - `-`: range values. .e.g: `0-5`
 *      - `/`: step values. .e.g: `0/2`
 *
 * ### Hour (0~23)
 *      - value: second value. .e.g: `0`
 *      - `*`: Any value
 *      - `,`: value list separator. .e.g: `1,2,3`
 *      - `-`: range values. .e.g: `0-5`
 *      - `/`: step values. .e.g: `0/2`
 *
 * ### Day of month (DAY; 1~31)
 *      - value: second value. .e.g: `0`
 *      - `*`: Any value
 *      - `,`: value list separator. .e.g: `1,2,3`
 *      - `-`: range values. .e.g: `0-5`
 *      - `/`: step values. .e.g: `0/2`
 *
 * ### Month (1~12, JAN~DEC)
 *      - `*`: Any value
 *      - `,`: value list separator. .e.g: `1,2,3`
 *      - `-`: range values. .e.g: `0-5`
 *      - `/`: step values. .e.g: `0/2`
 *
 *
 * ### Day of week(DAY; 0~6, SUN-SAT)
 *      - `*`: Any value
 *      - `,`: value list separator. .e.g: `1,2,3`
 *      - `-`: range values. .e.g: `0-5`
 *      - `/`: step values. .e.g: `0/2`
 *
 * - \@reboot    : Run once after reboot.
 * - \@yearly    : Run once a year, ie.  "0 0 1 1 *".
 * - \@annually  : Run once a year, ie.  "0 0 1 1 *".
 * - \@monthly   : Run once a month, ie. "0 0 1 * *".
 * - \@weekly    : Run once a week, ie.  "0 0 * * 0".
 * - \@daily     : Run once a day, ie.   "0 0 * * *".
 * - \@hourly    : Run once an hour, ie. "0 * * * *".
 *
 * @author ForteScarlet
 */
interface Cron : Iterable<Instant> {
    /**
     * 这个表达式的实际字符串。
     */
    val expression: String

    /** 秒的字面值 */
    val secondLiteral: String get() = second.literal

    /** 分钟的字面值 */
    val minuteLiteral: String get() = minute.literal

    /** 小时的字面值 */
    val hourLiteral: String get() = hour.literal

    /** 日的字面值 */
    val dayOfMonthLiteral: String get() = dayOfMonth.literal

    /** 月的字面值 */
    val monthLiteral: String get() = month.literal

    /** 周的字面值 */
    val dayOfWeekLiteral: String get() = dayOfWeek.literal

    /** 秒的值 */
    val second: Value

    /** 分钟的值 */
    val minute: Value

    /** 小时的值 */
    val hour: Value

    /** 日的值 */
    val dayOfMonth: Value

    /** 月的值 */
    val month: Value

    /** 周的值 */
    val dayOfWeek: Value

    operator fun contains(epochMilliseconds: Instant): Boolean

    /**
     * 获取执行器
     */
    fun executor(
        startTime: Instant = Clock.System.now(),
        endTime: Instant? = null,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): Executor

    override fun iterator(): Executor = executor(Clock.System.now())

    /**
     * Cron表达式中针对于各位置表达式的描述.
     *
     * [Value] 主要分为几种类型：[AnyValue]、[FixedValue]、[RangedValue]、[SteppedValue]、[ListValue]
     *
     * 关于Cron表达式的值的定义，参考的文章为 [crontab.guru](https://crontab.guru/crontab.5.html)
     *
     * @see AnyValue
     * @see FixedValue
     * @see RangedValue
     * @see SteppedValue
     * @see ListValue
     *
     */
    interface Value : Iterable<Int> {
        companion object

        /** 这个值的字面量 */
        val literal: String

        /**
         * 这个值的类型。
         */
        val type: ValueType

        /**
         * 每个 [Value] 都会有一段数值区间可以获取。
         */
        override fun iterator(): Iterator<Int>

        interface Second : Value {
            companion object {
                val RANGE: IntRange = 0..59
            }
        }

        interface Minute : Value {
            companion object {
                val RANGE: IntRange = 0..59
            }
        }

        interface Hour : Value {
            companion object {
                val RANGE: IntRange = 0..23
            }
        }

        interface Day : Value {
            interface OfMonth : Day {
                companion object {
                    val RANGE: IntRange = 1..31
                }
            }

            interface OfWeek : Day {
                companion object {
                    val RANGE: IntRange = 0..6
                }
            }
        }



        interface Month : Value {
            companion object {
                val RANGE: IntRange = 1..12
            }
        }

    }


    /**
     * Cron表达式执行器
     */
    interface Executor : Iterator<Instant> {
        val startTime: Instant
        val endTime: Instant?
        override fun next(): Instant
        override fun hasNext(): Boolean
    }

}

val Cron.values: Array<Cron.Value>
    get() = arrayOf(second, minute, hour, dayOfMonth, month, dayOfWeek)

// exclude
fun Cron.expression(vararg excludes: ValueType): String {
    if (excludes.isEmpty()) {
        return expression
    }

    val vs = values

    if (excludes.size == 1) {
        val exclude = excludes.first()
        return buildString {
            var first = true
            vs.forEach { v ->
                if (v.type != exclude) {
                    if (!first) {
                        append(' ')
                    } else {
                        first = false
                    }
                    append(v.literal)
                }
            }
        }
    }

    val excludeSet = excludes.toSet()
    if (excludeSet.size == ValueType.values().size) {
        return ""
    }

    return buildString {
        var first = true
        vs.forEach { v ->
            if (v.type !in excludeSet) {
                if (!first) {
                    append(' ')
                } else {
                    first = false
                }
                append(v.literal)
            }
        }
    }


}


operator fun Cron.contains(epochMilliseconds: Long): Boolean = Instant.fromEpochMilliseconds(epochMilliseconds) in this

fun Cron.contains(localDateTime: LocalDateTime, timeZone: TimeZone): Boolean = localDateTime.toInstant(timeZone) in this
operator fun Cron.contains(localDateTime: LocalDateTime): Boolean =
    contains(localDateTime, TimeZone.currentSystemDefault())


enum class ValueType {
    SECOND,
    MINUTE,
    HOUR,

    /* Of week, or of month */
    // TODO
    //  Note: The day of a command's execution can be specified in the following two fields --- 'day of month', and 'day of week'.
    //  If both fields are restricted (i.e., do not contain the "*" character), the command will be run when either field matches the current time.
    //  For example,
    //  "30 4 1,15 * 5" would cause a command to be run at 4:30 am on the 1st and 15th of each month, plus every Friday.
    DAY,
    MONTH;


}


object AllAnyValueCron : Cron {
    override val expression: String get() = "* * * * * *"

    override val second: Cron.Value get() = AnyValue.Second
    override val minute: Cron.Value get() = AnyValue.Minute
    override val hour: Cron.Value get() = AnyValue.Hour
    override val dayOfMonth: Cron.Value get() = AnyValue.DayOfMonth
    override val month: Cron.Value get() = AnyValue.Month
    override val dayOfWeek: Cron.Value get() = AnyValue.DayOfWeek

    override fun contains(epochMilliseconds: Instant): Boolean = true


    override fun executor(startTime: Instant, endTime: Instant?, timeZone: TimeZone): Cron.Executor =
        endTime?.let { e -> Executor(startTime, e) } ?: ExecutorForEver(startTime)


    private class Executor(override val startTime: Instant, override val endTime: Instant) : Cron.Executor {
        private var next: Instant? = startTime
        override fun next(): Instant {
            val n = next
            if (n == null) {
                throw NoSuchElementException("No more element.")
            } else {
                return n.also {
                    val np1 = n.plus(1, DateTimeUnit.SECOND)
                    next = if (np1 > endTime) {
                        null
                    } else {
                        np1
                    }
                }
            }
        }

        override fun hasNext(): Boolean = next != null
    }

    private class ExecutorForEver(override val startTime: Instant) : Cron.Executor {
        private var next: Instant = startTime
        override val endTime: Instant? get() = null
        override fun next(): Instant = next.also { next = next.plus(1, DateTimeUnit.SECOND) }
        override fun hasNext(): Boolean = true
    }
}
