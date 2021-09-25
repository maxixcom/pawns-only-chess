fun main() {
    val n = readLine()!!.toInt()
    val incomes = List(n) {
        readLine()!!.toInt()
    }
    val taxes = List(n) {
        readLine()!!.toDouble() * incomes[it] / 100
    }

    println(taxes.indexOf(taxes.maxOrNull()) + 1)
}
