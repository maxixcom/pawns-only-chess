fun main() {
    readLine()?.let { input ->
        for (c in input) {
            if (c.isDigit()) {
                print(c)
                break
            }
        }
    }
}
