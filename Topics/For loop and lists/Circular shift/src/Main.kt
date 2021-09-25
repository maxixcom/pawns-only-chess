fun main() {
    val numbers = MutableList(readLine()!!.toInt()) {
        readLine()!!.toInt()
    }
    val rotate = 1
    if (numbers.size > 1) {
        for (i in 1..rotate % numbers.size) {
            numbers.add(0, numbers.removeAt(numbers.lastIndex))
        }
    }

    println(numbers.joinToString(" "))
}
