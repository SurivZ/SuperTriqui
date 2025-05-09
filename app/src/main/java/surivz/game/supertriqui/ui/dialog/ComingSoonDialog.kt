package surivz.game.supertriqui.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import surivz.game.supertriqui.R
import surivz.game.supertriqui.ui.theme.GradientEnd
import surivz.game.supertriqui.ui.theme.GradientStart

@Composable
fun ComingSoonDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.Companion
            .fillMaxSize()
            .background(Color.Companion.Black.copy(alpha = 0.7f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Companion.Center
    ) {
        Surface(
            modifier = Modifier.Companion.widthIn(min = 280.dp, max = 320.dp),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 16.dp
        ) {
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .background(
                        Brush.Companion.linearGradient(
                            colors = listOf(GradientEnd, GradientStart),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color.Companion.White,
                        modifier = Modifier.Companion.size(48.dp)
                    )

                    Spacer(modifier = Modifier.Companion.height(16.dp))

                    Text(
                        text = context.getString(R.string.coming_soon_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Companion.White,
                        fontWeight = FontWeight.Companion.Bold,
                        modifier = Modifier.Companion.fillMaxWidth(),
                        textAlign = TextAlign.Companion.Center
                    )

                    Spacer(modifier = Modifier.Companion.height(8.dp))

                    Text(
                        text = context.getString(R.string.coming_soon_message),
                        color = Color.Companion.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Companion.Center,
                        lineHeight = 24.sp,
                        modifier = Modifier.Companion.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.Companion.height(24.dp))

                    Text(
                        text = context.getString(R.string.coming_soon_instruction),
                        color = Color.Companion.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        modifier = Modifier.Companion.fillMaxWidth(),
                        textAlign = TextAlign.Companion.Center
                    )
                }
            }
        }
    }
}
