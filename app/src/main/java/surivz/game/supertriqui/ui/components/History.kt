package surivz.game.supertriqui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import surivz.game.supertriqui.R
import surivz.game.supertriqui.logic.MoveRecord
import surivz.game.supertriqui.logic.Player

@Composable
fun MoveHistory(
    moveHistory: List<MoveRecord>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(true) }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(moveHistory.size) {
        if (moveHistory.isNotEmpty() && expanded) {
            coroutineScope.launch {
                scrollState.animateScrollToItem(moveHistory.size - 1)
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Icon(
                imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                contentDescription = if (expanded)
                    context.getString(R.string.show_history)
                else
                    context.getString(R.string.hide_history),
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
            Text(
                text = context.getString(R.string.move_history_title),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                contentDescription = if (expanded)
                    context.getString(R.string.show_history)
                else
                    context.getString(R.string.hide_history),
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }

        if (expanded) {
            LazyColumn(
                state = scrollState, modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                items(moveHistory) { move ->
                    MoveCard(move = move)
                }
            }
        }
    }
}

@Composable
fun MoveCard(move: MoveRecord) {
    val context = LocalContext.current

    val backgroundColor = if (move.player == Player.X) {
        Color(0x80F44336)
    } else {
        Color(0x802196F3)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .padding(start = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#${move.moveNumber}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = context.getString(R.string.player_move, move.player),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${
                    context.getString(
                        R.string.board_position,
                        move.boardIndex / 3 + 1,
                        move.boardIndex % 3 + 1
                    )
                }\n${
                    context.getString(
                        R.string.cell_position,
                        move.cellIndex / 3 + 1,
                        move.cellIndex % 3 + 1
                    )
                }",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .width(40.dp)
                .padding(end = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (move.resultedInBoardWin || move.resultedInBoardDraw) {
                Icon(
                    imageVector = if (move.resultedInBoardWin) Icons.Default.Star else Icons.Default.Handshake,
                    contentDescription = if (move.resultedInBoardWin)
                        context.getString(R.string.board_won_description)
                    else
                        context.getString(R.string.board_tied_description),
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
