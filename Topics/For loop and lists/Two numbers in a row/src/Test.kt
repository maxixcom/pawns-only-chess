fun main() {
    val numbers = List(11) { it }

    numbers.windowed(2, 2, true).forEach{
        println(it)
    }
}
