fun main() {
    val nums = List(readLine()!!.toInt()) { readLine()!!.toInt() }
    val test = readLine()!!.toInt()

    var count = 0
    for (i in nums) {
        if (i == test) {
            count++
        }
    }
    println(count)
}
