@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")

package kron

import kotlinx.datetime.DayOfWeek as X_Week
import kotlinx.datetime.Month as X_Month

sealed class BaseCronValue(private val valueTypeName: String) : Cron.Value {
    override fun toString(): String = "${valueTypeName}Value(type=$type, literal=$literal)"
}

sealed class AnyValue(override val type: ValueType) : BaseCronValue("Any") {
    override val literal: String get() = "*"

    object Second : AnyValue(ValueType.SECOND)
    object Minute : AnyValue(ValueType.MINUTE)
    object Hour : AnyValue(ValueType.HOUR)
    object DayOfMonth : AnyValue(ValueType.DAY)
    object DayOfWeek : AnyValue(ValueType.DAY)
    object Month : AnyValue(ValueType.MONTH)
}


/**
 * 固定值的 [Cron.Value].
 */
sealed class FixedValue(override val type: ValueType, val value: Int) : BaseCronValue("Fixed") {
    override val literal: String get() = value.toString()

    class Second(value: Int) : FixedValue(ValueType.SECOND, value) {
        init {
            // Required value.
            require(value in 0..59) { "Fixed second value must in 0..59, but $value" }
        }
    }

    class Minute(value: Int) : FixedValue(ValueType.MINUTE, value) {
        init {
            // Required value.
            require(value in 0..59) { "Fixed minute value must in 0..59, but $value" }
        }
    }

    class Hour(value: Int) : FixedValue(ValueType.HOUR, value) {
        init {
            // Required value.
            require(value in 0..23) { "Fixed hour value must in 0..23, but $value" }
        }
    }

    class DayOfMonth(value: Int) : FixedValue(ValueType.DAY, value) {
        init {
            // Required value.
            require(value in 1..31) { "Fixed day of month value must in 1..31, but $value" }
        }
    }

    open class DayOfWeek(value: Int) : FixedValue(ValueType.DAY, value) {
        val dayOfWeek: X_Week

        init {
            // Required value.
            // 7: Sunday, but non-standard.
            require(value in 0..7) { "Fixed day of week value must in 0..6, but $value." }
            dayOfWeek = if (value == 7) X_Week.SUNDAY else X_Week(value + 1)
        }
    }

    class Month(value: Int) : FixedValue(ValueType.MONTH, value) {
        val month: X_Month

        init {
            // Required value.
            require(value in 1..12) { "Fixed month value must in 1..12, but $value" }
            month = X_Month(value)
        }
    }
}

/**
 * 区间值的 [Cron.Value]
 */
sealed class RangedValue(override val type: ValueType, val range: IntRange) :
    BaseCronValue("Ranged"),
    Iterable<Int> by range {
    override val literal: String = "${range.first}-${range.last}"
    constructor(type: ValueType, start: Int, endInclusive: Int): this(type, start..endInclusive)

    class Second : RangedValue {
        constructor(range: IntRange): super(ValueType.SECOND, range)
        constructor(start: Int, endInclusive: Int): super(ValueType.SECOND, start, endInclusive)
    }
    class Minute : RangedValue {
        constructor(range: IntRange): super(ValueType.MINUTE, range)
        constructor(start: Int, endInclusive: Int): super(ValueType.MINUTE, start, endInclusive)
    }
    class Hour : RangedValue {
        constructor(range: IntRange): super(ValueType.HOUR, range)
        constructor(start: Int, endInclusive: Int): super(ValueType.HOUR, start, endInclusive)
    }
    class DayOfMonth : RangedValue {
        constructor(range: IntRange): super(ValueType.DAY, range)
        constructor(start: Int, endInclusive: Int): super(ValueType.DAY, start, endInclusive)
    }
    class DayOfWeek : RangedValue {
        constructor(range: IntRange): super(ValueType.DAY, range)
        constructor(start: Int, endInclusive: Int): super(ValueType.DAY, start, endInclusive)
    }
    class Month : RangedValue {
        constructor(range: IntRange): super(ValueType.MONTH, range)
        constructor(start: Int, endInclusive: Int): super(ValueType.MONTH, start, endInclusive)
    }
}

/**
 * 步长数值
 */
sealed class SteppedValue(override val type: ValueType, val base: Int, val step: Int) : BaseCronValue("Stepped") {
    override val literal: String = "$base/$step"

    class Second(base: Int, step: Int) : SteppedValue(ValueType.SECOND, base, step) {
        init {

        }
    }

    class Minute(base: Int, step: Int) : SteppedValue(ValueType.MINUTE, base, step)
    class Hour(base: Int, step: Int) : SteppedValue(ValueType.HOUR, base, step)
    class DayOfMonth(base: Int, step: Int) : SteppedValue(ValueType.DAY, base, step)
    class DayOfWeek(base: Int, step: Int) : SteppedValue(ValueType.DAY, base, step)
    class Month(base: Int, step: Int) : SteppedValue(ValueType.MONTH, base, step)
}

/**
 * 列表数据。
 */
sealed class ListValue(override val type: ValueType, private val values: IntArray) : BaseCronValue("List") {
    override val literal: String = values.joinToString(",")
    fun valuesCopyOf() = values.copyOf()
    operator fun get(index: Int): Int = values[index]

    class Second(values: IntArray) : ListValue(ValueType.SECOND, values)
    class Minute(values: IntArray) : ListValue(ValueType.MINUTE, values)
    class Hour(values: IntArray) : ListValue(ValueType.HOUR, values)
    class DayOfMonth(values: IntArray) : ListValue(ValueType.DAY, values)
    class DayOfWeek(values: IntArray) : ListValue(ValueType.DAY, values)
    class Month(values: IntArray) : ListValue(ValueType.MONTH, values)
}
