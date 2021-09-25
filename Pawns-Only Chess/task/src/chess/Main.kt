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

    /**
     * Main loop
     */
    fun run() {
        var currentPlayer: Player
        var step = 0
        while (true) {
            printBoard()
            currentPlayer = players[step % 2]
            // Ask player to make a move. If move is invalid ask him again, until we get correct one
            while (true) {
                val command = enterCommand(currentPlayer)
                try {
                    when (command) {
                        Command.Exit -> return
                        is Command.Move -> {
                            // Validate move
                            validateMove(currentPlayer, command)
                            // Process move
                            board[command.end.y][command.end.x] = board[command.start.y][command.start.x]
                            board[command.start.y][command.start.x] = CellType.Empty
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

    /**
     * Validate player's move
     */
    private fun validateMove(player: Player, command: Command.Move) {
        if (board[command.start.y][command.start.x] != player.type) {
            throw NoPawnToMoveException("No pawn at [${command.start.x},${command.start.y}] of ${player.type} type")
        }

        if (command.start.x != command.end.x) {
            throw InvalidMoveException("only vertical move is allowed")
        }

        val len = abs(command.start.y - command.end.y)
        if (len > 2) {
            throw InvalidMoveException("requested step is too large: [$len]")
        }

        if (player.type == CellType.White) {
            if (command.start.y >= command.end.y) {
                // wrong direction or no advance
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
    }

    /**
     * Wait for player's input and convert it to Command
     * if input is inappropriate we ask player to make request again
     */
    private fun enterCommand(player: Player): Command {
        while (true) {
            try {
                print("${player.name}'s turn:\n> ")
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
