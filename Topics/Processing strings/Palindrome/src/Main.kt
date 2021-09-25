fun main() {
    val s = readLine()!!
    var result = "yes"
    if (s.isNotEmpty()) {
        for (i in 0..s.length / 2) {
            if (s[i] != s[s.lastIndex - i]) {
                result = "no"
                break
            }
        }
    }
    println(result)
}
