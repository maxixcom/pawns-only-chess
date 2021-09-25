fun main() {
    val input = readLine()!!.toInt()
    val isDividedBy: (Int, Int) -> Unit = { value, m ->
        if (value % m == 0) {
            println("Divided by $m")
        }
    }
    listOf(2, 3, 5, 6).forEach { isDividedBy(input, it) }
}
