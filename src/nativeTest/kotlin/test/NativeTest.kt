package test

import kron.utils.nowYear
import platform.posix.clock
import platform.posix.sleep
import platform.posix.time
import kotlin.test.Test


actual val javaVersion: String? get() = null

class NativeTest {
    @Test
    fun timeTest() {
        println(nowYear())
    }
}
