package com.drivermate.ph

import android.content.Intent
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Locale

class MainActivity : ComponentActivity() {

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale("en", "PH")
            }
        }

        setContent {
            DriverMateHome(
                onOpenNotificationSettings = {
                    startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                },
                onTestVoice = {
                    tts?.speak(
                        "DriverMate PH is ready. Preferred booking alerts will be read aloud.",
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "test_voice"
                    )
                }
            )
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}

@Composable
fun DriverMateHome(
    onOpenNotificationSettings: () -> Unit,
    onTestVoice: () -> Unit
) {
    val darkBg = Color(0xFF0F172A)
    val cardBg = Color(0xFF1E293B)
    val green = Color(0xFF22C55E)
    val yellow = Color(0xFFFACC15)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "DriverMate PH",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Smarter trips. Better earnings.",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Setup Required",
                            color = yellow,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Allow notification access so DriverMate can read booking alerts from your driver apps.",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onOpenNotificationSettings,
                            colors = ButtonDefaults.buttonColors(containerColor = green),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Allow Notification Access", color = Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Voice Alert Test",
                            color = green,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Test if your phone can speak booking alerts clearly.",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedButton(
                            onClick = onTestVoice,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Test Voice Alert", color = Color.White)
                        }
                    }
                }
            }

            Text(
                text = "Version 1.0.0 • Safe notification assistant",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
