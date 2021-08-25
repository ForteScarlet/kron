package test

import kron.utils.nowYear
import platform.posix.clock
import platform.posix.sleep
import platform.posix.time
import kotlin.test.Test


class NativeTest {
    @Test
    fun timeTest() {
        println(nowYear())
    }
}
