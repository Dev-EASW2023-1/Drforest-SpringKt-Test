package kr.easw.drforestspringkt

import kr.easw.drforestspringkt.util.positive

class DrforestSpringKtApplicationTests {
//
//    @Test
//    fun contextLoads() {
//    }


}

fun main() {
    val data = 0 positive 1
    println("Data: $data / ${data.toString(2)}")
    println("Positive Check")
    println(checkFlag(data, 0))
    println(checkFlag(data, 1))
    println(checkFlag(data, 2))
    println(checkFlag(data, 3))
    println(checkFlag(data, 7))
}

private infix fun Int.positive(flag: Int): Int {
    return (this or (1 shl flag))
}


private infix fun Int.negative(flag: Int): Int {
    return (this and (1 shl flag).inv())
}

private fun checkFlag(origin: Int, flagId: Int): Boolean {
    println("Checking ${(flagId)}")
    return (origin and (1 shl flagId)) != 0
}
