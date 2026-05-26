package com.drivermate.ph

data class BookingInfo(
    val appName: String,
    val title: String,
    val message: String,
    val pickup: String = "",
    val dropoff: String = "",
    val fare: String = "",
    val rawText: String
)

object BookingParser {

    fun parse(appName: String, title: String?, text: String?): BookingInfo {
        val safeTitle = title.orEmpty()
        val safeText = text.orEmpty()
        val raw = "$safeTitle $safeText".trim()

        val fare = extractFare(raw)

        return BookingInfo(
            appName = appName,
            title = safeTitle,
            message = safeText,
            fare = fare,
            rawText = raw
        )
    }

    private fun extractFare(raw: String): String {
        val patterns = listOf(
            Regex("₱\\s?\\d+[,.]?\\d*"),
            Regex("PHP\\s?\\d+[,.]?\\d*", RegexOption.IGNORE_CASE),
            Regex("\\b\\d+[,.]?\\d*\\s?pesos\\b", RegexOption.IGNORE_CASE)
        )

        for (pattern in patterns) {
            val match = pattern.find(raw)
            if (match != null) return match.value
        }

        return ""
    }
}
