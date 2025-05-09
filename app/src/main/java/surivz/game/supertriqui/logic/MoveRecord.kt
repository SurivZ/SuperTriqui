package surivz.game.supertriqui.logic

data class MoveRecord(
    val player: Player,
    val moveNumber: Int,
    val boardIndex: Int,
    val cellIndex: Int,
    val resultedInBoardWin: Boolean,
    val resultedInBoardDraw: Boolean
)
