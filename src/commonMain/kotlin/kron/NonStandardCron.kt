package kron

import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.number

/*
 @yearly : Run once a year, ie. "0 0 1 1 *".
 @annually : Run once a year, ie. "0 0 1 1 *".
 @monthly : Run once a month, ie. "0 0 1 * *".
 @weekly : Run once a week, ie. "0 0 * * 0".
 @daily : Run once a day, ie. "0 0 * * *".
 @hourly : Run once an hour, ie. "0 * * * *".
 */

open class Yearly(m: Month, d: Int, h: Int, min: Int, s: Int) : Cron {
    override val expression: String = "$s $min $h $d ${m.number} *"

    override val second: Cron.Value = FixedValue.Second(s)
    override val minute: Cron.Value = FixedValue.Minute(min)
    override val hour: Cron.Value = FixedValue.Hour(h)
    override val dayOfMonth: Cron.Value = FixedValue.DayOfMonth(d)
    override val month: Cron.Value = FixedValue.Month(m)
    override val dayOfWeek: Cron.Value get() = AnyValue.DayOfWeek

    override fun contains(epochMilliseconds: Instant): Boolean {
        TODO("Not yet implemented")
    }

    override fun executor(startTime: Instant): Cron.Executor {
        TODO("Not yet implemented")
    }

    companion object : Yearly(Month.JANUARY, 1, 0, 0, 0)
}


open class Annually(m: Month, d: Int, h: Int, min: Int, s: Int) :  Yearly(m, d, h, min, s) {
    companion object : Annually(Month.JANUARY, 1, 0, 0, 0)
}