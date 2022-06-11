package kr.easw.drforestspringkt.util


infix fun Int.positive(flag: Int): Int {
    return (this or (1 shl flag))
}


infix fun Int.negative(flag: Int): Int {
    return (this and (1 shl flag).inv())
}

fun checkFlag(origin: Int, flagId: Int): Boolean {
    return (origin and (1 shl flagId)) != 0
}
