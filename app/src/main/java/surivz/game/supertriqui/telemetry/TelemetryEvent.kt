package surivz.game.supertriqui.telemetry

import kotlinx.serialization.Serializable

@Serializable
data class TelemetryEvent(
    val mode: String,
    val vsAI: Boolean,
    val aiLevel: String?,
    val result: String, // "X", "O", "draw"
    val durationMillis: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val moveCount: Int
)

