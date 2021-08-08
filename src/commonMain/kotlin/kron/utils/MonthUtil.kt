package kron.utils

import kotlinx.datetime.Month
import kotlinx.datetime.number
import kotlin.jvm.JvmInline


@Suppress("MemberVisibilityCanBePrivate")
@JvmInline
internal value class Year(val value: Int) {
    val isLeap: Boolean get() = value % 400 == 0 || (value % 4 == 0 && value % 100 != 0)
}




internal val biggerMonths = setOf(1,3,5,7,8,10,12)

val Month.standardMaxDay: Int get() = maxDay(false)

fun Month.maxDay(year: Int): Int = maxDay(Year(year).isLeap)

fun Month.maxDay(leap: Boolean): Int {
    if (this == Month.FEBRUARY) {
        return if (leap) 29 else 28
    }
    return if (this.number in biggerMonths) 31 else 30
}