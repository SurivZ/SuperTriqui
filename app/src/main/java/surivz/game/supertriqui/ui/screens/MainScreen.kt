package surivz.game.supertriqui.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import surivz.game.supertriqui.R
import surivz.game.supertriqui.ui.components.GameBackground
import surivz.game.supertriqui.ui.dialog.DifficultySelectionDialog
import surivz.game.supertriqui.ui.dialog.GameModeSelectionDialog
import surivz.game.supertriqui.ui.dialog.RulesSelectionDialog

@Composable
fun MainScreen(
    navController: NavController,
    onNavigateToGameMode: (String, Boolean, String) -> Unit,
    showDifficultyDialog: Boolean,
    onShowDifficultyDialog: (Boolean) -> Unit,
) {
    val context = LocalContext.current

    var showGameModeSelectionDialog by remember { mutableStateOf(false) }
    var showRulesSelection by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf("") }
    var showClassicOption by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        GameBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = context.getString(R.string.app_name).uppercase(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 45.sp,
                    textAlign = TextAlign.Center
                ),
                color = Color.White,
                modifier = Modifier.padding(bottom = 45.dp),
                fontWeight = FontWeight.ExtraBold
            )

            GameModeButton(
                icon = Icons.Default.Group,
                text = context.getString(R.string.classic_title).uppercase(),
                backgroundColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                onClick = { onNavigateToGameMode("classic", false, "") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            GameModeButton(
                icon = Icons.Default.Casino,
                text = context.getString(R.string.game_modes),
                backgroundColor = Color(0xFF2196F3),
                contentColor = Color.White,
                onClick = { showGameModeSelectionDialog = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            GameModeButton(
                icon = Icons.Default.Computer,
                text = context.getString(R.string.vs_machine),
                backgroundColor = Color(0xFFF44336),
                contentColor = Color.White,
                onClick = { onShowDifficultyDialog(true) }
            )
        }

        if (showGameModeSelectionDialog) {
            GameModeSelectionDialog(
                onDismiss = {
                    showClassicOption = false
                    showGameModeSelectionDialog = false
                },
                onGameModeSelected = { mode ->
                    showGameModeSelectionDialog = false
                    onNavigateToGameMode(mode, showClassicOption, selectedDifficulty)
                },
                showClassicOption = showClassicOption
            )
        }

        if (showDifficultyDialog) {
            DifficultySelectionDialog(
                onDismiss = { onShowDifficultyDialog(false) },
                onDifficultySelected = { difficulty ->
                    selectedDifficulty = difficulty
                    onShowDifficultyDialog(false)
                    showGameModeSelectionDialog = true
                    showClassicOption = true
                }
            )
        }

        IconButton(
            onClick = { showRulesSelection = true },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = context.getString(R.string.game_rules),
                tint = Color.White
            )
        }

        if (showRulesSelection) {
            RulesSelectionDialog(
                navController = navController,
                onDismiss = { showRulesSelection = false }
            )
        }
    }
}

@Composable
private fun GameModeButton(
    icon: ImageVector,
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
