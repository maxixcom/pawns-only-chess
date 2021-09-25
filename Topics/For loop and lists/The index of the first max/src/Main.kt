fun main() {
    // write your code here
    val numbers = List(readLine()!!.toInt()) {
        readLine()!!.toInt()
    }
    var maxValue = numbers[0]
    var maxIndex = 0
    for (i in numbers.indices) {
        if (numbers[i] > maxValue) {
            maxValue = numbers[i]
            maxIndex = i
        }
    }
    println(maxIndex)
}
