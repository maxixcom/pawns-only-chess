fun main() {
    // write your code here
    val b1 = readDimensions()
    val b2 = readDimensions()

    val result = when (compare(b1, b2)) {
        CompareResult.Equal -> "Box 1 = Box 2"
        CompareResult.Greater -> "Box 1 > Box 2"
        CompareResult.Less -> "Box 1 < Box 2"
        CompareResult.Incomparable -> "Incomparable"
    }

    println(result)
}

fun readDimensions(): List<Int> {
    val dimensions = readLine()!!.split(" ")
        .map(String::toInt)
    if (dimensions.size != 3) {
        throw IllegalArgumentException("All boxes should have 3 dimensions")
    }
    return dimensions
}

fun compare(b1: List<Int>, b2: List<Int>): CompareResult {
    // Align boxes
    val first = b1.sorted()
    val second = b2.sorted()

    // b1 == b2 ?
    val isFirstEqualsToSecond = first.zip(second) { f, s -> f == s }.all { it == true }
    if (isFirstEqualsToSecond) {
        return CompareResult.Equal
    }

    // b1 < b2 ?
    val isFirstLestThanSecond = first.zip(second) { f, s -> f <= s }.all { it == true }
    if (isFirstLestThanSecond) {
        return CompareResult.Less
    }

    // b2 > b1 ?
    val isFirstGreaterThanSecond = first.zip(second) { f, s -> f >= s }.all { it == true }
    if (isFirstGreaterThanSecond) {
        return CompareResult.Greater
    }

    return CompareResult.Incomparable
}

sealed class CompareResult {
    object Less : CompareResult()
    object Greater : CompareResult()
    object Equal : CompareResult()
    object Incomparable : CompareResult()
}
