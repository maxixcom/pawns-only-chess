fun main() {
    println(
        readLine()!!.replace(Regex("(.)\\1*")) {
            "${it.groupValues[1]}${it.value.length}"
        }
    )
}
