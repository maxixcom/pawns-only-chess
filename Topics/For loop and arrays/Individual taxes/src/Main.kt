fun main() {
    // enter number of companies
    val num = readLine()!!.toInt()
    // enter companies amount
    val amounts = (1..num)
        .map { readLine()!!.toInt() }
        .toList()
    // enter taxes
    val taxes: List<Double> = (0 until num)
        .map {
            val tax = readLine()!!.toInt()
            amounts[it] * tax / 100.0
        }
        .toList()
    // find company that pays most of the taxes
    var id = 0
    var maxTax = 0.0
    (0 until num).forEach {
        if (taxes[it] > maxTax) {
            maxTax = taxes[it]
            id = it
        }
    }

    println(id + 1)
}
