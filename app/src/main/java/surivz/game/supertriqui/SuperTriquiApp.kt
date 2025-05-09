package surivz.game.supertriqui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import surivz.game.supertriqui.ui.navigation.Routes
import surivz.game.supertriqui.ui.dialog.ComingSoonDialog
import surivz.game.supertriqui.ui.screens.MainScreen
import surivz.game.supertriqui.ui.screens.game.ChaoticGameScreen
import surivz.game.supertriqui.ui.screens.game.ClassicGameScreen
import surivz.game.supertriqui.ui.screens.rules.ChaoticTutorialScreen
import surivz.game.supertriqui.ui.screens.rules.ClassicTutorialScreen
import surivz.game.supertriqui.ui.screens.rules.DominationTutorialScreen
import surivz.game.supertriqui.ui.screens.rules.NoTiesTutorialScreen
import surivz.game.supertriqui.ui.screens.rules.PointRaceTutorialScreen

@Composable
fun SuperTriquiApp() {
    val navController = rememberNavController()
    var showDifficultyDialog by remember { mutableStateOf(false) }
    var showComingSoon by remember { mutableStateOf(false) }
    var currentGameMode by remember { mutableStateOf("") }

    NavHost(
        navController = navController, startDestination = Routes.MainScreen.route
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

        composable(Routes.NoTiesTutorial.route) {
            NoTiesTutorialScreen(
                onBackToSelection = { navController.popBackStack() },
                onComplete = { mode ->
                    currentGameMode = mode
                    navController.navigate(Routes.MainScreen.route)
                    showComingSoon = true
                }
            )
        }

        composable(Routes.PointRaceTutorial.route) {
            PointRaceTutorialScreen(
                onBackToSelection = { navController.popBackStack() },
                onComplete = { mode ->
                    currentGameMode = mode
                    navController.navigate(Routes.MainScreen.route)
                    showComingSoon = true
                }
            )
        }

        composable(Routes.ClassicGame.route) {
            ClassicGameScreen(
                onBackToMain = { navController.popBackStack() }
            )
        }

        composable(Routes.ChaoticGame.route) {
            ChaoticGameScreen(
                onBackToMain = { navController.popBackStack() }
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
                }
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
                }
            )
        }
    }
}
