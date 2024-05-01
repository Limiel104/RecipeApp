@file:JvmName("Log")

package android.util

fun i(tag: String, msg: String): Int {
    println("INFO: $tag: $msg")
    return 0
}