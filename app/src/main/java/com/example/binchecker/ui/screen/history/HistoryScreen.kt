package com.example.binchecker.ui.screen.history

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.binchecker.R
import com.example.binchecker.data.database.entity.CardInfo
import com.example.binchecker.data.network.dto.toBankDto
import com.example.binchecker.data.network.dto.toCardInfoDto
import com.example.binchecker.data.network.dto.toCountryDto
import com.example.binchecker.ui.screen.home.BankInfoContent
import com.example.binchecker.ui.screen.home.CardInfoContent
import com.example.binchecker.ui.screen.home.CountryInfoContent
import com.example.binchecker.ui.theme.BINCheckerTheme

@Composable
fun HistoryScreen(
    historyViewModel: HistoryViewModel,
    modifier: Modifier = Modifier
) {
    BINCheckerTheme {
        Scaffold(
            modifier = modifier
        ) { innerPadding ->
            HistoryBody(
                historyViewModel = historyViewModel,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun HistoryBody(
    historyViewModel: HistoryViewModel,
    modifier: Modifier
) {
    val data by historyViewModel.historyUiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        historyViewModel.getAllCards()
    }

    Column(
        modifier = modifier.padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.history),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        when (data) {
            is HistoryUIState.Loading -> {
                Column (
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
            is HistoryUIState.Error -> {
                val errorMessage = (data as HistoryUIState.Error).error
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            is HistoryUIState.SuccessCards -> {
                val cards = (data as HistoryUIState.SuccessCards).cards
                if (cards != emptyList<CardInfo>()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    LazyColumn {
                        items(cards.indices.toList()) { index ->
                            val color = if (index % 2 == 0) R.color.purple_1 else R.color.purple_2
                            BINContentCard(color, cards[index])
                        }
                    }
                } else {
                    Column (
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NoneHistoryCards()
                    }
                }
            }
        }
    }
}

@Composable
fun BINContentCard(color: Int, cardInfo: CardInfo) {
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
            Spacer(modifier = Modifier.height(12.dp))
            CardInfoContent(cardInfo.toCardInfoDto())
            Spacer(modifier = Modifier.height(8.dp))
            CountryInfoContent(cardInfo.toCountryDto())
            Spacer(modifier = Modifier.height(8.dp))
            BankInfoContent(cardInfo.toBankDto())
        }
    }
}

@Composable
fun NoneHistoryCards() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.purple_2)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.empty_list_history),
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoneHistoryCardPreview() {
    BINCheckerTheme {
        NoneHistoryCards()
    }
}