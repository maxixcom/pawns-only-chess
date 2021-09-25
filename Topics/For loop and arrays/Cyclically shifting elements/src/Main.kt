import java.util.LinkedList

fun main() {
    // write your code here
    val total = readLine()!!.toInt()
    val input = LinkedList<Int>()
    for (i in 1..total) {
        input.add(readLine()!!.toInt())
    }
    input.push(input.pollLast())

    println(input.joinToString(" "))
}
