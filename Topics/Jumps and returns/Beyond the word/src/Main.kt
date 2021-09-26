fun main() {
    val input = readLine()!!.lowercase().toSet()

    for (c in 'a'..'z') {
        if (c in input) {
            continue
        }
        print(c)
    }
}
