package surivz.game.supertriqui.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import surivz.game.supertriqui.R
import surivz.game.supertriqui.logic.GameState

@Composable
fun GameContent(
    gameState: GameState,
    onCellClick: (Int, Int) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (gameState.isGameOver()) {
                when {
                    gameState.winner != null -> context.getString(
                        R.string.winner_indicator,
                        gameState.winner
                    )

                    else -> context.getString(R.string.tie_indicator)
                }
            } else {
                context.getString(R.string.turn_indicator, gameState.currentPlayer)
            },
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        BigBoard(
            gameState = gameState,
            onCellClick = onCellClick,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1f)
        )
    }
}
