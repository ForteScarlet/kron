package kron.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * 获取当前年份
 */
actual fun nowYear(): Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year