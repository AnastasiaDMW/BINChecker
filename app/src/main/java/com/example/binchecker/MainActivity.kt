package com.example.binchecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.binchecker.ui.screen.history.HistoryViewModel
import com.example.binchecker.ui.screen.home.HomeViewModel
import com.example.binchecker.ui.theme.BINCheckerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val homeViewModel: HomeViewModel by viewModels()
            val historyViewModel: HistoryViewModel by viewModels()
            BINCheckerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BINCheckerApp(homeViewModel, historyViewModel)
                }
            }
        }
    }
}