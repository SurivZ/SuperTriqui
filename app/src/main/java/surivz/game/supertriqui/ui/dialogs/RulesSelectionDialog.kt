package surivz.game.supertriqui.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import surivz.game.supertriqui.R
import surivz.game.supertriqui.ui.components.CustomButton
import surivz.game.supertriqui.ui.navigation.Routes
import surivz.game.supertriqui.ui.theme.GradientEnd
import surivz.game.supertriqui.ui.theme.GradientStart

@Composable
fun RulesSelectionDialog(
    navController: NavController,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
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
                    modifier = Modifier.Companion.padding(24.dp),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally
                ) {
                    Text(
                        text = context.getString(R.string.game_mode_selection_title),
                        style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center),
                        color = Color.Companion.White,
                        fontWeight = FontWeight.Companion.Bold,
                        modifier = Modifier.Companion.padding(bottom = 16.dp)
                    )

                    CustomButton(
                        icon = Icons.Default.Group,
                        text = context.getString(R.string.classic_title),
                        description = context.getString(R.string.classic_content),
                        onClick = {
                            onDismiss()
                            navController.navigate(Routes.ClassicTutorial.route)
                        }
                    )

                    Spacer(modifier = Modifier.Companion.height(12.dp))

                    CustomButton(
                        icon = Icons.Default.Shuffle,
                        text = context.getString(R.string.chaotic_title),
                        description = context.getString(R.string.chaotic_content),
                        onClick = {
                            onDismiss()
                            navController.navigate(Routes.ChaoticTutorial.route)
                        }
                    )

                    Spacer(modifier = Modifier.Companion.height(12.dp))

                    CustomButton(
                        icon = Icons.Default.Map,
                        text = context.getString(R.string.domination_title),
                        description = context.getString(R.string.domination_content),
                        onClick = {
                            onDismiss()
                            navController.navigate(Routes.DominationTutorial.route)
                        }
                    )

                    Spacer(modifier = Modifier.Companion.height(12.dp))

                    CustomButton(
                        icon = Icons.Default.Block,
                        text = context.getString(R.string.no_ties_title),
                        description = context.getString(R.string.no_ties_content),
                        onClick = {
                            onDismiss()
                            navController.navigate(Routes.NoMercyTutorial.route)
                        }
                    )

                    Spacer(modifier = Modifier.Companion.height(12.dp))

                    CustomButton(
                        icon = Icons.Default.Timer,
                        text = context.getString(R.string.point_race_title),
                        description = context.getString(R.string.point_race_content),
                        onClick = {
                            onDismiss()
                            navController.navigate(Routes.PointsRaceTutorial.route)
                        }
                    )
                }
            }
        }
    }
}
