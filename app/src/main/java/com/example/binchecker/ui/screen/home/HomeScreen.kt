package com.example.binchecker.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.binchecker.R
import com.example.binchecker.data.database.entity.CardInfo
import com.example.binchecker.data.network.dto.BankDto
import com.example.binchecker.data.network.dto.CardInfoDto
import com.example.binchecker.data.network.dto.CountryDto
import com.example.binchecker.data.network.dto.toBankDto
import com.example.binchecker.data.network.dto.toCardInfoDto
import com.example.binchecker.data.network.dto.toCountryDto
import com.example.binchecker.ui.theme.BINCheckerTheme

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateToHistoryScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BINCheckerTheme {
        Scaffold(
            modifier = modifier
        ) { innerPadding ->
            HomeBody(
                homeViewModel,
                navigateToHistoryScreen,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBody(
    homeViewModel: HomeViewModel,
    navigateToHistoryScreen: () -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val data by homeViewModel.uiState.collectAsState()
    val colors = OutlinedTextFieldDefaults. colors(
        unfocusedBorderColor = colorResource(R.color.purple_5),
        focusedBorderColor = colorResource(R.color.purple_5)
    )
    var cardBIN by remember { mutableStateOf(TextFieldValue("")) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.home),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(64.dp))
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = cardBIN,
                singleLine = true,
                interactionSource = interactionSource,
                onValueChange = {
                    val input = it.text.replace(" ", "")

                    if (input.isEmpty() || (input.length <= 8 && input.last().isDigit())) {
                        if (input.length >= 6) {
                            if (!homeViewModel.isSendRequest) {
                                homeViewModel.searchCard(input)
                            }
                        }
                        val result = formattedBINText(input)
                        cardBIN = TextFieldValue(result.toString(), selection = TextRange(result.length))
                    }
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            ) { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = cardBIN.text,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = {
                        Text(text = stringResource(R.string.bin_input),
                            color = Color.Black)
                    },
                    container = {
                        Container(
                            enabled = true,
                            isError = false,
                            interactionSource = interactionSource,
                            colors = colors,
                            shape = RoundedCornerShape(8.dp),
                            focusedBorderThickness = 2.dp,
                            unfocusedBorderThickness = 2.dp,
                        )
                    }
                )
            }

            when (data) {
                is UIState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LoadingScreen()
                    }
                }
                is UIState.Error -> {
                    val errorMessage = (data as UIState.Error).error
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
                is UIState.SuccessCard -> {
                    val card = (data as UIState.SuccessCard).curCard
                    if (card != null) {
                        Spacer(modifier = Modifier.height(40.dp))
                        BINSearchContentCard(modifier = Modifier, R.color.purple_1, TypeCardInfo.Card, card)
                        BINSearchContentCard(modifier = Modifier, R.color.purple_2, TypeCardInfo.Country, card)
                        BINSearchContentCard(modifier = Modifier, R.color.purple_1, TypeCardInfo.Bank, card)
                    }
                }
            }

        }
        Column {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.purple_5)
                ),
                onClick = {
                    navigateToHistoryScreen()
                }
            ) {
                Text(
                    stringResource(R.string.btn_history),
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun BINSearchContentCard(
    modifier: Modifier,
    color: Int,
    typeCardInfo: TypeCardInfo,
    cardInfo: CardInfo
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(color),
        )
    ) {
        Column(
            modifier = modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            when(typeCardInfo) {
                is TypeCardInfo.Card -> {
                    CardInfoContent(cardInfo.toCardInfoDto())
                }
                is TypeCardInfo.Country -> {
                    CountryInfoContent(cardInfo.toCountryDto())
                }
                is TypeCardInfo.Bank -> {
                    BankInfoContent(cardInfo.toBankDto())
                }
            }
        }
    }
}

@Composable
fun CardInfoContent(data: CardInfoDto) {
    if (data.scheme != "") {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.scheme),
                fontWeight = FontWeight.Bold
            )
            Text(text = " ${data.scheme}")
        }
    }
    if (data.type != "") {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.type),
                fontWeight = FontWeight.Bold
            )
            Text(text = " ${data.type}")
        }
    }
    if (data.brand != "") {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.brand),
                fontWeight = FontWeight.Bold
            )
            Text(text = " ${data.brand}")
        }
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.prepaid),
            fontWeight = FontWeight.Bold
        )
        val prepaidText = if (data.prepaid) "Да" else "Нет"
        Text(text = " $prepaidText")
    }
}

@Composable
fun CountryInfoContent(data: CountryDto) {
    if (data.name != "") {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.country),
                fontWeight = FontWeight.Bold
            )
            Text(text = " ${data.name}")
        }
    }
    if (data.emoji != "") {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.flag_img),
                fontWeight = FontWeight.Bold
            )
            Text(text = " ${data.emoji}")
        }
    }
    if (data.alpha2 != "") {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.word_code),
                fontWeight = FontWeight.Bold
            )
            Text(text = " ${data.alpha2}")
        }
    }
    if (data.numeric != "") {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.country_code),
                fontWeight = FontWeight.Bold
            )
            Text(text = " ${data.numeric}")
        }
    }
}

@Composable
fun BankInfoContent(data: BankDto) {
    if (data.name != "") {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.bank),
                fontWeight = FontWeight.Bold
            )
            Text(text = " ${data.name}")
        }
    }
    if (data.url != "") {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.url),
                fontWeight = FontWeight.Bold
            )
            Text(text = " ${data.url}")
        }
    }
    if (data.phone != "") {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.phone),
                fontWeight = FontWeight.Bold
            )
            Text(text = " ${data.phone}")
        }
    }
    if (data.city != "") {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.city),
                fontWeight = FontWeight.Bold
            )
            Text(text = " ${data.city}")
        }
    }
}

@Composable
fun LoadingScreen() {
    Card(
        modifier = Modifier.width(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.purple_2)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ErrorCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Red
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
                text = stringResource(R.string.error),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorCardPreview() {
    BINCheckerTheme {
        ErrorCard("Такой карты нет.")
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    BINCheckerTheme {
        LoadingScreen()
    }
}

private fun formattedBINText(input: String): StringBuilder {
    val formattedText = StringBuilder()
    for (i in input.indices) {
        if (i > 0 && i % 4 == 0) {
            formattedText.append(" ")
        }
        formattedText.append(input[i])
    }
    return formattedText
}