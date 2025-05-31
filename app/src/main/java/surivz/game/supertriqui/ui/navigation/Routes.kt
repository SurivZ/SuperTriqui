package surivz.game.supertriqui.ui.navigation

sealed class Routes(val route: String) {
    data object MainScreen : Routes("main_screen")

    data object ClassicTutorial : Routes("classic_tutorial")
    data object ChaoticTutorial : Routes("chaotic_tutorial")
    data object DominationTutorial : Routes("domination_tutorial")
    data object NoMercyTutorial : Routes("no_mercy_tutorial")
    data object PointsRaceTutorial : Routes("points_race_tutorial")

    data object ClassicGame : Routes("classic_game")
    data object ChaoticGame : Routes("chaotic_game")
    data object DominationGame : Routes("domination_game")
    data object NoMercyGame : Routes("no_mercy_game")
    data object PointsRaceGame : Routes("points_race_game")

    data object ClassicGameVsAI : Routes("classic_game_vs_ai/{aiLevel}") {
        fun createRoute(aiLevel: String) = "classic_game_vs_ai/$aiLevel"
    }

    data object ChaoticGameVsAI : Routes("chaotic_game_vs_ai/{aiLevel}") {
        fun createRoute(aiLevel: String) = "chaotic_game_vs_ai/$aiLevel"
    }

    data object DominationGameVsAI : Routes("domination_game_vs_ai/{aiLevel}") {
        fun createRoute(aiLevel: String) = "domination_game_vs_ai/$aiLevel"
    }

    data object NoMercyGameVsAI : Routes("no_mercy_game_vs_ai/{aiLevel}") {
        fun createRoute(aiLevel: String) = "no_mercy_game_vs_ai/$aiLevel"
    }

    data object PointsRaceGameVsAI : Routes("points_race_game_vs_ai/{aiLevel}") {
        fun createRoute(aiLevel: String) = "points_race_game_vs_ai/$aiLevel"
    }
}
