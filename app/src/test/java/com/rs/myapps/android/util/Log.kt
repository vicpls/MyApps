package android.util

object Log {
    @JvmStatic
    fun d(tag: String?, msg: String?): Int {
        println("DEBUG: $tag: $msg")
        return 0
    }

    @JvmStatic
    fun v(tag: String?, msg: String?): Int {
        println("VERBOSE: $tag: $msg");
        return 0
    }
}
