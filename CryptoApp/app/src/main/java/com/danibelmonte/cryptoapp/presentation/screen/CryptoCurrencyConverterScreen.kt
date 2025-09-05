package com.danibelmonte.cryptoapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danibelmonte.cryptoapp.presentation.viewmodel.CryptoPickerViewModel
import com.danielbelmonte.cryptoapp.presentation.screen.CryptoConverterViewModel
import java.text.NumberFormat
import java.util.Locale
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoCurrencyConverterScreen(
    viewModel: CryptoConverterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bg = Color(0xFF0F0F0F)
    val cardBg = Color(0xFF1B1B1B)
    val cardStroke = Color(0xFF2A2A2A)
    val hint = Color(0xFF9E9E9E)
    val accent = Color(0xFFE59A2F)

    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }

    var fromAmount by remember { mutableStateOf("") }
    var toAmount by remember { mutableStateOf("") }

    val fromSymbol: String = uiState.fromCrypto?.symbol ?: "--"
    val toSymbol: String = uiState.toCrypto?.symbol ?: "--"

    val fromPrice = uiState.fromCrypto?.usd?.price ?: 0.0
    val toPrice = uiState.toCrypto?.usd?.price ?: 0.0

    fun formatUsd(value: Double): String = NumberFormat.getCurrencyInstance(Locale.US).format(value)

    fun parseAmount(text: String): Double {
        val clean = text
            .replace("$", "")
            .replace(",", "")
            .replace(" ", "")
        return clean.toDoubleOrNull() ?: 0.0
    }

    fun formatNumber(value: Double, maxFraction: Int = 8): String {
        val pattern = buildString {
            append("#,##0")
            if (maxFraction > 0) {
                append(".")
                repeat(maxFraction) { append("#") }
            }
        }
        val df = DecimalFormat(pattern)
        return df.format(value)
    }

    LaunchedEffect(fromAmount, fromPrice, toPrice) {
        toAmount = if (toPrice <= 0.0) {
            "--"
        } else {
            val fromQty = parseAmount(fromAmount)
            val result = fromQty * (fromPrice / toPrice)
            formatNumber(result)
        }
    }
    LaunchedEffect(Unit) { viewModel.startAutoRefresh() }
    LaunchedEffect(fromSymbol, fromPrice) {
        if (fromAmount.isBlank() || fromAmount == "") {
            fromAmount = if (fromPrice == 0.0) "" else ("$fromPrice")
        }
    }

    Scaffold(
    ) { inner ->
        Text("coins: ${uiState.cryptos.size}")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(inner)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                CoinAmountCard(
                    amount = fromAmount,
                    onAmountChange = { fromAmount = it },
                    isEditable = true,
                    approxFiat = formatUsd(fromPrice),
                    symbol = fromSymbol,
                    onSymbolClick = { showFromPicker = true },
                    cardBg = cardBg,
                    cardStroke = cardStroke,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                DropdownMenu(
                    expanded = showFromPicker,
                    onDismissRequest = { showFromPicker = false }
                ) {
                    val coins = uiState.filteredCryptos.ifEmpty { uiState.cryptos }
                    if (coins.isEmpty()) {
                        DropdownMenuItem(text = { Text("No coins available") }, enabled = false, onClick = {})
                    }
                    coins.forEach { coin ->
                        DropdownMenuItem(
                            text = { Text("${coin.symbol} — ${coin.name}") },
                            onClick = {
                                viewModel.setFrom(coin.symbol)
                                showFromPicker = false
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    color = cardBg,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                CoinAmountCard(
                    amount = toAmount,
                    onAmountChange = {},
                    isEditable = false,
                    approxFiat = formatUsd(toPrice),
                    symbol = toSymbol,
                    onSymbolClick = { showToPicker = true },
                    cardBg = cardBg,
                    cardStroke = cardStroke,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                DropdownMenu(
                    expanded = showToPicker,
                    onDismissRequest = { showToPicker = false },
                ) {
                    if (uiState.cryptos.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No coins available") },
                            enabled = false,
                            onClick = { }
                        )
                    }
                    uiState.cryptos.forEach { coin ->
                        DropdownMenuItem(
                            text = { Text(text = "${coin.symbol} — ${coin.name}") },
                            onClick = {
                                viewModel.setTo(coin.symbol)
                                showToPicker = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    val tmpAmount = fromAmount
                    fromAmount = toAmount
                    toAmount = tmpAmount
                    viewModel.swapCurrencies()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accent),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Swap", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("⏱", color = hint, fontSize = 14.sp)
                Spacer(Modifier.width(8.dp))
                val secondsLeft by viewModel.secondsLeft.collectAsStateWithLifecycle()
                Text(
                     "refresh in ${secondsLeft}s", color = hint
                )
            }
        }

    }
}

@Composable
private fun CoinAmountCard(
    amount: String,
    onAmountChange: (String) -> Unit,
    isEditable: Boolean,
    approxFiat: String,
    symbol: String,
    onSymbolClick: () -> Unit,
    cardBg: Color,
    cardStroke: Color,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, cardStroke, RoundedCornerShape(12.dp)),
        color = cardBg
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { new ->
                        if (isEditable) {
                            onAmountChange(new)
                        }
                    },
                    readOnly = !isEditable,
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        cursorColor = Color.White
                    ),
                    keyboardOptions = keyboardOptions
                )
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(approxFiat, color = Color(0xFF8E8E8E), fontSize = 12.sp)
                    Spacer(Modifier.weight(1f))
                }
            }

            Spacer(Modifier.width(12.dp))

            CoinChip(symbol = symbol, onClick = onSymbolClick)
        }
    }
}

@Composable
private fun CoinChip(symbol: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF2A2A2A)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1677FF))
            )
            Spacer(Modifier.width(8.dp))
            Text(symbol, color = Color.White, fontWeight = FontWeight.SemiBold)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
