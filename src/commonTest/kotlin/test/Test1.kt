package test

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kron.*
import kotlin.test.Test


internal inline fun <reified T : Any?> T.println(needShow: (T) -> Any? = { it }): T = also { println(needShow(this)) }

/**
 *
 * @author ForteScarlet
 */
class Test1 {

    @Test
    fun test1() {
        val iter = DateTimeIter(
            dateTime(3),
            cronMonth(2, 3, 4),
            cronDayOfMonth(1, 2),
            cronHour(1),
            cronMinute(1),
            cronSecond(1),
        )

        println("")
        println("=====================")
        println("")

        while (iter.hasNext()) {
            val n = iter.next()
            n.toLocalDateTimeOrNull()?.println()
        }

    }

    @Test
    fun test2() {

        val cron = SimpleCron(
            RangedValue.Second(0..3),
            FixedValue.Minute(0),
            FixedValue.Hour(0),
            FixedValue.DayOfMonth(1),
            SteppedValue.Month(8, 9, 2)
        )


        println(cron.expression)

        var t = 1
        for (instant in cron.executor()) {

            println(instant.toLocalDateTime(TimeZone.currentSystemDefault()))

            if (t++ > 10) {
                break
            }

        }


    }

    @Test
    fun test3() {
        val cronValue = "0-3 0 0 1 5-9/2 *"
        val cron = resolveCron(cronValue)
        println(cron.expression)
        val executor = cron.executor()
        var i = 1
        for (instant in executor) {
            println(instant.toLocalDateTime(TimeZone.currentSystemDefault()))
            if (i++ > 10) {
                break
            }
        }

    }


}