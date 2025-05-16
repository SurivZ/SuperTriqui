package surivz.game.supertriqui.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import surivz.game.supertriqui.R

@Composable
fun GameHeader(
    onBack: () -> Unit,
    onSurrender: () -> Unit,
    modifier: Modifier = Modifier,
    allowDraw: Boolean = true,
    onDraw: () -> Unit = {},
    vsAI: Boolean = false,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = context.getString(R.string.go_back),
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Row {
            if (!vsAI && allowDraw) {
                IconButton(onClick = onDraw) {
                    Icon(
                        imageVector = Icons.Default.Handshake,
                        contentDescription = context.getString(R.string.offer_draw_title),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            IconButton(onClick = onSurrender) {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = context.getString(R.string.surrender_button),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
