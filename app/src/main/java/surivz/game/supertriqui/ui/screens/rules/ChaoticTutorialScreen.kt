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
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
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
import androidx.compose.runtime.LaunchedEffect
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
fun ChaoticTutorialScreen(
    onBackToSelection: () -> Unit, onComplete: (String) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var hasMadeFirstMove by remember { mutableStateOf(false) }
    var hasMadeSecondMove by remember { mutableStateOf(false) }
    var practiceCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var selectedBoard by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    val steps = listOf(
        TutorialStep(
            title = context.getString(R.string.chaotic_tutorial_step1_title),
            content = context.getString(R.string.chaotic_tutorial_step1_content),
            icon = Icons.Default.School
        ),
        TutorialStep(
            title = context.getString(R.string.chaotic_tutorial_step2_title),
            content = context.getString(R.string.chaotic_tutorial_step2_content),
            icon = Icons.Default.Shuffle,
            exampleContent = { StaticBoardExample() }
        ),
        TutorialStep(
            title = context.getString(R.string.chaotic_tutorial_step3_title),
            content = context.getString(R.string.chaotic_tutorial_step3_content),
            icon = Icons.Default.TouchApp,
            interactive = true,
            exampleContent = {
                FirstMoveExample(
                    hasMadeMove = hasMadeFirstMove, onCellClicked = { row, col ->
                        practiceCell = Pair(row, col)
                        hasMadeFirstMove = true
                    })
            }
        ),
        TutorialStep(
            title = context.getString(R.string.chaotic_tutorial_step4_title),
            content = context.getString(R.string.chaotic_tutorial_step4_content),
            icon = Icons.Default.Casino,
            interactive = true,
            exampleContent = {
                RandomBoardSelectionExample(
                    hasMadeMove = hasMadeSecondMove,
                    selectedBoard = selectedBoard,
                    onBoardSelected = { boardRow, boardCol ->
                        if (!hasMadeSecondMove) {
                            selectedBoard = Pair(boardRow, boardCol)
                            hasMadeSecondMove = true
                        }
                    })
            }
        ),
        TutorialStep(
            title = context.getString(R.string.chaotic_tutorial_step5_title),
            content = context.getString(R.string.chaotic_tutorial_step5_content),
            icon = Icons.Default.Block,
            exampleContent = { BlockedBoardsExample() }
        ),
        TutorialStep(
            title = context.getString(R.string.chaotic_tutorial_step6_title),
            content = context.getString(R.string.chaotic_tutorial_step6_content),
            icon = Icons.Default.Done
        )
    )

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
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
                text = context.getString(R.string.chaotic_tutorial_title),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
        }
    }, bottomBar = {
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
                        selectedBoard = null
                    }, colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f))
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
                        if (currentStepData.interactive && ((currentStep == 2 && !hasMadeFirstMove) || (currentStep == 3 && !hasMadeSecondMove))) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    context.getString(R.string.tutorial_indication)
                                )
                            }
                            return@Button
                        }
                        currentStep++
                        hasMadeFirstMove = false
                        hasMadeSecondMove = false
                        practiceCell = null
                        selectedBoard = null
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(context.getString(R.string.next))
                }
            } else {
                Button(
                    onClick = { onComplete("Caótico") }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green, contentColor = Color.White
                    )
                ) {
                    Text(context.getString(R.string.play_now))
                }
            }
        }
    }) { paddingValues ->
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
private fun RandomBoardSelectionExample(
    hasMadeMove: Boolean, selectedBoard: Pair<Int, Int>?, onBoardSelected: (Int, Int) -> Unit
) {
    var showAnimation by remember { mutableStateOf(false) }
    var tempSelected by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (!hasMadeMove) {
            showAnimation = true
            coroutineScope.launch {
                repeat(3) {
                    tempSelected = Pair((0..2).random(), (0..2).random())
                    delay(500)
                }

                val randomRow = (0..2).random()
                val randomCol = (0..2).random()
                tempSelected = Pair(randomRow, randomCol)
                delay(800)

                onBoardSelected(randomRow, randomCol)
                showAnimation = false
            }
        }
    }

    Box(
        modifier = Modifier
            .size(240.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), modifier = Modifier.fillMaxSize()
        ) {
            items(9) { index ->
                val row = index / 3
                val col = index % 3
                val isSelected = selectedBoard?.let { it.first == row && it.second == col } == true
                val isAnimating = tempSelected?.let { it.first == row && it.second == col } == true

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .aspectRatio(1f)
                        .border(
                            1.dp, when {
                                isSelected -> Color.Magenta
                                isAnimating && showAnimation -> Color.Yellow
                                else -> Color.DarkGray
                            }
                        )
                        .background(
                            when {
                                isSelected -> Color.Magenta.copy(alpha = 0.2f)
                                isAnimating && showAnimation -> Color.Yellow.copy(alpha = 0.2f)
                                else -> Color.LightGray.copy(alpha = 0.1f)
                            }
                        ), contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = context.getString(R.string._selected),
                            tint = Color.Magenta
                        )
                    } else if (isAnimating && showAnimation) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = context.getString(R.string.searching),
                            tint = Color.Yellow,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
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
            columns = GridCells.Fixed(3), modifier = Modifier.fillMaxSize()
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
    hasMadeMove: Boolean, onCellClicked: (row: Int, col: Int) -> Unit
) {
    var markedCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    Box(
        modifier = Modifier
            .size(120.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .background(Color.LightGray.copy(alpha = 0.25f))
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), modifier = Modifier.fillMaxSize()
        ) {
            items(9) { index ->
                val row = index / 3
                val col = index % 3
                val isMarked = markedCell?.let { it.first == row && it.second == col } == true

                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .aspectRatio(1f)
                        .border(
                            1.dp, if (isMarked) Color.Cyan.copy(alpha = 0.7f)
                            else Color.DarkGray
                        )
                        .clickable(
                            enabled = !hasMadeMove && markedCell == null,
                            onClick = {
                                markedCell = Pair(row, col)
                                onCellClicked(row, col)
                            })
                        .background(
                            if (isMarked) Color.Cyan.copy(alpha = 0.3f)
                            else Color.LightGray.copy(alpha = 0.125f)
                        ), contentAlignment = Alignment.Center
                ) {
                    if (isMarked) {
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
private fun BlockedBoardsExample() {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .padding(8.dp)
                .background(Color.LightGray.copy(alpha = 0.25f))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), modifier = Modifier.fillMaxSize()
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
                columns = GridCells.Fixed(3), modifier = Modifier.fillMaxSize()
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
