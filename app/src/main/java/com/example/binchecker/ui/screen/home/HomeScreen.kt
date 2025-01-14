package com.example.binchecker.ui.screen.home

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.binchecker.R
import com.example.binchecker.ui.theme.BINCheckerTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    BINCheckerTheme {
        Scaffold(
            modifier = modifier
        ) { innerPadding ->
            HomeBody(
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBody(modifier: Modifier) {
    val colors = OutlinedTextFieldDefaults. colors(
        unfocusedBorderColor = colorResource(R.color.purple_5),
        focusedBorderColor = colorResource(R.color.purple_5)
    )
    var cardBIN by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.home),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.ic_round_history),
                    contentDescription = stringResource(R.string.history)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = cardBIN,
                singleLine = true,
                interactionSource = interactionSource,
                onValueChange = { cardBIN = it },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            ) { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = cardBIN,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    interactionSource = interactionSource,
                    visualTransformation = VisualTransformation.None,
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

            Spacer(modifier = Modifier.height(40.dp))

            BINContentCard(modifier = Modifier, R.color.purple_1, TypeCardInfo.Card, mapOf())
            BINContentCard(modifier = Modifier, R.color.purple_2, TypeCardInfo.Country, mapOf())
            BINContentCard(modifier = Modifier, R.color.purple_1, TypeCardInfo.Bank, mapOf())

            Spacer(modifier = Modifier.height(40.dp))
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
fun BINContentCard(
    modifier: Modifier,
    color: Int,
    typeCardInfo: TypeCardInfo,
    data: Map<String, String>
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
                    CardInfoContent(data)
                }
                is TypeCardInfo.Country -> {
                    CountryInfoContent(data)
                }
                is TypeCardInfo.Bank -> {
                    BankInfoContent(data)
                }
            }
        }
    }
}

@Composable
fun CardInfoContent(data: Map<String, String>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.scheme),
            fontWeight = FontWeight.Bold
        )
        Text(text = "visa")
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.type),
            fontWeight = FontWeight.Bold
        )
        Text(text = "debit")
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.brand),
            fontWeight = FontWeight.Bold
        )
        Text(text = "Visa/Dankort")
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.prepaid),
            fontWeight = FontWeight.Bold
        )
        Text(text = "Нет")
    }
}

@Composable
fun CountryInfoContent(data: Map<String, String>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.country),
            fontWeight = FontWeight.Bold
        )
        Text(text = "Denmark")
    }
}

@Composable
fun BankInfoContent(data: Map<String, String>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.bank),
            fontWeight = FontWeight.Bold
        )
        Text(text = " Jyske Bank")
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.url),
            fontWeight = FontWeight.Bold
        )
        Text(text = " www.jyskebank.dk")
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.phone),
            fontWeight = FontWeight.Bold
        )
        Text(text = " +4589893300")
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.city),
            fontWeight = FontWeight.Bold
        )
        Text(text = " Hjorring")
    }
}

@Preview(showBackground = true)
@Composable
fun BINContentCardPreview() {
    BINCheckerTheme {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            BINContentCard(Modifier.fillMaxWidth(), R.color.purple_1, TypeCardInfo.Bank, mapOf())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    BINCheckerTheme {
        HomeBody(Modifier.fillMaxSize())
    }
}