package kron.utils

import kotlin.js.Date

/**
 * 获取当前年份
 */
public actual fun nowYear(): Int = Date().getFullYear()