package chess

import kotlin.math.abs

/**
 * Represent player
 */
data class Player(
    val name: String,
    val type: CellType
)

/**
 * Represent coordinate on a board
 */
data class Coordinate(
    val x: Int,
    val y: Int,
)

/**
 * Input command
 */
sealed class Command {
    data class Move(val start: Coordinate, val end: Coordinate) : Command()
    object Exit : Command()
}

/**
 * Represents cell state type on the board
 */
enum class CellType(private val label: String) {
    White("W"), // cell occupied with white pawn
    Black("B"), // cell occupied with black pawn
    Empty(" "); // cell is empty

    override fun toString(): String {
        return label
    }
}

/**
 * Exceptions
 */
class CoordinateFormatException(message: String) : Exception(message)
class MoveFormatException(message: String) : Exception(message)
class InvalidMoveException(message: String) : Exception(message)
class NoPawnToMoveException(message: String) : Exception(message)

/**
 * Convert chess coordinate to application coordinate object
 */
fun String.toCoordinate(): Coordinate {
    val regex = "^([a-h])([1-8])$".toRegex(RegexOption.IGNORE_CASE)
    return regex.find(this)?.let {
        Coordinate(
            x = it.groupValues[1][0] - 'a',
            y = it.groupValues[2][0].digitToInt() - 1
        )
    } ?: throw CoordinateFormatException("Can't parse coordinate from string: [$this]")
}

/**
 * Convert application coordinate to chess one
 */
fun Coordinate.toChess() = ('a' + this.x).toString() + (y + 1).toString()

/**
 * Convert move command from string representation to application Move object
 */
fun String.toMove(): Command.Move {
    if (this.length != 4) {
        throw MoveFormatException("Can't parse your move, should be of x0y1 format. Input is [$this]")
    }
    val (start, end) = this.chunked(2).take(2).map(String::toCoordinate)
    return Command.Move(start, end)
}

/**
 * Convert application Move object to chess formatted string
 */
fun Command.Move.toChess() = start.toChess() + end.toChess()

/**
 * Convert cell type value to color
 */
fun CellType.toColor() = when (this) {
    CellType.White -> "white"
    CellType.Black -> "black"
    else -> ""
}

fun List<List<CellType>>.isCellOfType(x: Int, y: Int, type: CellType) = this[y][x] == type

fun List<MutableList<CellType>>.movePawn(command: Command.Move) {
    this[command.end.y][command.end.x] = this[command.start.y][command.start.x]
    this[command.start.y][command.start.x] = CellType.Empty
}

fun List<MutableList<CellType>>.countPawnsOfType(cellType: CellType): Int {
    return sumOf { rank ->
        rank.count { it == cellType }
    }
}

/**
 * This class represents game process. It contains main loop and controls player's input requests.
 */
class Game(
    private val players: List<Player>,
) {
    private val board = List(8) {
        when (it) {
            1 -> MutableList(8) { CellType.White }
            6 -> MutableList(8) { CellType.Black }
            else -> MutableList(8) { CellType.Empty }
        }
    }

    data class BittenCell(
        val cell: Coordinate,
        val weekPawn: Coordinate,
        val weekPawnType: CellType,
    )

    private var bittenCell: BittenCell? = null
    private lateinit var currentPlayer: Player

    /**
     * Main loop
     */
    fun run() {
        var step = 0

        while (true) {
            printBoard()

            // check game state
            if (isWinCondition()) {
                break
            }

            if (isStalemateCondition()) {
                println("Stalemate!")
                break
            }

            currentPlayer = players[step % 2]
            // Ask player to make a move. If move is invalid ask him again, until we get correct one
            while (true) {
                val command = enterCommand()
                try {
                    when (command) {
                        Command.Exit -> return
                        is Command.Move -> {
                            processPlayersTurn(command)
                            break
                        }
                    }
                } catch (e: Exception) {
                    when (e) {
                        is InvalidMoveException -> println("Invalid Input")
                        is NoPawnToMoveException -> println(
                            "No ${currentPlayer.type.toColor()} pawn at " +
                                (command as Command.Move).start.toChess()
                        )
                        else -> throw e
                    }
                }
            }

            step++
        }
    }

    private fun isWinCondition(): Boolean {
        // Check win conditions for whites
        val whitePawnsReachTheEnd = board.last().count {
            it == CellType.White
        }
        val noBlackPawns = board.countPawnsOfType(CellType.Black) == 0

        if (whitePawnsReachTheEnd > 0 || noBlackPawns) {
            println("White Wins!")
            return true
        }

        // Check win conditions for blacks
        val blackPawnsReachTheEnd = board.first().count {
            it == CellType.Black
        }
        val noWhitePawns = board.countPawnsOfType(CellType.White) == 0

        if (blackPawnsReachTheEnd > 0 || noWhitePawns) {
            println("Black Wins!")
            return true
        }

        return false
    }

    private fun isStalemateCondition(): Boolean {
        val canMove: (CellType, Int, Int) -> Boolean = { type, x, y ->
            (type == CellType.White && board[y + 1][x] == CellType.Empty) ||
                (type == CellType.Black && board[y - 1][x] == CellType.Empty)
        }
        val canCapture: (CellType, Int, Int) -> Boolean = { type, x, y ->
            if (type == CellType.White) {
                (x - 1) >= 0 && (board[y + 1][x - 1] == CellType.Black) ||
                    (x + 1) <= 8 && (board[y + 1][x + 1] == CellType.Black)
            } else {
                (x - 1) >= 0 && (board[y - 1][x - 1] == CellType.White) ||
                    (x + 1) <= 7 && (board[y - 1][x + 1] == CellType.White)
            }
        }

        var blackHasMoves = false
        var whiteHasMoves = false
        for (y in board.indices) {
            for (x in board[y].indices) {
                if (!whiteHasMoves && board[y][x] == CellType.White) {
                    if (canMove(CellType.White, x, y) || canCapture(CellType.White, x, y)) {
                        whiteHasMoves = true
                    }
                } else if (!blackHasMoves && board[y][x] == CellType.Black) {
                    if (canMove(CellType.Black, x, y) || canCapture(CellType.Black, x, y)) {
                        blackHasMoves = true
                    }
                } else if (board[y][x] == CellType.Empty) {
                    continue
                }
                if (blackHasMoves && whiteHasMoves) {
                    break
                }
            }
        }

        return !blackHasMoves || !whiteHasMoves
    }

    /**
     * Process player's turn
     */
    private fun processPlayersTurn(command: Command.Move) {
        if (!board.isCellOfType(command.start.x, command.start.y, currentPlayer.type)) {
            throw NoPawnToMoveException("No pawn at ${command.start.toChess()} of ${currentPlayer.type} type")
        }

        if (command.start.x == command.end.x) {
            playerMove(command)
        } else if (
            abs(command.start.x - command.end.x) == 1 &&
            abs(command.start.y - command.end.y) == 1
        ) {
            playerCapture(command)
        } else {
            throw InvalidMoveException("only vertical move or capture is allowed")
        }
    }

    /**
     * make player's capture
     */
    private fun playerCapture(command: Command.Move) {
        if (currentPlayer.type == CellType.White) {
            // white captures black
            if (command.start.y > command.end.y) {
                throw InvalidMoveException("white: wrong direction")
            }
            if (bittenCell != null && bittenCell!!.cell == command.end) {
                return playerEnPassant(command)
            } else if (!board.isCellOfType(command.end.x, command.end.y, CellType.Black)) {
                throw InvalidMoveException("Can't capture no black pawn at ${command.end.toChess()}")
            }
        } else {
            // black captures white
            if (command.start.y < command.end.y) {
                throw InvalidMoveException("white: wrong direction")
            }
            if (bittenCell != null && bittenCell!!.cell == command.end) {
                return playerEnPassant(command)
            } else if (!board.isCellOfType(command.end.x, command.end.y, CellType.White)) {
                throw InvalidMoveException("Can't capture no white pawn at ${command.end.toChess()}")
            }
        }

        // process capture
        board.movePawn(command)

        // reset bitten cell if any
        bittenCell = null
    }

    /**
     * Process player en passant
     */
    private fun playerEnPassant(command: Command.Move) {
        board.movePawn(command)

        bittenCell?.let {
            // remove week pawn from board
            board[it.weekPawn.y][it.weekPawn.x] = CellType.Empty
            // reset bitten cell if any
            bittenCell = null
        }
    }

    /**
     * make player's move
     */
    private fun playerMove(command: Command.Move) {

        val len = abs(command.start.y - command.end.y)
        if (len > 2) {
            throw InvalidMoveException("requested step is too large: [$len]")
        }
        if (currentPlayer.type == CellType.White) {
            // white moves
            if (command.start.y >= command.end.y) {
                throw InvalidMoveException("white: wrong direction or no advance")
            }
            if (len == 2 && command.start.y != 1) {
                throw InvalidMoveException("white: two-step advance isn't allowed from rank ${command.start.y + 1}")
            }

            for (y in (command.start.y + 1)..command.end.y) {
                if (board[y][command.start.x] != CellType.Empty) {
                    throw InvalidMoveException(
                        "white: cell at [${command.start.x},y] is already taken by " +
                            board[y][command.start.x].toColor()
                    )
                }
            }
        } else {
            // black moves
            if (command.start.y <= command.end.y) {
                throw InvalidMoveException("black: wrong direction or no advance")
            }
            if (len == 2 && command.start.y != 6) {
                throw InvalidMoveException("black: two-step advance isn't allowed from rank ${command.start.y + 1}")
            }

            for (y in (command.start.y - 1) downTo command.end.y) {
                if (board[y][command.start.x] != CellType.Empty) {
                    throw InvalidMoveException(
                        "black: cell at [${command.start.x},y] is already taken by " +
                            board[y][command.start.x].toColor()
                    )
                }
            }
        }

        // process move
        board.movePawn(command)

        if (len == 2) {
            // register bitten cell
            this.bittenCell = BittenCell(
                cell = Coordinate(
                    x = command.end.x,
                    y = if (currentPlayer.type == CellType.White) {
                        command.end.y - 1
                    } else {
                        command.end.y + 1
                    }
                ),
                weekPawn = command.end,
                weekPawnType = currentPlayer.type
            )
        } else {
            this.bittenCell = null
        }
    }

    /**
     * Wait for player's input and convert it to Command
     * if input is inappropriate we ask player to make request again
     */
    private fun enterCommand(): Command {
        while (true) {
            try {
                print("${currentPlayer.name}'s turn:\n> ")
                val input = readLine()!!
                if (input == "exit") {
                    return Command.Exit
                }
                return input.toMove()
            } catch (e: Exception) {
                println("Invalid Input")
                continue
            }
        }
    }

    /**
     * The function will print current board state in format like:
     *
     *   +---+---+---+---+---+---+---+---+
     * 8 |   |   |   |   |   |   |   |   |
     *   +---+---+---+---+---+---+---+---+
     * 7 | B | B | B | B | B | B | B | B |
     *   +---+---+---+---+---+---+---+---+
     * 6 |   |   |   |   |   |   |   |   |
     *   +---+---+---+---+---+---+---+---+
     * 5 |   |   |   |   |   |   |   |   |
     *   +---+---+---+---+---+---+---+---+
     * 4 |   |   |   |   |   |   |   |   |
     *   +---+---+---+---+---+---+---+---+
     * 3 |   |   |   |   |   |   |   |   |
     *   +---+---+---+---+---+---+---+---+
     * 2 | W | W | W | W | W | W | W | W |
     *   +---+---+---+---+---+---+---+---+
     * 1 |   |   |   |   |   |   |   |   |
     *   +---+---+---+---+---+---+---+---+
     *     a   b   c   d   e   f   g   h
     */
    private fun printBoard() {
        val printSeparator: () -> Unit = {
            println("  +---+---+---+---+---+---+---+---+")
        }
        printSeparator()
        for (i in this.board.indices.reversed()) {
            println(
                "${i + 1} " + board[i].joinToString(
                    separator = " | ",
                    prefix = "| ",
                    postfix = " |"
                )
            )
            printSeparator()
        }
        println("    a   b   c   d   e   f   g   h")
    }
}

fun main() {
    println("Pawns-Only Chess")

    val players = List(2) {
        if (it == 0) {
            print("First Player's name:\n> ")
            Player(readLine()!!, CellType.White)
        } else {
            print("Second Player's name:\n> ")
            Player(readLine()!!, CellType.Black)
        }
    }

    Game(players)
        .run()

    println("Bye!")
}
