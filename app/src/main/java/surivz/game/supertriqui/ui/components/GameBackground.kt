package surivz.game.supertriqui.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import surivz.game.supertriqui.ui.theme.GradientEnd
import surivz.game.supertriqui.ui.theme.GradientStart

@Composable
fun GameBackground(
    modifier: Modifier = Modifier,
    canvas: Boolean = true,
    start: Offset = Offset(0f, 0f),
    end: Offset = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(GradientStart, GradientEnd),
                        start = start,
                        end = end
                    )
                )
        )


        if (canvas) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cellSize = size.minDimension / 12f
                val symbolAlpha = 0.25f

                val horizontalCount = (size.width / cellSize).toInt() + 2
                val verticalCount = (size.height / cellSize).toInt() + 2

                for (i in 0..horizontalCount) {
                    for (j in 0..verticalCount) {
                        val x = i * cellSize
                        val y = j * cellSize

                        if ((i + j) % 2 == 0) {
                            drawLine(
                                color = Color.White.copy(alpha = symbolAlpha),
                                start = Offset(x + cellSize * 0.2f, y + cellSize * 0.2f),
                                end = Offset(x + cellSize * 0.8f, y + cellSize * 0.8f),
                                strokeWidth = 2.5f
                            )
                            drawLine(
                                color = Color.White.copy(alpha = symbolAlpha),
                                start = Offset(x + cellSize * 0.8f, y + cellSize * 0.2f),
                                end = Offset(x + cellSize * 0.2f, y + cellSize * 0.8f),
                                strokeWidth = 2.5f
                            )
                        } else {
                            drawCircle(
                                color = Color.White.copy(alpha = symbolAlpha),
                                center = Offset(x + cellSize / 2, y + cellSize / 2),
                                radius = cellSize * 0.3f,
                                style = Stroke(width = 2.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}
