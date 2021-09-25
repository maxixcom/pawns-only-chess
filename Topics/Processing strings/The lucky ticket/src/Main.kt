fun main() {
    val (left, right) = readLine()!!.chunked(3).map {
        it.map(Char::toInt).sum()
    }
    if (left == right) {
        println("Lucky")
    } else {
        println("Regular")
    }
}
