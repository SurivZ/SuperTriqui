package surivz.game.supertriqui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import surivz.game.supertriqui.logic.GameState
import surivz.game.supertriqui.logic.Player
import surivz.game.supertriqui.logic.SmallBoardState

@Composable
fun BigBoard(
    gameState: GameState,
    onCellClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridColor = Color(0xFFFFFFFF)
    val boardBackground = Color(0x26FFFFFF)
    val activeBoardColor = Color(0x1A00ADFF)
    val xColor = Color(0xFFF44336)
    val oColor = Color(0xFF2196F3)
    val drawColor = Color(0xFF9E9E9E)

    Column(
        modifier = modifier
            .background(boardBackground)
            .border(2.dp, gridColor)
            .padding(6.dp)
    ) {
        for (row in 0 until 3) {
            Row(modifier = Modifier.weight(1f)) {
                for (col in 0 until 3) {
                    val boardIndex = row * 3 + col
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .background(
                                when {
                                    gameState.boards[boardIndex].winner == Player.X -> xColor.copy(
                                        alpha = 0.15f
                                    )

                                    gameState.boards[boardIndex].winner == Player.O -> oColor.copy(
                                        alpha = 0.15f
                                    )

                                    gameState.boards[boardIndex].isFull() -> drawColor.copy(alpha = 0.1f)
                                    !gameState.isGameOver() && (gameState.nextBoard == -1 || gameState.nextBoard == boardIndex) -> activeBoardColor
                                    else -> Color.Transparent
                                }
                            )
                    ) {
                        SmallBoard(
                            board = gameState.boards[boardIndex],
                            isActive = !gameState.isGameOver() && (gameState.nextBoard == -1 || gameState.nextBoard == boardIndex) && !gameState.boards[boardIndex].isFinished(),
                            onClick = { cellIndex -> onCellClick(boardIndex, cellIndex) },
                            gridColor = gridColor,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                        )

                        when {
                            gameState.boards[boardIndex].winner != null -> Text(
                                text = gameState.boards[boardIndex].winner.toString(),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (gameState.boards[boardIndex].winner == Player.X) xColor else oColor
                            )

                            gameState.boards[boardIndex].isFull() -> Text(
                                text = "—",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = drawColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SmallBoard(
    board: SmallBoardState,
    isActive: Boolean,
    onClick: (Int) -> Unit,
    gridColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            for (row in 0 until 3) {
                Row(modifier = Modifier.weight(1f)) {
                    for (col in 0 until 3) {
                        val cellIndex = row * 3 + col
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .border(0.75.dp, gridColor)
                                .clickable(
                                    enabled = isActive && board.cells[cellIndex] == null,
                                    onClick = { onClick(cellIndex) })
                                .background(
                                    if (isActive && board.cells[cellIndex] == null) Color(0x5538b8f5)
                                    else Color.Transparent
                                )
                        ) {
                            board.cells[cellIndex]?.let { player ->
                                Text(
                                    text = player.toString(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (player == Player.X) Color(0xFFF44336) else Color(
                                        0xFF2196F3
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
