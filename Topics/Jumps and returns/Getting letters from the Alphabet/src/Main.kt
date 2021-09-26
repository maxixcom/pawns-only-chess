fun main() {
    // put your code here
    val endChar = readLine()!![0]
    for (c in 'a'..'z') {
        if (c == endChar) {
            break
        }
        print(c)
    }
}
