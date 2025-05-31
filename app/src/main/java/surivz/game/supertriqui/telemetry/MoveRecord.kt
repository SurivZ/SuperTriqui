package surivz.game.supertriqui.telemetry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoveRecord(
    @SerialName("move_number") val moveNumber: Int,
    val player: String,
    @SerialName("board_index") val boardIndex: Int,
    @SerialName("cell_index") val cellIndex: Int,
    @SerialName("resulted_in_board_win") val resultedInBoardWin: Boolean,
    @SerialName("resulted_in_board_draw") val resultedInBoardDraw: Boolean
)
