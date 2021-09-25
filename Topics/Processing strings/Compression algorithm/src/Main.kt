fun main() {
    val input = readLine()!!

    if (input.isEmpty()) {
        return
    }

    var lastChar = input[0]
    var count = 1

    for (i in 1..input.lastIndex) {
        if (input[i] == lastChar) {
            count++
            continue
        }

        print("$lastChar$count")

        lastChar = input[i]
        count = 1
    }

    print("$lastChar$count")
}
