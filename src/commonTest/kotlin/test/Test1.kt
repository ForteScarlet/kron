package test

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kron.*
import kron.utils.asPreview
import kotlin.test.Test


internal inline fun <reified T: Any?> T.println(needShow: (T) -> Any? = { it }): T = also { println(needShow(this)) }


/**
 *
 * @author ForteScarlet
 */
class Test1 {

    @Test
    fun test1() {
        val iter = DateTimeIter(
            DateTime(3),
            cronMonth(2, 3, 4),
            cronDayOfMonth(1, 2),
            cronHour(1),
            cronMinute(1),
            cronSecond(1),
            // cronDayOfWeek(),
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
            RangedValue.Second(5 .. 7),
            FixedValue.Minute(0),
            FixedValue.Hour(0),
            FixedValue.DayOfMonth(1),
            ListValue.Month(listOf(1, 3, 5))
        )

        var t = 1
        for (instant in cron.executor()) {

            println(instant.toLocalDateTime(TimeZone.currentSystemDefault()))

            if (t++ > 10) {
                break
            }

        }



    }


}