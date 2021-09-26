enum class Country(val currency: String) {
    Germany("Euro"),
    Mali("CFA franc"),
    Dominica("Eastern Caribbean dollar"),
    Canada("Canadian dollar"),
    Spain("Euro"),
    Australia("Australian dollar"),
    Brazil("Brazilian real"),
    Senegal("CFA franc"),
    France("Euro"),
    Grenada("Eastern Caribbean dollar"),
    Kiribati("Australian dollar"),
}

fun main() {
    try {
        val countries = readLine()!!.split("\\s+".toRegex()).map(Country::valueOf).take(2)
        println(countries[0].currency == countries[1].currency)
    } catch (e: IllegalArgumentException) {
        println(false)
    }
}
