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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import surivz.game.supertriqui.AILevel
import surivz.game.supertriqui.AIPlayer
import surivz.game.supertriqui.R
import surivz.game.supertriqui.logic.GameState
import surivz.game.supertriqui.logic.MoveRecord
import surivz.game.supertriqui.logic.Player
import surivz.game.supertriqui.logic.SmallBoardState
import surivz.game.supertriqui.logic.opponent
import surivz.game.supertriqui.telemetry.Game
import surivz.game.supertriqui.telemetry.TelemetryManager
import surivz.game.supertriqui.ui.components.GameBackground
import surivz.game.supertriqui.ui.components.GameContent
import surivz.game.supertriqui.ui.components.GameHeader
import surivz.game.supertriqui.ui.components.MoveHistory
import surivz.game.supertriqui.ui.dialogs.GameDialog
import surivz.game.supertriqui.ui.dialogs.GameResultDialog
import surivz.game.supertriqui.telemetry.MoveRecord as TelemetryMoveRecord

@Composable
fun DominationGameScreen(
    onBackToMain: () -> Unit,
    vsAI: Boolean = false,
    aiLevel: AILevel = AILevel.NOVICE,
    telemetryAllowed: Boolean = false
) {
    val context = LocalContext.current
    val startTime = remember { System.currentTimeMillis() }
    val telemetryManager = remember { TelemetryManager(context) }

    GameBackground(canvas = false)

    var showSurrenderDialog by remember { mutableStateOf(false) }
    var showDrawOfferDialog by remember { mutableStateOf(false) }
    var showDrawResponseDialog by remember { mutableStateOf(false) }
    var showGameResultDialog by remember { mutableStateOf(false) }
    var gameResultMessage by remember { mutableStateOf("") }

    var gameState by remember { mutableStateOf(DominationGameState()) }
    val aiPlayer by remember { mutableStateOf(AIPlayer(aiLevel)) }
    var isAITurn by remember { mutableStateOf(false) }

    fun handleGameEnd() {
        gameResultMessage = when (gameState.winner) {
            Player.X -> context.getString(R.string.player_wins, Player.X)
            Player.O -> context.getString(R.string.player_wins, Player.O)
            else -> context.getString(R.string.game_tied)
        }

        showGameResultDialog = true
        isAITurn = false

        // Telemetría
        val duration = System.currentTimeMillis() - startTime
        val result = when (gameState.winner) {
            Player.X -> "X"
            Player.O -> "O"
            else -> "draw"
        }

        if (telemetryAllowed) {
            CoroutineScope(Dispatchers.IO).launch {
                telemetryManager.logEvent(
                    Game(
                        mode = "domination",
                        vsAI = vsAI,
                        aiLevel = if (vsAI) aiLevel.name else null,
                        result = result,
                        durationMillis = duration,
                        moveCount = gameState.moveHistory.size,
                        moves = gameState.moveHistory.map { move ->
                            TelemetryMoveRecord(
                                moveNumber = move.moveNumber,
                                player = move.player.toString(),
                                boardIndex = move.boardIndex,
                                cellIndex = move.cellIndex,
                                resultedInBoardWin = move.resultedInBoardWin,
                                resultedInBoardDraw = move.resultedInBoardDraw
                            )
                        }
                    )
                )
            }
        }
    }

    fun resetGame() {
        gameState = DominationGameState()
        showGameResultDialog = false
        isAITurn = false
    }

    fun onCellClick(boardIndex: Int, cellIndex: Int) {
        if (gameState.isGameOver()) return
        if (gameState.nextBoard != -1 && gameState.nextBoard != boardIndex) return
        if (gameState.boards[boardIndex].isFinished()) return

        val newState = gameState.makeMove(boardIndex, cellIndex)
        gameState = newState

        if (newState.isGameOver()) {
            handleGameEnd()
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
                    handleGameEnd()
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
                    handleGameEnd()
                }
            )
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
                }
            )
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
                    handleGameEnd()
                }
            )
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

data class DominationGameState(
    override val boards: List<SmallBoardState> = List(9) { SmallBoardState() },
    override val currentPlayer: Player = Player.X,
    override val nextBoard: Int = -1,
    override val winner: Player? = null,
    val moveHistory: List<MoveRecord> = emptyList(),
    val xBoardsWon: Int = 0,
    val oBoardsWon: Int = 0
) : GameState {

    override fun isGameOver(): Boolean = winner != null || boards.all { it.isFinished() }

    private fun calculateWinner(xWins: Int, oWins: Int): Player? {
        return when {
            xWins >= 5 -> Player.X
            oWins >= 5 -> Player.O
            boards.all { it.isFinished() } -> {
                when {
                    xWins > oWins -> Player.X
                    oWins > xWins -> Player.O
                    else -> null // Empate real
                }
            }

            else -> null // Juego aún no termina
        }
    }

    fun makeMove(boardIndex: Int, cellIndex: Int): DominationGameState {
        if (isGameOver() || boards[boardIndex].isFinished()) return this

        val currentBoard = boards[boardIndex]
        val updatedBoard = currentBoard.makeMove(cellIndex, currentPlayer)

        // Determinar si este movimiento ganó un tablero
        val didWinBoard = (updatedBoard.winner != null && currentBoard.winner == null)
        val newXBoardsWon =
            xBoardsWon + if (didWinBoard && updatedBoard.winner == Player.X) 1 else 0
        val newOBoardsWon =
            oBoardsWon + if (didWinBoard && updatedBoard.winner == Player.O) 1 else 0

        // Actualizar la lista de tableros
        val newBoards = boards.toMutableList().apply { this[boardIndex] = updatedBoard }

        // Calcular el nuevo ganador
        val newWinner = calculateWinner(newXBoardsWon, newOBoardsWon)

        // Determinar el próximo tablero permitido
        val nextBoard = when {
            newWinner != null -> -1
            newBoards[cellIndex].isFinished() -> -1
            else -> cellIndex
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
                resultedInBoardWin = didWinBoard,
                resultedInBoardDraw = updatedBoard.isFull() && updatedBoard.winner == null
            ),
            xBoardsWon = newXBoardsWon,
            oBoardsWon = newOBoardsWon
        )
    }
}
