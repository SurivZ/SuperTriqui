package surivz.game.supertriqui.logic

data class SmallBoardState(
    val cells: List<Player?> = List(9) { null },
    val winner: Player? = null
) {
    fun isFinished(): Boolean = winner != null || isFull()

    fun isFull(): Boolean = cells.all { it != null }

    private fun checkWinner(cells: List<Player?>): Player? {
        val winningLines = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )

        return winningLines.firstNotNullOfOrNull { line ->
            val (a, b, c) = line
            if (cells[a] != null && cells[a] == cells[b] && cells[a] == cells[c]) cells[a] else null
        }
    }

    fun makeMove(index: Int, player: Player): SmallBoardState {
        if (cells[index] != null || winner != null) return this

        val newCells = cells.toMutableList().apply { this[index] = player }
        val newWinner = checkWinner(newCells)

        return copy(cells = newCells, winner = newWinner)
    }
}
