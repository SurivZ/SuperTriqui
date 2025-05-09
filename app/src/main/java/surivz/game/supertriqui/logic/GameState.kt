package surivz.game.supertriqui.logic

interface GameState {
    val boards: List<SmallBoardState>
    val nextBoard: Int
    val currentPlayer: Player
    val winner: Player?
    fun isGameOver(): Boolean
}
