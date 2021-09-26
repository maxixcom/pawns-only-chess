fun main() = readLine()!!.let { input ->
    var uniq = 0
    for (c in 'a'..'z') {
        if (input.count { it == c } == 1) {
            uniq++
        }
    }
    println(uniq)
}
