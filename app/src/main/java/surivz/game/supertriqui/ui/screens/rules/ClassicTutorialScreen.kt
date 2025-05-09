package surivz.game.supertriqui.ui.screens.rules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import surivz.game.supertriqui.R
import surivz.game.supertriqui.ui.theme.GradientEnd
import surivz.game.supertriqui.ui.theme.GradientStart

@Composable
fun ClassicTutorialScreen(
    onBackToSelection: () -> Unit,
    onComplete: (String) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var hasMadeFirstMove by remember { mutableStateOf(false) }
    var hasMadeSecondMove by remember { mutableStateOf(false) }
    var practiceCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var secondMoveBoard by remember {
        mutableStateOf(List(3) { List<Boolean?>(3) { null } })
    }

    val steps = listOf(
        TutorialStep(
            title = context.getString(R.string.classic_tutorial_step1_title),
            content = context.getString(R.string.classic_tutorial_step1_content),
            icon = Icons.Default.School
        ),
        TutorialStep(
            title = context.getString(R.string.classic_tutorial_step2_title),
            content = context.getString(R.string.classic_tutorial_step2_content),
            icon = Icons.Default.Psychology,
            exampleContent = { StaticBoardExample() }
        ),
        TutorialStep(
            title = context.getString(R.string.classic_tutorial_step3_title),
            content = context.getString(R.string.classic_tutorial_step3_content),
            icon = Icons.Default.TouchApp,
            interactive = true,
            exampleContent = {
                FirstMoveExample(
                    hasMadeMove = hasMadeFirstMove,
                    onCellClicked = { row, col ->
                        if (row == 0 && col == 2) {
                            practiceCell = Pair(row, col)
                            hasMadeFirstMove = true
                        }
                    }
                )
            }
        ),
        TutorialStep(
            title = context.getString(R.string.classic_tutorial_step4_title),
            content = context.getString(R.string.classic_tutorial_step4_content),
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            interactive = true,
            exampleContent = {
                SecondMoveExample(
                    hasMadeMove = hasMadeSecondMove,
                    boardState = secondMoveBoard,
                    onCellClicked = { row, col ->
                        if (!hasMadeSecondMove && secondMoveBoard[row][col] == null) {
                            secondMoveBoard = secondMoveBoard.mapIndexed { rIdx, rowList ->
                                if (rIdx == row) {
                                    rowList.mapIndexed { cIdx, cell ->
                                        if (cIdx == col) true else cell
                                    }
                                } else {
                                    rowList
                                }
                            }
                            hasMadeSecondMove = true
                        }
                    }
                )
            }
        ),
        TutorialStep(
            title = context.getString(R.string.classic_tutorial_step5_title),
            content = context.getString(R.string.classic_tutorial_step5_content),
            icon = Icons.Default.Block,
            exampleContent = { BlockedBoardsExample() }
        ),
        TutorialStep(
            title = context.getString(R.string.classic_tutorial_step6_title),
            content = context.getString(R.string.classic_tutorial_step6_content),
            icon = Icons.Default.Done
        )
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackToSelection
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = context.getString(R.string.go_back),
                        tint = Color.White
                    )
                }
                Text(
                    text = context.getString(R.string.classic_tutorial_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 0) {
                    OutlinedButton(
                        onClick = {
                            currentStep--
                            hasMadeFirstMove = false
                            hasMadeSecondMove = false
                            practiceCell = null
                            if (currentStep == 2) {
                                secondMoveBoard = List(3) { List(3) { null } }
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f))
                    ) {
                        Text(context.getString(R.string.previous))
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                if (currentStep < steps.size - 1) {
                    Button(
                        onClick = {
                            val currentStepData = steps[currentStep]
                            if (currentStepData.interactive &&
                                ((currentStep == 2 && !hasMadeFirstMove) ||
                                        (currentStep == 3 && !hasMadeSecondMove))
                            ) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(context.getString(R.string.tutorial_indication))
                                }
                                return@Button
                            }
                            currentStep++
                            hasMadeFirstMove = false
                            hasMadeSecondMove = false
                            practiceCell = null
                            if (currentStep == 3) {
                                secondMoveBoard = List(3) { List(3) { null } }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) { Text(context.getString(R.string.next)) }
                } else {
                    Button(
                        onClick = { onComplete("Clásico") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.White
                        )
                    ) { Text(context.getString(R.string.play_now)) }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GradientStart, GradientEnd)
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = steps[currentStep].icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = steps[currentStep].title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = steps[currentStep].content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                steps[currentStep].exampleContent?.invoke()
            }
        }
    }
}

@Composable
private fun StaticBoardExample() {
    Box(
        modifier = Modifier
            .size(240.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(9) {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .aspectRatio(1f)
                        .border(1.dp, Color.DarkGray)
                        .background(Color.LightGray.copy(alpha = 0.25f))
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                    ) {
                        items(9) {
                            Box(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .aspectRatio(1f)
                                    .border(1.dp, Color.DarkGray)
                                    .background(Color.LightGray.copy(alpha = 0.125f))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FirstMoveExample(
    hasMadeMove: Boolean,
    onCellClicked: (row: Int, col: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .background(Color.LightGray.copy(alpha = 0.25f))
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(9) { index ->
                val row = index / 3
                val col = index % 3
                val isTarget = row == 0 && col == 2

                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .aspectRatio(1f)
                        .border(
                            1.dp,
                            if (isTarget) Color.Cyan.copy(alpha = 0.3f)
                            else Color.DarkGray
                        )
                        .clickable(
                            enabled = !hasMadeMove && isTarget,
                            onClick = { onCellClicked(row, col) }
                        )
                        .background(
                            if (isTarget && !hasMadeMove) Color.Cyan.copy(alpha = 0.15f)
                            else Color.LightGray.copy(alpha = 0.125f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isTarget && hasMadeMove) {
                        Text(
                            text = "X",
                            color = Color.Red,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SecondMoveExample(
    hasMadeMove: Boolean,
    boardState: List<List<Boolean?>>,
    onCellClicked: (row: Int, col: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(240.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(9) { boardIndex ->
                val boardRow = boardIndex / 3
                val boardCol = boardIndex % 3
                val isTopRightBoard = boardRow == 0 && boardCol == 2

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .aspectRatio(1f)
                        .border(
                            1.dp,
                            if (isTopRightBoard) Color.Cyan
                            else Color.DarkGray
                        )
                        .background(Color.LightGray.copy(alpha = 0.25f))
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                    ) {
                        items(9) { cellIndex ->
                            val row = cellIndex / 3
                            val col = cellIndex % 3
                            val cell = if (isTopRightBoard) boardState[row][col] else null
                            val hasPredefinedX =
                                boardRow == 1 && boardCol == 1 && row == 0 && col == 2

                            Box(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .aspectRatio(1f)
                                    .border(1.dp, Color.DarkGray)
                                    .clickable(
                                        enabled = isTopRightBoard && !hasMadeMove && cell == null,
                                        onClick = { onCellClicked(row, col) }
                                    )
                                    .background(
                                        if (isTopRightBoard && !hasMadeMove)
                                            Color.Cyan.copy(alpha = 0.15f)
                                        else Color.LightGray.copy(alpha = 0.125f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    hasPredefinedX -> Text(
                                        text = "X",
                                        color = Color.Red,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    cell == true -> Text(
                                        text = "O",
                                        color = Color.Blue,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BlockedBoardsExample() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .padding(8.dp)
                .background(Color.LightGray.copy(alpha = 0.25f))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize()
            ) {
                items(9) { index ->
                    val row = index / 3
                    val col = index % 3
                    val isDiagonal = row == col

                    Box(
                        modifier = Modifier
                            .padding(1.dp)
                            .aspectRatio(1f)
                            .border(1.dp, Color.DarkGray)
                            .background(Color.LightGray.copy(alpha = 0.125f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDiagonal) {
                            Text(
                                text = "X",
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .size(120.dp)
                .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .padding(8.dp)
                .background(Color.LightGray.copy(alpha = 0.25f))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize()
            ) {
                items(9) { index ->
                    val xPositions = listOf(0, 2, 3, 5, 7)
                    val oPositions = listOf(1, 4, 6, 8)

                    Box(
                        modifier = Modifier
                            .padding(1.dp)
                            .aspectRatio(1f)
                            .border(1.dp, Color.Black.copy(alpha = 0.5f))
                            .background(Color.LightGray.copy(alpha = 0.125f)),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            xPositions.contains(index) -> Text(
                                text = "X",
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            oPositions.contains(index) -> Text(
                                text = "O",
                                color = Color.Blue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

data class TutorialStep(
    val title: String,
    val content: String,
    val icon: ImageVector,
    val interactive: Boolean = false,
    val exampleContent: (@Composable () -> Unit)? = null
)
