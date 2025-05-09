package surivz.game.supertriqui

import surivz.game.supertriqui.logic.GameState
import surivz.game.supertriqui.logic.Player
import kotlin.random.Random

enum class AILevel {
    NOVICE, INTERMEDIATE, EXPERT
}

class AIPlayer(private val level: AILevel) {
    fun makeMove(gameState: GameState): Pair<Int, Int>? {
        if (gameState.isGameOver()) return null

        val validBoards = getValidBoards(gameState)
        if (validBoards.isEmpty()) return null

        return when (level) {
            AILevel.NOVICE -> makeNoviceMove(gameState, validBoards)
            AILevel.INTERMEDIATE -> makeIntermediateMove(gameState, validBoards)
            AILevel.EXPERT -> null
        }
    }

    private fun getValidBoards(gameState: GameState): List<Int> {
        return if (gameState.nextBoard == -1) {
            gameState.boards.indices.filter { !gameState.boards[it].isFinished() }
        } else {
            listOf(gameState.nextBoard).filter { !gameState.boards[it].isFinished() }
        }
    }

    private fun makeNoviceMove(gameState: GameState, validBoards: List<Int>): Pair<Int, Int> {
        return if (Random.nextInt(100) < 20) {
            makeSmartMove(gameState, validBoards) ?: makeRandomMove(gameState, validBoards)
        } else {
            makeRandomMove(gameState, validBoards)
        }
    }

    private fun makeIntermediateMove(gameState: GameState, validBoards: List<Int>): Pair<Int, Int> {
        return makeSmartMove(gameState, validBoards) ?: makeRandomMove(gameState, validBoards)
    }

    private fun makeSmartMove(gameState: GameState, validBoards: List<Int>): Pair<Int, Int>? {
        for (boardIndex in validBoards) {
            for (cellIndex in gameState.boards[boardIndex].cells.indices) {
                if (gameState.boards[boardIndex].cells[cellIndex] == null) {
                    val simulatedBoard = gameState.boards[boardIndex].makeMove(cellIndex, Player.O)
                    if (simulatedBoard.winner == Player.O) {
                        return boardIndex to cellIndex
                    }
                }
            }
        }

        for (boardIndex in validBoards) {
            for (cellIndex in gameState.boards[boardIndex].cells.indices) {
                if (gameState.boards[boardIndex].cells[cellIndex] == null) {
                    val simulatedBoard = gameState.boards[boardIndex].makeMove(cellIndex, Player.X)
                    if (simulatedBoard.winner == Player.X) {
                        return boardIndex to cellIndex
                    }
                }
            }
        }

        return null
    }

    private fun makeRandomMove(gameState: GameState, validBoards: List<Int>): Pair<Int, Int> {
        val boardIndex = validBoards.random()

        val emptyCells = gameState.boards[boardIndex].cells.mapIndexedNotNull { index, player ->
            if (player == null) index else null
        }

        return boardIndex to emptyCells.random()
    }
}
