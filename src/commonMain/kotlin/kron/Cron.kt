@file:Suppress("unused")

package kron

import kotlinx.datetime.*


/**
 * 一个 Cron 表达式.
 *
 * Cron表达式支持六个单位：(sec) (min) (hour) (day of month) (month) (week day)
 * 其中，Day of month 和 Day of week只能存在一个非 `*` 值。
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
 * @author ForteScarlet
 */
interface Cron : Iterable<Instant> {

    /** 秒的字面值 */
    val secondLiteral: String

    /** 分钟的字面值 */
    val minuteLiteral: String

    /** 小时的字面值 */
    val hourLiteral: String

    /** 日的字面值 */
    val dayOfMonthLiteral: String

    /** 月的字面值 */
    val monthLiteral: String

    /** 周的字面值 */
    val dayOfWeekLiteral: String

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
    fun executor(startTime: Instant): Executor

    override fun iterator(): Executor = executor(Clock.System.now())

    /**
     * Cron表达式中针对于各位置的值的封装.
     *
     * @see AnyValue
     *
     */
    sealed interface Value {
        /** 这个值的字面量 */
        val literal: String

        /**
         * 这个值的类型。
         */
        val type: ValueType
    }



    /**
     * Cron表达式执行器
     */
    interface Executor : Iterator<Instant> {
        val startTime: Instant
        override fun next(): Instant
        override fun hasNext(): Boolean
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
    /* of week, of month */
    DAY,
    MONTH
}