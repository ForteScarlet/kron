package test

import java.time.Year

actual val javaVersion: String? get() = System.getProperty("java.version")

/*
    占位文件
 */
class JvmTest {


    fun test() {

    }

}

class Test2(name: String) {
    fun run() = 1


}

fun main() {
    val test2: Test2? = null
    val n: Int = test2?.run() ?: 2

}
