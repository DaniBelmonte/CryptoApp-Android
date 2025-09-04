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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danibelmonte.cryptoapp.presentation.viewmodel.CryptoPickerViewModel
import com.danielbelmonte.cryptoapp.presentation.screen.CryptoConverterViewModel

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
    val accent = Color(0xFFE59A2F) // button

    // UI state for dialogs / sheets
    var showEditFrom by remember { mutableStateOf(false) }
    var showEditTo by remember { mutableStateOf(false) }
    // showFromPicker and showToPicker are menus now
    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }

    var fromAmount by remember { mutableStateOf("23.4") }
    var toAmount by remember { mutableStateOf("0.059") }

    val fromSymbol: String = uiState.fromCrypto?.symbol ?: "--"
    val toSymbol: String = uiState.toCrypto?.symbol ?: "--"

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

            // FROM card + dropdown menu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                CoinAmountCard(
                    amount = fromAmount,
                    onAmountClick = { showEditFrom = true },
                    approxFiat = "~\$2,029",
                    symbol = fromSymbol,
                    balance = "Bal: 28.29",
                    onSymbolClick = { showFromPicker = true },
                    cardBg = cardBg,
                    cardStroke = cardStroke
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
                                viewModel.setTo(coin.symbol)
                                showFromPicker = false
                            }
                        )
                    }
                }
            }

            // Swap icon between cards
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
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }

            // TO card + dropdown menu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                CoinAmountCard(
                    amount = toAmount,
                    onAmountClick = { showEditTo = true },
                    approxFiat = "~\$56,483",
                    symbol = toSymbol,
                    balance = null,
                    onSymbolClick = { showToPicker = true },
                    cardBg = cardBg,
                    cardStroke = cardStroke
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

            // Swap button
            Button(
                onClick = {
                    val tmpAmount = fromAmount
                    fromAmount = toAmount
                    toAmount = tmpAmount
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
                Text("~2mins processing time", color = hint, fontSize = 14.sp)
            }

            // ===== Edit amount dialogs =====
            if (showEditFrom) {
                EditAmountDialog(
                    title = "Edit FROM amount",
                    current = fromAmount,
                    onDismiss = { showEditFrom = false },
                    onConfirm = { new -> fromAmount = new; showEditFrom = false }
                )
            }
            if (showEditTo) {
                EditAmountDialog(
                    title = "Edit TO amount",
                    current = toAmount,
                    onDismiss = { showEditTo = false },
                    onConfirm = { new -> toAmount = new; showEditTo = false }
                )
            }
        }
    }
}


@Composable
private fun EditAmountDialog(
    title: String,
    current: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { new ->
                    // allow only digits and decimal separator
                    if (new.isEmpty() || new.matches(Regex("^[0-9]*([.,][0-9]*)?$"))) {
                        text = new.replace(',', '.')
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
    )
}

@Composable
private fun CryptoListPicker(
    items: List<com.danibelmonte.cryptoapp.domain.entity.CryptoBo>,
    onPick: (com.danibelmonte.cryptoapp.domain.entity.CryptoBo) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Select a coin", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        items.forEach { coin ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPick(coin) }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1677FF))
                )
                Spacer(Modifier.width(8.dp))
                Text("${coin.symbol} — ${coin.name}", color = Color.White)
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun CoinAmountCard(
    amount: String,
    onAmountClick: () -> Unit,
    approxFiat: String,
    symbol: String,
    balance: String?,
    onSymbolClick: () -> Unit,
    cardBg: Color,
    cardStroke: Color
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
                Text(
                    text = amount,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(approxFiat, color = Color(0xFF8E8E8E), fontSize = 12.sp)
                    Spacer(Modifier.weight(1f))
                    if (balance != null) {
                        Text(balance, color = Color(0xFF8E8E8E), fontSize = 12.sp, overflow = TextOverflow.Ellipsis)
                    }
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
                .clickable { onClick() }
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
