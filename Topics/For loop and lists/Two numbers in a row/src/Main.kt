fun main() {
    val numbers = List(readLine()!!.toInt()) {
        readLine()!!.toInt()
    }
    val testSequence = readLine()!!.split("\\s+".toRegex()).map(String::toInt)

    for (i in numbers.indices) {
        if (i + 1 == numbers.size) {
            break
        }
        if (
            numbers[i] == testSequence[0] && numbers[i + 1] == testSequence[1] ||
            numbers[i] == testSequence[1] && numbers[i + 1] == testSequence[0]
        ) {
            print("NO")
            return
        }
    }
    print("YES")
}
