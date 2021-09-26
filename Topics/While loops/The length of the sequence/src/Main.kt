fun main() {
    val numbers = mutableListOf<Int>()
    do {
        numbers.add(readLine()!!.toInt())
    } while (numbers.last() != 0)
    println(numbers.size - 1)
}
