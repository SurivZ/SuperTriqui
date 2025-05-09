package surivz.game.supertriqui.logic

enum class Player {
    X, O;

    override fun toString(): String = when (this) {
        X -> "X"
        O -> "O"
    }
}

fun Player.opponent(): Player = when (this) {
    Player.X -> Player.O
    Player.O -> Player.X
}
