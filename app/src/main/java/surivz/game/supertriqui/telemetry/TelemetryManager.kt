package surivz.game.supertriqui.telemetry

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class TelemetryManager(private val context: Context) {

    private val cacheFile: File = File(context.cacheDir, "telemetry_cache.jsonl")
    private val endpoint = "http://31.97.11.175/api/stats"

    suspend fun logEvent(event: TelemetryEvent) {
        val jsonLine = Json.encodeToString(event) + "\n"
        cacheFile.appendText(jsonLine)

        if (isNetworkAvailable()) {
            flushEvents()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private suspend fun flushEvents() = withContext(Dispatchers.IO) {
        if (!cacheFile.exists()) return@withContext

        val lines = cacheFile.readLines()
        val successfulLines = mutableListOf<String>()

        for (line in lines) {
            try {
                val connection = URL(endpoint).openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                connection.outputStream.write(line.toByteArray())

                if (connection.responseCode == 200) {
                    successfulLines.add(line)
                }

                connection.disconnect()
            } catch (_: Exception) {
            }
        }

        // Elimina los que sí se enviaron
        if (successfulLines.isNotEmpty()) {
            val remaining = lines - successfulLines.toSet()
            cacheFile.writeText(remaining.joinToString("\n") + "\n")
        }
    }
}
