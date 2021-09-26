data class Task(val name: String)

object Manager {
    var solvedTask: Int = 0
    fun solveTask(task: Task) {
        solvedTask++
        println("Task ${task.name} solved!")
    }
}
