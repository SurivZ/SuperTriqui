package surivz.game.supertriqui.ui.navigation

sealed class Routes(val route: String) {
    data object MainScreen : Routes("main_screen")

    data object ClassicTutorial : Routes("classic_tutorial")
    data object ChaoticTutorial : Routes("chaotic_tutorial")
    data object DominationTutorial : Routes("domination_tutorial")
    data object NoTiesTutorial : Routes("no_ties_tutorial")
    data object PointRaceTutorial : Routes("point_race_tutorial")

    data object ClassicGame : Routes("classic_game")
    data object ChaoticGame : Routes("chaotic_game")

    data object ClassicGameVsAI : Routes("classic_game_vs_ai/{aiLevel}") {
        fun createRoute(aiLevel: String) = "classic_game_vs_ai/$aiLevel"
    }

    data object ChaoticGameVsAI : Routes("chaotic_game_vs_ai/{aiLevel}") {
        fun createRoute(aiLevel: String) = "chaotic_game_vs_ai/$aiLevel"
    }
}
