package kron.utils

import java.time.Year

/**
 * 获取当前年份
 */
actual fun nowYear(): Int = Year.now().value