package surivz.game.supertriqui.telemetry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val mode: String,
    @SerialName("vs_ai") val vsAI: Boolean,
    @SerialName("ai_level") val aiLevel: String?,
    val result: String,
    @SerialName("duration_ms") val durationMillis: Long,
    @SerialName("timestamp") val timestamp: Long = System.currentTimeMillis(),
    @SerialName("move_count") val moveCount: Int,
    val moves: List<MoveRecord> = emptyList()
)
