package kron



/**
 * 任意值，即 `*`
 */
object AnyValue : Cron.Value {
    override val literal: String get() = "*"
}

/**
 * 固定值的 [Cron.Value].
 */
class FixedValue(private val value: Int) : Cron.Value {
    override val literal: String get() = value.toString()
}

/**
 * 区间值的 [Cron.Value]
 */
class RangedValue(private val range: IntRange) : Cron.Value {
    override val literal: String = "${range.first}-${range.last}"
}

/**
 * 步长数值
 */
class SteppedValue(private val base: Int, private val step: Int): Cron.Value {
    override val literal: String = "$base/$step"
}

/**
 * 列表数据。
 */
class ListValue(private val values: IntArray): Cron.Value {
    override val literal: String = values.joinToString(",")
}