package com.drivermate.ph

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationReaderService : NotificationListenerService() {

    private lateinit var voiceAlertManager: VoiceAlertManager

    override fun onCreate() {
        super.onCreate()
        voiceAlertManager = VoiceAlertManager(this)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null) return

        val packageName = sbn.packageName ?: return
        val notification = sbn.notification ?: return
        val extras = notification.extras ?: return

        val title = extras.getCharSequence("android.title")?.toString().orEmpty()
        val text = extras.getCharSequence("android.text")?.toString().orEmpty()
        val bigText = extras.getCharSequence("android.bigText")?.toString().orEmpty()

        val fullText = listOf(title, text, bigText)
            .filter { it.isNotBlank() }
            .joinToString(" ")

        if (fullText.isBlank()) return

        if (!isPossibleDriverBooking(packageName, fullText)) return

        val booking = BookingParser.parse(
            appName = packageName,
            title = title,
            text = fullText
        )

        val alertMessage = RouteMatcher.buildAlertMessage(booking)

        voiceAlertManager.speak(alertMessage)
    }

    private fun isPossibleDriverBooking(packageName: String, text: String): Boolean {
        val lowerPackage = packageName.lowercase()
        val lowerText = text.lowercase()

        val driverAppPackageHints = listOf(
            "lalamove",
            "grab",
            "joyride",
            "toktok",
            "transportify",
            "angkas",
            "foodpanda",
            "delivery"
        )

        val bookingTextHints = listOf(
            "booking",
            "pickup",
            "pick up",
            "dropoff",
            "drop off",
            "delivery",
            "fare",
            "₱",
            "php",
            "order",
            "new job",
            "new trip"
        )

        val fromDriverApp = driverAppPackageHints.any { lowerPackage.contains(it) }
        val looksLikeBooking = bookingTextHints.any { lowerText.contains(it) }

        return fromDriverApp || looksLikeBooking
    }

    override fun onDestroy() {
        voiceAlertManager.shutdown()
        super.onDestroy()
    }
}
