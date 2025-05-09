package surivz.game.supertriqui.ui.screens.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import surivz.game.supertriqui.AILevel
import surivz.game.supertriqui.AIPlayer
import surivz.game.supertriqui.R
import surivz.game.supertriqui.logic.GameState
import surivz.game.supertriqui.logic.MoveRecord
import surivz.game.supertriqui.logic.Player
import surivz.game.supertriqui.logic.SmallBoardState
import surivz.game.supertriqui.logic.opponent
import surivz.game.supertriqui.ui.components.GameBackground
import surivz.game.supertriqui.ui.components.GameContent
import surivz.game.supertriqui.ui.components.GameHeader
import surivz.game.supertriqui.ui.components.MoveHistory
import surivz.game.supertriqui.ui.dialog.GameDialog
import surivz.game.supertriqui.ui.dialog.GameResultDialog

@Composable
fun ChaoticGameScreen(
    onBackToMain: () -> Unit,
    vsAI: Boolean = false,
    aiLevel: AILevel = AILevel.NOVICE
) {
    val context = LocalContext.current

    GameBackground(canvas = false)

    var showSurrenderDialog by remember { mutableStateOf(false) }
    var showDrawOfferDialog by remember { mutableStateOf(false) }
    var showDrawResponseDialog by remember { mutableStateOf(false) }
    var showGameResultDialog by remember { mutableStateOf(false) }
    var gameResultMessage by remember { mutableStateOf("") }

    var gameState by remember { mutableStateOf(ChaoticGameState()) }
    val aiPlayer by remember { mutableStateOf(AIPlayer(aiLevel)) }
    var isAITurn by remember { mutableStateOf(false) }

    fun handleGameEnd(message: String) {
        gameResultMessage = message
        showGameResultDialog = true
        isAITurn = false
    }

    fun resetGame() {
        gameState = ChaoticGameState()
        showGameResultDialog = false
        isAITurn = false
    }

    fun onCellClick(boardIndex: Int, cellIndex: Int) {
        if (gameState.isGameOver()) return

        if (gameState.moveHistory.isNotEmpty() &&
            (gameState.nextBoard != -1 && gameState.nextBoard != boardIndex)
        ) return
        if (gameState.boards[boardIndex].isFinished()) return

        val newState = gameState.makeMove(boardIndex, cellIndex)
        gameState = newState

        if (newState.isGameOver()) {
            handleGameEnd(
                when {
                    newState.winner != null -> context.getString(
                        R.string.player_wins,
                        newState.winner
                    )

                    else -> context.getString(R.string.game_tied)
                }
            )
        } else if (vsAI && newState.currentPlayer == Player.O) {
            isAITurn = true
        }
    }

    LaunchedEffect(gameState, isAITurn) {
        if (vsAI && isAITurn && !gameState.isGameOver()) {
            delay(500)

            val move = aiPlayer.makeMove(gameState)
            if (move != null) {
                val (boardIndex, cellIndex) = move
                val newState = gameState.makeMove(boardIndex, cellIndex)
                gameState = newState

                if (newState.isGameOver()) {
                    handleGameEnd(
                        when {
                            newState.winner != null -> context.getString(
                                R.string.player_wins,
                                newState.winner
                            )

                            else -> context.getString(R.string.game_tied)
                        }
                    )
                }
            }
            isAITurn = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GameHeader(
            onBack = onBackToMain,
            onSurrender = { showSurrenderDialog = true },
            onDraw = { showDrawOfferDialog = true },
            vsAI = vsAI
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 64.dp),
            contentAlignment = Alignment.Center
        ) {
            GameContent(
                gameState = gameState, onCellClick = ::onCellClick
            )
        }

        MoveHistory(
            moveHistory = gameState.moveHistory
        )

        if (showSurrenderDialog) {
            GameDialog(
                onDismiss = { showSurrenderDialog = false },
                title = context.getString(R.string.confirm_surrender_title),
                text = context.getString(R.string.confirm_surrender_message),
                confirmText = context.getString(R.string.surrender_button),
                onConfirm = {
                    gameState = gameState.copy(
                        winner = gameState.currentPlayer.opponent(),
                        currentPlayer = gameState.currentPlayer.opponent()
                    )
                    showSurrenderDialog = false
                    handleGameEnd(context.getString(R.string.player_wins, gameState.winner))
                })
        }

        if (showDrawOfferDialog) {
            GameDialog(
                onDismiss = { showDrawOfferDialog = false },
                title = context.getString(R.string.offer_draw_title),
                text = context.getString(R.string.offer_draw_message),
                confirmText = context.getString(R.string.offer_button),
                onConfirm = {
                    showDrawOfferDialog = false
                    showDrawResponseDialog = true
                })
        }

        if (showDrawResponseDialog) {
            GameDialog(
                onDismiss = { showDrawResponseDialog = false },
                title = context.getString(R.string.draw_response_title),
                text = context.getString(R.string.draw_response_message),
                confirmText = context.getString(R.string.accept_button),
                dismissText = context.getString(R.string.reject_button),
                onConfirm = {
                    gameState = gameState.copy(winner = null)
                    showDrawResponseDialog = false
                    handleGameEnd(context.getString(R.string.game_tied))
                })
        }

        if (showGameResultDialog) {
            GameResultDialog(
                message = gameResultMessage,
                onNewGame = { resetGame() },
                onBackToMenu = {
                    resetGame()
                    onBackToMain()
                }
            )
        }
    }
}

data class ChaoticGameState(
    override val boards: List<SmallBoardState> = List(9) { SmallBoardState() },
    override val currentPlayer: Player = Player.X,
    override val nextBoard: Int = -1,
    override val winner: Player? = null,
    val moveHistory: List<MoveRecord> = emptyList()
) : GameState {
    override fun isGameOver(): Boolean = winner != null || boards.all { it.isFinished() }

    private fun checkWinner(boards: List<SmallBoardState>): Player? {
        val bigBoard = boards.map { it.winner }
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

        for (line in winningLines) {
            val (a, b, c) = line
            if (bigBoard[a] != null && bigBoard[a] == bigBoard[b] && bigBoard[a] == bigBoard[c]) {
                return bigBoard[a]
            }
        }
        return null
    }

    fun makeMove(boardIndex: Int, cellIndex: Int): ChaoticGameState {
        if (isGameOver() || boards[boardIndex].isFinished()) return this

        val newBoards = boards.toMutableList()
        val updatedBoard = newBoards[boardIndex].makeMove(cellIndex, currentPlayer)
        newBoards[boardIndex] = updatedBoard

        val newWinner = checkWinner(newBoards)

        val nextBoard = if (newWinner == null && !newBoards.all { it.isFinished() }) {
            val availableBoards = newBoards.indices.filter {
                !newBoards[it].isFinished()
            }
            if (availableBoards.isNotEmpty()) availableBoards.random() else -1
        } else {
            -1
        }

        return copy(
            boards = newBoards,
            currentPlayer = currentPlayer.opponent(),
            nextBoard = nextBoard,
            winner = newWinner,
            moveHistory = moveHistory + MoveRecord(
                player = currentPlayer,
                moveNumber = moveHistory.size + 1,
                boardIndex = boardIndex,
                cellIndex = cellIndex,
                resultedInBoardWin = updatedBoard.winner != null,
                resultedInBoardDraw = updatedBoard.isFull() && updatedBoard.winner == null
            )
        )
    }
}
