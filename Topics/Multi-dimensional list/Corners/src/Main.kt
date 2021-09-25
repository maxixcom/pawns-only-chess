fun main() {
    // Do not touch code below
    val inputList: MutableList<MutableList<String>> = mutableListOf()
    val n = readLine()!!.toInt()
    for (i in 0 until n) {
        val strings = readLine()!!.split(' ').toMutableList()
        inputList.add(strings)
    }

    val printLineOfList: (Int) -> Unit = { row ->
        println("${inputList[row][0]} ${inputList[row][inputList[row].lastIndex]}")
    }

    listOf(0, inputList.lastIndex).forEach(printLineOfList)
}
