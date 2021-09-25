// write your code here
fun getLastDigit(i: Int) = Character.getNumericValue(i.toString().last())

/* Do not change code below */
fun main() {
    val a = readLine()!!.toInt()
    println(getLastDigit(a))
}
