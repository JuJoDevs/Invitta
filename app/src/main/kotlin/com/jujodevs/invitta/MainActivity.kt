package com.jujodevs.invitta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jujodevs.invitta.ui.theme.InvittaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvittaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TemporalScreen(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun TemporalScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        Text(
            text = "INVITTA",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    InvittaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            TemporalScreen(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
