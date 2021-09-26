fun f(x: Double): Double = when {
    x <= 0 -> f1(x)
    x > 0 && x < 1 -> f2(x)
    else -> f3(x)
}

// implement your functions here
fun f1(x: Double): Double = x * x + 1

fun f2(x: Double): Double = 1 / (x * x)

fun f3(x: Double): Double = x * x - 1
