// use this code as a source of inspiration for your function
fun getFullNames() = repeat(3) {
    println(createFullName(readLine()!!, readLine()!!))
}

// implement your function here
fun createFullName(first: String, last: String) = "$first $last"
