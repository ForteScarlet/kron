package test

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
            2021,
            cronMonth(2, 3, 4),
            cronDayOfMonth(1, 2),
            cronDayOfWeek(),
            cronHour(1),
            cronMinute(1),
            cronSecond(1),
            DateTime(3)
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

        val iter = cronMonth(1..5).iterator().asPreview()
        println(iter.preNext)
        println(iter.preNext)

        iter.next().println { "next: $it" }
        println(iter.preNext)


    }


}