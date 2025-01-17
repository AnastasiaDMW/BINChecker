package com.example.binchecker.ui.screen.history

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.binchecker.R
import com.example.binchecker.ui.screen.home.BankInfoContent
import com.example.binchecker.ui.screen.home.CardInfoContent
import com.example.binchecker.ui.screen.home.CountryInfoContent
import com.example.binchecker.ui.theme.BINCheckerTheme

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
) {
    BINCheckerTheme {
        Scaffold(
            modifier = modifier
        ) { innerPadding ->
            HistoryBody(
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun HistoryBody(modifier: Modifier) {
    var cardBIN by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val list = listOf<String>()

    Column(
        modifier = modifier.padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.history),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            BINContentCard(R.color.purple_1, mapOf())
            BINContentCard(R.color.purple_2, mapOf())
        }
//        LazyColumn {
//            items(list) {
//                BINContentCard(R.color.purple_1, mapOf())
//            }
//        }
    }
}

@Composable
fun BINContentCard(color: Int, data: Map<String, String>) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(color),
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "4534534951650121",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
//            Spacer(modifier = Modifier.height(12.dp))
//            CardInfoContent(data)
//            Spacer(modifier = Modifier.height(8.dp))
//            CountryInfoContent(data)
//            Spacer(modifier = Modifier.height(8.dp))
//            BankInfoContent(data)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BINContentCardPreview() {
    BINCheckerTheme {
        BINContentCard(R.color.purple_1, mapOf())
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryBodyPreview() {
    BINCheckerTheme {
        HistoryBody(Modifier.fillMaxSize())
    }
}