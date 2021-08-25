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
fun cronSecond(value: String): Cron.Value.Second =
    when (val resolvedValue = resolveValue(value)) {
        AnyResolvedValue -> AnyValue.Second
        is FixedResolvedValue -> FixedValue.Second(resolvedValue.value)
        is RangedResolvedValue -> RangedValue.Second(resolvedValue.value)
        is ListResolvedValue -> ListValue.Second(resolvedValue.value)
    }
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
fun cronMinute(value: String): Cron.Value.Minute =
    when (val resolvedValue = resolveValue(value)) {
        AnyResolvedValue -> AnyValue.Minute
        is FixedResolvedValue -> FixedValue.Minute(resolvedValue.value)
        is RangedResolvedValue -> RangedValue.Minute(resolvedValue.value)
        is ListResolvedValue -> ListValue.Minute(resolvedValue.value)
    }
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
fun cronHour(value: String): Cron.Value.Hour =
    when (val resolvedValue = resolveValue(value)) {
        AnyResolvedValue -> AnyValue.Hour
        is FixedResolvedValue -> FixedValue.Hour(resolvedValue.value)
        is RangedResolvedValue -> RangedValue.Hour(resolvedValue.value)
        is ListResolvedValue -> ListValue.Hour(resolvedValue.value)
    }
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
fun cronDayOfMonth(value: String): Cron.Value.Day.OfMonth =
    when (val resolvedValue = resolveValue(value)) {
        AnyResolvedValue -> AnyValue.DayOfMonth
        is FixedResolvedValue -> FixedValue.DayOfMonth(resolvedValue.value)
        is RangedResolvedValue -> RangedValue.DayOfMonth(resolvedValue.value)
        is ListResolvedValue -> ListValue.DayOfMonth(resolvedValue.value)
    }
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
fun cronDayOfWeek(value: String): Cron.Value.Day.OfWeek =
    when (val resolvedValue = resolveValue(value)) {
        AnyResolvedValue -> AnyValue.DayOfWeek
        is FixedResolvedValue -> FixedValue.DayOfWeek(resolvedValue.value)
        is RangedResolvedValue -> RangedValue.DayOfWeek(resolvedValue.value)
        is ListResolvedValue -> ListValue.DayOfWeek(resolvedValue.value)
    }
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
fun cronMonth(value: String): Cron.Value.Month =
    when (val resolvedValue = resolveValue(value)) {
        AnyResolvedValue -> AnyValue.Month
        is FixedResolvedValue -> FixedValue.Month(resolvedValue.value)
        is RangedResolvedValue -> RangedValue.Month(resolvedValue.value)
        is ListResolvedValue -> ListValue.Month(resolvedValue.value)
    }
//endregion


//region resolve cron
fun resolveCron(cron: String): Cron {
    val values = cron.split(Regex(" +"))
    check(values.size == 6) { "The number of elements in 'cron' must be 6, but ${values.size} " }
    return SimpleCron(
        cronSecond(values[0]),
        cronMinute(values[1]),
        cronHour(values[2]),
        cronDayOfMonth(values[3]),
        cronMonth(values[4]),
        cronDayOfWeek(values[5]),

        expression = cron
    )
}


//endregion


internal sealed interface ResolvedValue {
    val value: Any
}

internal object AnyResolvedValue : ResolvedValue {
    override val value: String get() = "*"
}

internal class FixedResolvedValue(override val value: Int) : ResolvedValue

internal class RangedResolvedValue(override val value: IntProgression) : ResolvedValue

internal class ListResolvedValue(override val value: List<Any>) : ResolvedValue

internal fun resolveValue(value: String): ResolvedValue {
    require(value.isNotBlank()) { "Value was blank." }

    // *
    // 数值
    // 多数值
    // 数值区间
    // 有步长的数值区间

    if (value == "*") return AnyResolvedValue
    val splitValues = value.split(',')
    if (splitValues.size == 1) {
        // Only one element
        val singleValue = splitValues[0]
        return if (singleValue.contains('-')) {
            // range
            val rangeValues = singleValue.split('-')
            check(rangeValues.size == 2) { "Range value only need 2, but ${rangeValues.size}: $rangeValues" }

            with(rangeValues.last()) {
                if (contains('/')) {
                    // has step
                    val stepSplit = split('/')
                    check(stepSplit.size == 2) { "Range Step value must only one, mut found ${stepSplit.size}: $stepSplit" }
                    RangedResolvedValue(rangeValues.first().toInt()..stepSplit.first().toInt() step stepSplit.last()
                        .toInt())
                } else {
                    // no step
                    RangedResolvedValue(rangeValues.first().toInt()..rangeValues.last().toInt())
                }
            }
        } else {
            val intValue = singleValue.toInt()
            FixedResolvedValue(intValue)
        }
    } else {
        // May elements
        val resolveList = splitValues.map { element ->
            if (element.contains('-')) {
                // range
                val elementRangeValues = element.split('-')
                check(elementRangeValues.size == 2) { "Range value only need 2, but ${elementRangeValues.size}: $elementRangeValues" }

                with(elementRangeValues.last()) {
                    if (contains('/')) {
                        // has step
                        val stepSplit = split('/')
                        check(stepSplit.size == 2) { "Range Step value must only one, mut found ${stepSplit.size}: $stepSplit" }
                        elementRangeValues.first().toInt()..stepSplit.first().toInt() step stepSplit.last().toInt()
                    } else {
                        // no step
                        elementRangeValues.first().toInt()..elementRangeValues.last().toInt()
                    }
                }
            } else element.toInt()

        }

        return ListResolvedValue(resolveList)
    }
}












