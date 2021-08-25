@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")

package kron

import kotlinx.datetime.number
import kotlinx.datetime.DayOfWeek as X_Week
import kotlinx.datetime.Month as X_Month


sealed class BaseCronValue(private val valueTypeName: String) : Cron.Value {
    override fun toString(): String = "${valueTypeName}Value(type=$type, literal=$literal)"
}

sealed class AnyValue(override val type: ValueType, val iterRange: IntRange) : BaseCronValue("Any") {
    override val literal: String get() = "*"
    override fun iterator(): Iterator<Int> = iterRange.iterator()

    /**
     * [AnyValue] 中包含范围内的全部元素
     */
    override fun contains(value: Int): Boolean = value in iterRange


    object Second : AnyValue(ValueType.SECOND, 0..59), Cron.Value.Second
    object Minute : AnyValue(ValueType.MINUTE, 0..59), Cron.Value.Minute
    object Hour : AnyValue(ValueType.HOUR, 0..23), Cron.Value.Hour
    object DayOfMonth : AnyValue(ValueType.DAY, 1..31), Cron.Value.Day.OfMonth
    object DayOfWeek : AnyValue(ValueType.DAY, 0..6), Cron.Value.Day.OfWeek
    object Month : AnyValue(ValueType.MONTH, 1..12), Cron.Value.Month
}


/**
 * 固定值的 [Cron.Value].
 */
sealed class FixedValue(override val type: ValueType, val value: Int) : BaseCronValue("Fixed") {
    override val literal: String get() = value.toString()
    override fun iterator(): Iterator<Int> = iterator { yield(value) }

    override fun contains(value: Int): Boolean = value == this.value

    class Second(value: Int) : FixedValue(ValueType.SECOND, value), Cron.Value.Second {
        init {
            // Required value.
            require(value in 0..59) { "Fixed second value must in 0..59, but $value" }
        }
    }

    class Minute(value: Int) : FixedValue(ValueType.MINUTE, value), Cron.Value.Minute {
        init {
            // Required value.
            require(value in 0..59) { "Fixed minute value must in 0..59, but $value" }
        }
    }

    class Hour(value: Int) : FixedValue(ValueType.HOUR, value), Cron.Value.Hour {
        init {
            // Required value.
            require(value in 0..23) { "Fixed hour value must in 0..23, but $value" }
        }
    }

    class DayOfMonth(value: Int) : FixedValue(ValueType.DAY, value), Cron.Value.Day.OfMonth {
        init {
            // Required value.
            require(value in 1..31) { "Fixed day of month value must in 1..31, but $value" }
        }
    }

    // Names can also be used for the 'month' and 'day of week' fields. Use the first three letters of the particular day or month (case does not matter).
    // Ranges or lists of names are not allowed.
    open class DayOfWeek(value: Int) : FixedValue(ValueType.DAY, value), Cron.Value.Day.OfWeek {
        constructor(value: String) : this(when (value.uppercase()) {
            "MON" -> 0
            "TUE" -> 1
            "WED" -> 2
            "THU" -> 3
            "FRI" -> 4
            "SAT" -> 5
            "SUN" -> 6
            else -> throw IllegalArgumentException("Unknown week '$value'")
        })

        val dayOfWeek: X_Week

        init {
            // Required value.
            // 7: Sunday, but non-standard.
            // 0-7 (0 or 7 is Sunday, or use names)
            require(value in 0..7) { "Fixed day of week value must in 0..6, but $value." }
            dayOfWeek = if (value == 7) X_Week.SUNDAY else X_Week(value + 1)
        }
    }

    // Names can also be used for the 'month' and 'day of week' fields. Use the first three letters of the particular day or month (case does not matter).
    // Ranges or lists of names are not allowed.
    class Month : FixedValue, Cron.Value.Month {
        constructor(value: Int) : super(ValueType.MONTH, value) {
            // Required value.
            require(value in 1..12) { "Fixed month value must in 1..12, but $value" }
            month = X_Month(value)
        }

        constructor(month: X_Month) : super(ValueType.MONTH, month.number) {
            this.month = month
        }

        constructor(value: String) : this(when (value.uppercase()) {
            "JAN" -> 1
            "FEB" -> 2
            "MAR" -> 3
            "APR" -> 4
            "MAY" -> 5
            "JUN" -> 6
            "JUL" -> 7
            "AUG" -> 8
            "SEP" -> 9
            "OCT" -> 10
            "NOV" -> 11
            "DEC" -> 12
            else -> throw IllegalArgumentException("Unknown month '$value'")
        })

        val month: X_Month
    }
}


/**
 * 区间值的 [Cron.Value]
 */
sealed class RangedValue(override val type: ValueType, val range: IntProgression) :
    BaseCronValue("Ranged"),
    Iterable<Int> by range {
    override val literal: String = "${range.first}-${range.last}"


    constructor(type: ValueType, start: Int, endInclusive: Int) : this(type, start..endInclusive)

    override fun contains(value: Int): Boolean = value in range

    class Second : RangedValue, Cron.Value.Second {
        constructor(range: IntRange) : super(ValueType.SECOND, range) {
            require(range.first >= 0 && range.last <= 59) { "Second range must in 0..59, but $range" }
        }

        constructor(start: Int, endInclusive: Int) : this(start..endInclusive)
    }

    class Minute : RangedValue, Cron.Value.Minute {
        constructor(range: IntRange) : super(ValueType.MINUTE, range) {
            require(range.first >= 0 && range.last <= 59) { "Minute range must in 0..59, but $range" }
        }

        constructor(start: Int, endInclusive: Int) : this(start..endInclusive)
    }

    class Hour : RangedValue, Cron.Value.Hour {
        constructor(range: IntRange) : super(ValueType.HOUR, range) {
            require(range.first >= 0 && range.last <= 23) { "Hour range must in 0..23, but $range" }
        }

        constructor(start: Int, endInclusive: Int) : this(start..endInclusive)
    }

    class DayOfMonth : RangedValue, Cron.Value.Day.OfMonth {
        constructor(range: IntRange) : super(ValueType.DAY, range) {
            require(range.first >= 1 && range.last <= 31) { "DayOfMonth range must in 1..31, but $range" }
        }

        constructor(start: Int, endInclusive: Int) : this(start..endInclusive)
    }

    class DayOfWeek : RangedValue, Cron.Value.Day.OfWeek {
        constructor(range: IntRange) : super(ValueType.DAY, range) {
            require(range.first >= 0 && range.last <= 6) { "DayOfWeek range must in 0..6, but $range" }
        }

        constructor(start: Int, endInclusive: Int) : this(start..endInclusive)
    }

    class Month : RangedValue, Cron.Value.Month {
        constructor(range: IntRange) : super(ValueType.MONTH, range) {
            require(range.first >= 1 && range.last <= 12) { "Month range must in 1..12, but $range" }
        }

        constructor(start: Int, endInclusive: Int) : this(start..endInclusive)
    }
}

/**
 * 步长数值.
 *
 * > Step values can be used in conjunction with ranges.
 *
 * > Following a range with "/<number>" specifies skips of the number's value through the range.
 *
 * > For example,
 * > "0-23/2" can be used in the 'hours' field to specify command execution for every other hour
 * > (the alternative in the V7 standard is "0,:2,:4,:6,:8,:10,:12,:14,:16,:18,:20,:22").
 *
 */
// TODO
//  Step values are also permitted after an asterisk, so if specifying a job to be run every two hours, you can use "*/2".

sealed class SteppedValue(
    override val type: ValueType,
    val base: Int,
    val to: Int?,
    max: Int,
    val step: Int,
) : RangedValue(type, if (step == 1) base..(to ?: max) else base..(to ?: max) step step) {
    override val literal: String = if (to != null) "$base-$to/$step" else "$base/$step"
    override fun iterator(): Iterator<Int> = range.iterator()

    class Second(base: Int, to: Int? = null, step: Int = 1) : SteppedValue(ValueType.SECOND, base, to, 59, step),
        Cron.Value.Second {
        init {
            require(base in 0..59 && (to == null || to in (base + 1)..59)) { "Second range must in 0..59, but $base .. ${to ?: "59"}" }
        }
    }

    class Minute(base: Int, to: Int? = null, step: Int = 1) : SteppedValue(ValueType.MINUTE, base, to, 59, step),
        Cron.Value.Minute {
        init {
            require(base in 0..59 && (to == null || to in (base + 1)..59)) { "Minute range must in 0..59, but $base .. ${to ?: "59"}" }
        }
    }

    class Hour(base: Int, to: Int? = null, step: Int = 1) : SteppedValue(ValueType.HOUR, base, to, 23, step),
        Cron.Value.Hour {
        init {
            require(base in 0..23 && (to == null || to in (base + 1)..23)) { "Hour range must in 0..23, but $base .. ${to ?: "23"}" }
        }
    }

    class DayOfMonth(base: Int, to: Int? = null, step: Int = 1) : SteppedValue(ValueType.DAY, base, to, 31, step),
        Cron.Value.Day.OfMonth {
        init {
            require(base in 1..31 && (to == null || to in (base + 1)..31)) { "DayOfMonth range must in 1..31, but $base .. ${to ?: "31"}" }
        }
    }

    class DayOfWeek(base: Int, to: Int? = null, step: Int = 1) : SteppedValue(ValueType.DAY, base, to, 6, step),
        Cron.Value.Day.OfWeek {
        init {
            require(base in 0..6 && (to == null || to in (base + 1)..6)) { "DayOfWeek range must in 0..6, but $base .. ${to ?: "6"}" }
        }
    }

    class Month(base: Int, to: Int? = null, step: Int = 1) : SteppedValue(ValueType.MONTH, base, to, 12, step),
        Cron.Value.Month {
        init {
            require(base in 1..12 && (to == null || to in (base + 1)..12)) { "Month range must in 1..12, but $base .. ${to ?: "12"}" }
        }
    }
}

/**
 * 列表数据。
 * @param values values中的元素值仅允许两个类型: [Int] 或 [IntRange].
 */
sealed class ListValue(override val type: ValueType, private val values: List<Any>) : BaseCronValue("List") {
    override val literal: String = values.joinToString(",") {
        when (it) {
            is Number -> it.toInt().toString()
            is IntRange -> "${it.first}-${it.last}"
            else -> throw IllegalArgumentException("Values only support type Int or IntRange.")
        }
    }

    private val valuesList: List<Int> = values.asSequence().flatMap {
        when (it) {
            is Number -> listOf(it.toInt())
            is IntRange -> it
            else -> throw IllegalArgumentException("Values only support type Int or IntRange.")
        }
    }.distinct().toList().sorted()


    // fun valuesCopyOf(): IntArray = values.copyOf()
    // operator fun get(index: Int): Int = values[index]

    override fun iterator(): Iterator<Int> = valuesList.iterator()

    override fun contains(value: Int): Boolean = value in valuesList

    class Second(values: List<Any>) : ListValue(ValueType.SECOND, values), Cron.Value.Second {
        init {
            values.checkValues(0, 59)
        }
    }

    class Minute(values: List<Any>) : ListValue(ValueType.MINUTE, values), Cron.Value.Minute {
        init {
            values.checkValues(0, 59)
        }
    }

    class Hour(values: List<Any>) : ListValue(ValueType.HOUR, values), Cron.Value.Hour {
        init {
            values.checkValues(0, 23)
        }
    }

    class DayOfMonth(values: List<Any>) : ListValue(ValueType.DAY, values), Cron.Value.Day.OfMonth {
        init {
            values.checkValues(1, 31)
        }
    }

    class DayOfWeek(values: List<Any>) : ListValue(ValueType.DAY, values), Cron.Value.Day.OfWeek {
        init {
            values.checkValues(0, 6)
        }
    }

    class Month(values: List<Any>) : ListValue(ValueType.MONTH, values), Cron.Value.Month {
        init {
            values.checkValues(1, 12)
        }
    }
}

/**
 * Check values for [ListValue].
 */
internal inline fun List<Any>.checkValues(min: Int, max: Int) {
    for (value in this) {
        when (value) {
            is Number -> value.toInt().also { require(it in min..max) { "Value must in $min..$max, but $value" } }
            is IntRange -> require(value.first >= min && value.last <= max) { "Value range must in $min..$max, but $value" }
            else -> throw IllegalArgumentException("Values only support type Int or IntRange.")
        }
    }
}



