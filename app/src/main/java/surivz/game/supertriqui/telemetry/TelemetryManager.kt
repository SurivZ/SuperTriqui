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
    private val endpoint = "http://31.97.11.175:81/api/game"

    suspend fun logEvent(event: Game) {
        val jsonLine = Json.encodeToString(event) + "\n"

        if (isNetworkAvailable()) if (sendEvent(jsonLine)) return

        cacheFile.appendText(jsonLine)

        if (isNetworkAvailable()) flushEvents()
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private suspend fun sendEvent(line: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val connection = URL(endpoint).openConnection() as HttpURLConnection
            connection.apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                connectTimeout = 5000
                readTimeout = 5000
                doOutput = true
            }

            connection.outputStream.use { it.write(line.toByteArray()) }

            val success = connection.responseCode == 201
            connection.disconnect()
            success
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun flushEvents() = withContext(Dispatchers.IO) {
        if (!cacheFile.exists()) return@withContext

        val lines = cacheFile.readLines().toMutableList()
        if (lines.isEmpty()) return@withContext

        val failedToSend = mutableListOf<String>()

        for (line in lines) {
            val success = sendEvent(line)
            if (!success) failedToSend.add(line)
        }

        cacheFile.writeText(failedToSend.joinToString("\n") + if (failedToSend.isNotEmpty()) "\n" else "")
    }
}
