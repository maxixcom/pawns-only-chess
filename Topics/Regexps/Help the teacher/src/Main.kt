fun main() {
    val report = readLine()!!
    // write your code here.

    if (report.matches("\\d+ wrong answers?".toRegex())) {
        val num = report.replace(" wrong answers?".toRegex(), "").toInt()
        println(num <= 9)
    }
}
