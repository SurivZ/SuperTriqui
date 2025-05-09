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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import surivz.game.supertriqui.R
import surivz.game.supertriqui.ui.theme.GradientEnd
import surivz.game.supertriqui.ui.theme.GradientStart

@Composable
fun PointRaceTutorialScreen(
    onBackToSelection: () -> Unit,
    onComplete: (String) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var points by remember { mutableIntStateOf(0) }
    var boardState by remember { mutableStateOf(createInitialBoardState()) }
    var isInteractive by remember { mutableStateOf(true) }
    val activeBoard by remember { mutableStateOf(Pair(1, 1)) }

    val steps = listOf(
        TutorialStep(
            title = context.getString(R.string.point_race_step1_title),
            content = context.getString(R.string.point_race_step1_content),
            icon = Icons.Default.School
        ),
        TutorialStep(
            title = context.getString(R.string.point_race_step2_title),
            content = context.getString(R.string.point_race_step2_content),
            icon = Icons.Default.Timer,
            exampleContent = { TimerExample() }
        ),
        TutorialStep(
            title = context.getString(R.string.point_race_step3_title),
            content = context.getString(R.string.point_race_step3_content),
            icon = Icons.Default.TouchApp,
            interactive = true,
            exampleContent = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Puntos: $points",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    InteractiveRaceBoard(
                        boardState = boardState,
                        activeBoard = activeBoard,
                        onCellSelected = { boardRow, boardCol, cellRow, cellCol ->
                            if (isInteractive && boardRow == 1 && boardCol == 1 && cellRow == 0 && cellCol == 2) {
                                boardState = updateBoardState(current = boardState)

                                scope.launch {
                                    delay(500)
                                    points++
                                    isInteractive = false
                                    boardState = resetCenterBoard(boardState)
                                }
                            }
                        },
                        isInteractive = isInteractive
                    )

                    Text(
                        text = if (isInteractive)
                            context.getString(R.string.point_race_no_action)
                        else
                            context.getString(R.string.point_race_action_detected),
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        ),
        TutorialStep(
            title = context.getString(R.string.point_race_step4_title),
            content = context.getString(R.string.point_race_step4_content),
            icon = Icons.Default.Timer,
            exampleContent = {
                TimedGameExample(currentPoints = points)
            }
        ),
        TutorialStep(
            title = context.getString(R.string.point_race_step5_title),
            content = context.getString(R.string.point_race_step5_content),
            icon = Icons.Default.Block,
            exampleContent = { BlockedBoardsExample() }
        ),
        TutorialStep(
            title = context.getString(R.string.point_race_step6_title),
            content = context.getString(R.string.point_race_step6_content),
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
                    text = context.getString(R.string.point_race_tutorial_title),
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
                            points = 0
                            boardState = createInitialBoardState()
                            isInteractive = true
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
                            if (currentStepData.interactive && currentStep == 2 && points == 0) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        context.getString(R.string.tutorial_indication)
                                    )
                                }
                                return@Button
                            }
                            currentStep++
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(context.getString(R.string.next))
                    }
                } else {
                    Button(
                        onClick = { onComplete("Carrera de Puntos") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.White
                        )
                    ) {
                        Text(context.getString(R.string.play_now))
                    }
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

private fun resetCenterBoard(current: List<List<List<List<Boolean?>>>>): List<List<List<List<Boolean?>>>> {
    return current.mapIndexed { br, board ->
        board.mapIndexed { bc, miniBoard ->
            if (br == 1 && bc == 1) {
                List(3) { List(3) { null } }
            } else {
                miniBoard
            }
        }
    }
}

private fun createInitialBoardState(): List<List<List<List<Boolean?>>>> {
    return List(3) { boardRow ->
        List(3) { boardCol ->
            if (boardRow == 1 && boardCol == 1) {
                List(3) { cellRow ->
                    List(3) { cellCol ->
                        when {
                            cellRow == 0 && cellCol < 2 -> true
                            else -> null
                        }
                    }
                }
            } else {
                List(3) { List(3) { null } }
            }
        }
    }
}

private fun updateBoardState(
    current: List<List<List<List<Boolean?>>>>,
    boardRow: Int = 1,
    boardCol: Int = 1,
    cellRow: Int = 0,
    cellCol: Int = 2,
    value: Boolean = true
): List<List<List<List<Boolean?>>>> {
    return current.mapIndexed { br, board ->
        if (br == boardRow) {
            board.mapIndexed { bc, miniBoard ->
                if (bc == boardCol) {
                    miniBoard.mapIndexed { cr, row ->
                        if (cr == cellRow) {
                            row.mapIndexed { cc, cell ->
                                if (cc == cellCol) value else cell
                            }
                        } else {
                            row
                        }
                    }
                } else {
                    miniBoard
                }
            }
        } else {
            board
        }
    }
}

@Composable
private fun InteractiveRaceBoard(
    boardState: List<List<List<List<Boolean?>>>>,
    activeBoard: Pair<Int, Int>,
    onCellSelected: (Int, Int, Int, Int) -> Unit,
    isInteractive: Boolean = true
) {
    Box(
        modifier = Modifier
            .size(280.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .background(Color.LightGray.copy(alpha = 0.25f))
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(9) { boardIndex ->
                val boardRow = boardIndex / 3
                val boardCol = boardIndex % 3
                val isActive =
                    boardRow == activeBoard.first && boardCol == activeBoard.second && isInteractive

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .aspectRatio(1f)
                        .border(
                            2.dp,
                            if (isActive) Color.Cyan else Color.DarkGray,
                            RoundedCornerShape(4.dp)
                        )
                        .background(
                            if (isActive) Color.Cyan.copy(alpha = 0.1f)
                            else Color.Transparent
                        )
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                    ) {
                        items(9) { cellIndex ->
                            val cellRow = cellIndex / 3
                            val cellCol = cellIndex % 3
                            val cellValue = boardState[boardRow][boardCol][cellRow][cellCol]
                            val isTargetCell =
                                isActive && boardRow == 1 && boardCol == 1 && cellRow == 0 && cellCol == 2

                            Box(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .aspectRatio(1f)
                                    .border(1.dp, Color.DarkGray.copy(alpha = 0.7f))
                                    .clickable(
                                        enabled = isActive && cellValue == null,
                                        onClick = {
                                            onCellSelected(boardRow, boardCol, cellRow, cellCol)
                                        }
                                    )
                                    .background(
                                        when {
                                            isTargetCell && cellValue == null -> Color.Cyan.copy(
                                                alpha = 0.2f
                                            )

                                            else -> Color.Transparent
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    cellValue != null -> Text(
                                        text = if (cellValue) "X" else "O",
                                        color = if (cellValue) Color.Red else Color.Blue,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    isTargetCell -> Text(
                                        text = "¡Aquí!",
                                        color = Color.Green,
                                        fontSize = 8.sp
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
private fun TimerExample(currentTime: Int = 15) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .size(150.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .background(Color.LightGray.copy(alpha = 0.25f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = context.getString(R.string.timer),
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$currentTime ${context.getString(R.string.second_abbr)}",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = context.getString(R.string.left_time),
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun TimedGameExample(
    currentPoints: Int,
    timeLeft: Int = 15
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .background(Color.LightGray.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = context.getString(R.string.time),
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = "$timeLeft ${context.getString(R.string.second_abbr)}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = context.getString(R.string.points),
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = "$currentPoints",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
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
                            .border(1.dp, Color.DarkGray)
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
