data class Player(val id: Int, val name: String, val hp: Int) {
    companion object {
        var lastId: Int = 0
        fun create(name: String) = Player(lastId++, name, 100)
    }
}
