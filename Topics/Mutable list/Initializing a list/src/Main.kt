fun main() {
    val numbers = MutableList(100) {
        when (it) {
            0 -> 1
            9 -> 10
            99 -> 100
            else -> 0
        }
    }

    // do not touch the lines below 
    println(numbers.joinToString())
}
