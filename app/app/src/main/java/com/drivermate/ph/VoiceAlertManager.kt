package com.drivermate.ph

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class VoiceAlertManager(context: Context) {

    private var tts: TextToSpeech? = null
    private var ready = false

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("en", "PH")
                tts?.setSpeechRate(0.95f)
                ready = true
            }
        }
    }

    fun speak(message: String) {
        if (!ready) return

        tts?.speak(
            message,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "driver_mate_alert"
        )
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
