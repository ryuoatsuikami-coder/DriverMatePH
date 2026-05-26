package com.drivermate.ph

object RouteMatcher {

    private val preferredKeywords = listOf(
        "cavite",
        "imus",
        "dasma",
        "dasmariñas",
        "bacoor",
        "general trias",
        "gentri",
        "manila",
        "pasay",
        "makati",
        "taguig"
    )

    private val blockedKeywords = listOf(
        "cancelled",
        "canceled",
        "failed"
    )

    fun isPreferred(booking: BookingInfo): Boolean {
        val text = booking.rawText.lowercase()

        if (blockedKeywords.any { text.contains(it) }) {
            return false
        }

        return preferredKeywords.any { text.contains(it) }
    }

    fun buildAlertMessage(booking: BookingInfo): String {
        val preferred = isPreferred(booking)

        val prefix = if (preferred) {
            "Preferred booking."
        } else {
            "Booking alert."
        }

        val fareText = if (booking.fare.isNotBlank()) {
            "Fare ${booking.fare}."
        } else {
            ""
        }

        return "$prefix ${booking.rawText}. $fareText"
            .replace("\\s+".toRegex(), " ")
            .trim()
    }
}
