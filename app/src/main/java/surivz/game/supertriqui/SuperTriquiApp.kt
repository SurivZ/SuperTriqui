package surivz.game.supertriqui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import surivz.game.supertriqui.ui.dialog.ComingSoonDialog
import surivz.game.supertriqui.ui.dialog.ConsentDialog
import surivz.game.supertriqui.ui.navigation.Routes
import surivz.game.supertriqui.ui.screens.MainScreen
import surivz.game.supertriqui.ui.screens.game.ChaoticGameScreen
import surivz.game.supertriqui.ui.screens.game.ClassicGameScreen
import surivz.game.supertriqui.ui.screens.rules.ChaoticTutorialScreen
import surivz.game.supertriqui.ui.screens.rules.ClassicTutorialScreen
import surivz.game.supertriqui.ui.screens.rules.DominationTutorialScreen
import surivz.game.supertriqui.ui.screens.rules.NoMercyTutorialScreen
import surivz.game.supertriqui.ui.screens.rules.PointsRaceTutorialScreen

@Composable
fun ConsentManager(
    onConsentGiven: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var showDialog by remember { mutableStateOf(false) }
    var consent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!prefs.contains("telemetry_consent")) {
            showDialog = true
        } else {
            consent = prefs.getBoolean("telemetry_consent", false)
            onConsentGiven(consent)
        }
    }

    if (showDialog) {
        ConsentDialog(
            onConfirm = {
                prefs.edit { putBoolean("telemetry_consent", true) }
                consent = true
                showDialog = false
                onConsentGiven(true)
            },
            onDismiss = {
                prefs.edit { putBoolean("telemetry_consent", false) }
                consent = false
                showDialog = false
                onConsentGiven(false)
            }
        )
    }
}

@Composable
fun SuperTriquiApp() {
    val navController = rememberNavController()
    var showDifficultyDialog by remember { mutableStateOf(false) }
    var showComingSoon by remember { mutableStateOf(false) }
    var currentGameMode by remember { mutableStateOf("") }
    var telemetryAllowed by remember { mutableStateOf(false) }

    ConsentManager { allowed ->
        telemetryAllowed = allowed
    }

    NavHost(
        navController = navController,
        startDestination = Routes.MainScreen.route
    ) {
        composable(Routes.MainScreen.route) {
            MainScreen(
                navController = navController,
                onNavigateToGameMode = { mode, vsAI, difficulty ->
                    when (difficulty) {
                        "Experto" -> showComingSoon = true
                        else -> {
                            currentGameMode = mode
                            when (mode) {
                                "classic" -> if (vsAI) navController.navigate(
                                    Routes.ClassicGameVsAI.createRoute(
                                        difficulty
                                    )
                                ) else navController.navigate(Routes.ClassicGame.route)

                                "chaotic" -> if (vsAI) navController.navigate(
                                    Routes.ChaoticGameVsAI.createRoute(
                                        difficulty
                                    )
                                ) else navController.navigate(Routes.ChaoticGame.route)

                                else -> showComingSoon = true
                            }
                        }
                    }
                },
                showDifficultyDialog = showDifficultyDialog,
                onShowDifficultyDialog = { show -> showDifficultyDialog = show },
            )

            if (showComingSoon) {
                ComingSoonDialog(
                    onDismiss = {
                        showComingSoon = false
                        currentGameMode = ""
                    }
                )
            }
        }

        // Clásico
        composable(Routes.ClassicTutorial.route) {
            ClassicTutorialScreen(
                onBackToSelection = { navController.popBackStack() },
                onComplete = { mode ->
                    currentGameMode = mode
                    navController.navigate(Routes.MainScreen.route)
                    navController.navigate(Routes.ClassicGame.route)
                }
            )
        }

        composable(Routes.ClassicGame.route) {
            ClassicGameScreen(
                onBackToMain = { navController.popBackStack() },
                telemetryAllowed = telemetryAllowed
            )
        }

        composable(Routes.ClassicGameVsAI.route) { backStackEntry ->
            val aiLevel = backStackEntry.arguments?.getString("aiLevel") ?: "Novato"

            ClassicGameScreen(
                onBackToMain = { navController.popBackStack() },
                vsAI = true,
                aiLevel = when (aiLevel) {
                    "Intermedio" -> AILevel.INTERMEDIATE
                    else -> AILevel.NOVICE
                },
                telemetryAllowed = telemetryAllowed
            )
        }

        // Caótico
        composable(Routes.ChaoticTutorial.route) {
            ChaoticTutorialScreen(
                onBackToSelection = { navController.popBackStack() },
                onComplete = { mode ->
                    currentGameMode = mode
                    navController.navigate(Routes.MainScreen.route)
                    navController.navigate(Routes.ChaoticGame.route)
                }
            )
        }

        composable(Routes.ChaoticGame.route) {
            ChaoticGameScreen(
                onBackToMain = { navController.popBackStack() },
                telemetryAllowed = telemetryAllowed
            )
        }

        composable(Routes.ChaoticGameVsAI.route) { backStackEntry ->
            val aiLevel = backStackEntry.arguments?.getString("aiLevel") ?: "Novato"

            ChaoticGameScreen(
                onBackToMain = { navController.popBackStack() },
                vsAI = true,
                aiLevel = when (aiLevel) {
                    "Intermedio" -> AILevel.INTERMEDIATE
                    else -> AILevel.NOVICE
                },
                telemetryAllowed = telemetryAllowed
            )
        }

        // Dominio del Mapa
        composable(Routes.DominationTutorial.route) {
            DominationTutorialScreen(
                onBackToSelection = { navController.popBackStack() },
                onComplete = { mode ->
                    currentGameMode = mode
                    navController.navigate(Routes.MainScreen.route)
                    showComingSoon = true
                }
            )
        }

        // Sin Empates
        composable(Routes.NoMercyTutorial.route) {
            NoMercyTutorialScreen(
                onBackToSelection = { navController.popBackStack() },
                onComplete = { mode ->
                    currentGameMode = mode
                    navController.navigate(Routes.MainScreen.route)
                    showComingSoon = true
                }
            )
        }

        // Carrera de puntos
        composable(Routes.PointsRaceTutorial.route) {
            PointsRaceTutorialScreen(
                onBackToSelection = { navController.popBackStack() },
                onComplete = { mode ->
                    currentGameMode = mode
                    navController.navigate(Routes.MainScreen.route)
                    showComingSoon = true
                }
            )
        }
    }
}
