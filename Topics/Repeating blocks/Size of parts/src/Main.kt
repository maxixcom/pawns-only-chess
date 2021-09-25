fun main() {
    // put your code here
    val total = readLine()!!.toInt()
    val input = (1..total)
        .map {
            readLine()!!.toInt()
        }
        .groupingBy { it }.eachCount()

    println("${input[0] ?: 0} ${input[1] ?: 0} ${input[-1] ?: 0}")
}
