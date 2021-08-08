@file:Suppress("unused")

package kron


//region second
fun cronSecond(): Cron.Value.Second = AnyValue.Second
fun cronSecond(value: Int): Cron.Value.Second = FixedValue.Second(value)
fun cronSecond(valueRange: IntRange): Cron.Value.Second = RangedValue.Second(valueRange)
fun cronSecond(valueProgression: IntProgression): Cron.Value.Second {
    val step = valueProgression.step
    return if (step == 1) cronSecond(valueProgression.first..valueProgression.last) else
        SteppedValue.Second(
            valueProgression.first,
            valueProgression.last,
            step)
}

fun cronSecond(vararg values: Int): Cron.Value.Second = ListValue.Second(values.asList())
fun cronSecond(vararg valueRanges: IntRange): Cron.Value.Second = ListValue.Second(valueRanges.asList())
//endregion


//region minute
fun cronMinute(): Cron.Value.Minute = AnyValue.Minute
fun cronMinute(value: Int): Cron.Value.Minute = FixedValue.Minute(value)
fun cronMinute(valueRange: IntRange): Cron.Value.Minute = RangedValue.Minute(valueRange)
fun cronMinute(valueProgression: IntProgression): Cron.Value.Minute {
    val step = valueProgression.step
    return if (step == 1) cronMinute(valueProgression.first..valueProgression.last) else
        SteppedValue.Minute(
            valueProgression.first,
            valueProgression.last,
            step)
}

fun cronMinute(vararg values: Int): Cron.Value.Minute = ListValue.Minute(values.asList())
fun cronMinute(vararg valueRanges: IntRange): Cron.Value.Minute = ListValue.Minute(valueRanges.asList())
//endregion

//region hour
fun cronHour(): Cron.Value.Hour = AnyValue.Hour
fun cronHour(value: Int): Cron.Value.Hour = FixedValue.Hour(value)
fun cronHour(valueRange: IntRange): Cron.Value.Hour = RangedValue.Hour(valueRange)
fun cronHour(valueProgression: IntProgression): Cron.Value.Hour {
    val step = valueProgression.step
    return if (step == 1) cronHour(valueProgression.first..valueProgression.last) else
        SteppedValue.Hour(
            valueProgression.first,
            valueProgression.last,
            step)
}

fun cronHour(vararg values: Int): Cron.Value.Hour = ListValue.Hour(values.asList())
fun cronHour(vararg valueRanges: IntRange): Cron.Value.Hour = ListValue.Hour(valueRanges.asList())
//endregion

//region day of month
fun cronDayOfMonth(): Cron.Value.Day.OfMonth = AnyValue.DayOfMonth
fun cronDayOfMonth(value: Int): Cron.Value.Day.OfMonth = FixedValue.DayOfMonth(value)
fun cronDayOfMonth(valueRange: IntRange): Cron.Value.Day.OfMonth = RangedValue.DayOfMonth(valueRange)
fun cronDayOfMonth(valueProgression: IntProgression): Cron.Value.Day.OfMonth {
    val step = valueProgression.step
    return if (step == 1) cronDayOfMonth(valueProgression.first..valueProgression.last) else
        SteppedValue.DayOfMonth(
            valueProgression.first,
            valueProgression.last,
            step)
}

fun cronDayOfMonth(vararg values: Int): Cron.Value.Day.OfMonth = ListValue.DayOfMonth(values.asList())
fun cronDayOfMonth(vararg valueRanges: IntRange): Cron.Value.Day.OfMonth = ListValue.DayOfMonth(valueRanges.asList())
//endregion

//region day of week
fun cronDayOfWeek(): Cron.Value.Day.OfWeek = AnyValue.DayOfWeek
fun cronDayOfWeek(value: Int): Cron.Value.Day.OfWeek = FixedValue.DayOfWeek(value)
fun cronDayOfWeek(valueRange: IntRange): Cron.Value.Day.OfWeek = RangedValue.DayOfWeek(valueRange)
fun cronDayOfWeek(valueProgression: IntProgression): Cron.Value.Day.OfWeek {
    val step = valueProgression.step
    return if (step == 1) cronDayOfWeek(valueProgression.first..valueProgression.last) else
        SteppedValue.DayOfWeek(
            valueProgression.first,
            valueProgression.last,
            step)
}

fun cronDayOfWeek(vararg values: Int): Cron.Value.Day.OfWeek = ListValue.DayOfWeek(values.asList())
fun cronDayOfWeek(vararg valueRanges: IntRange): Cron.Value.Day.OfWeek = ListValue.DayOfWeek(valueRanges.asList())
//endregion

//region day of week
fun cronMonth(): Cron.Value.Month = AnyValue.Month
fun cronMonth(value: Int): Cron.Value.Month = FixedValue.Month(value)
fun cronMonth(valueRange: IntRange): Cron.Value.Month = RangedValue.Month(valueRange)
fun cronMonth(valueProgression: IntProgression): Cron.Value.Month {
    val step = valueProgression.step
    return if (step == 1) cronMonth(valueProgression.first..valueProgression.last) else
        SteppedValue.Month(
            valueProgression.first,
            valueProgression.last,
            step)
}

fun cronMonth(vararg values: Int): Cron.Value.Month = ListValue.Month(values.asList())
fun cronMonth(vararg valueRanges: IntRange): Cron.Value.Month = ListValue.Month(valueRanges.asList())
//endregion