package com.danielbelmonte.cryptoapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.danibelmonte.cryptoapp.domain.entity.CryptoBo

/**
 * Swap coin screen with cryptocurrency conversion functionality.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CryptoCurrencyConverterScreen(
    viewModel: CryptoConverterViewModel = hiltViewModel()
) {
    val bg = Color(0xFF0F0F0F)
    val cardBg = Color(0xFF1B1B1B)
    val cardStroke = Color(0xFF2A2A2A)
    val hint = Color(0xFF9E9E9E)
    val accent = Color(0xFFE59A2F)
    val error = Color(0xFFFF5252)

    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Show loading or error state if needed
    if (uiState.loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = accent)
        }
        return
    }

    uiState.error?.let { errorMsg ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = errorMsg,
                color = error,
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }

    Scaffold { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(inner)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // FROM card
            val fromCrypto = uiState.fromCrypto
            CoinAmountCard(
                amount = uiState.amount,
                onAmountChange = { viewModel.onAmountChange(it) },
                approxFiat = fromCrypto?.let { "~$${String.format("%,.2f", it.usd.price)}" } ?: "~$0.00",
                symbol = fromCrypto?.symbol ?: "",
                balance = fromCrypto?.let { "Bal: ${String.format("%.2f", it.circulatingSupply)}" } ?: "",
                onSymbolClick = { viewModel.onFromDropdownExpanded(true) },
                cardBg = cardBg,
                cardStroke = cardStroke
            )

            // Swap icon between cards
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        viewModel.swapCurrencies()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(cardBg, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Swap currencies",
                        tint = accent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // TO card
            val toCrypto = uiState.toCrypto
            CoinAmountCard(
                amount = uiState.convertedAmount,
                onAmountClick = { /* Not editable */ },
                approxFiat = toCrypto?.let { "~$${String.format("%,.2f", it.usd.price)}" } ?: "~$0.00",
                symbol = toCrypto?.symbol ?: "",
                balance = toCrypto?.let { "Bal: ${String.format("%.2f", it.circulatingSupply)}" } ?: "",
                onSymbolClick = { viewModel.onToDropdownExpanded(true) },
                cardBg = cardBg,
                cardStroke = cardStroke,
                isAmountEditable = false
            )

            Spacer(Modifier.height(20.dp))

            // Swap button
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.swapCurrencies()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accent),
                shape = RoundedCornerShape(8.dp),
                enabled = uiState.fromCrypto != null && uiState.toCrypto != null
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
                Text("Real-time conversion", color = hint, fontSize = 14.sp)
            }

            // Crypto selection dialogs
            if (uiState.isFromDropdownExpanded) {
                CryptoSelectionDialog(
                    title = "Select From Currency",
                    cryptos = uiState.filteredCryptos,
                    onDismiss = { viewModel.onFromDropdownExpanded(false) },
                    onCryptoSelected = { viewModel.onFromCryptoSelect(it) },
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                    accent = accent,
                    cardBg = cardBg,
                    cardStroke = cardStroke,
                    hint = hint
                )
            }

            if (uiState.isToDropdownExpanded) {
                CryptoSelectionDialog(
                    title = "Select To Currency",
                    cryptos = uiState.filteredCryptos,
                    onDismiss = { viewModel.onToDropdownExpanded(false) },
                    onCryptoSelected = { viewModel.onToCryptoSelect(it) },
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                    accent = accent,
                    cardBg = cardBg,
                    cardStroke = cardStroke,
                    hint = hint
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CoinAmountCard(
    amount: String,
    onAmountClick: (() -> Unit)? = null,
    onAmountChange: ((String) -> Unit)? = null,
    approxFiat: String,
    symbol: String,
    balance: String?,
    onSymbolClick: () -> Unit,
    cardBg: Color,
    cardStroke: Color,
    isAmountEditable: Boolean = true
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, cardStroke, RoundedCornerShape(12.dp)),
        color = cardBg
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Amount section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                if (isAmountEditable && onAmountChange != null) {
                    val focusRequester = remember { FocusRequester() }

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                                onAmountChange(newValue)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.White
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        )
                    )

                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                } else {
                    Text(
                        amount,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { onAmountClick?.invoke() }
                    )
                }

                Text(
                    approxFiat,
                    color = Color(0xFF9E9E9E),
                    fontSize = 14.sp
                )

                balance?.let {
                    Text(
                        it,
                        color = Color(0xFF9E9E9E),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Symbol section
            CoinChip(
                symbol = symbol,
                onClick = onSymbolClick,
                accent = Color(0xFFE59A2F)
            )
        }
    }
}

@Composable
private fun CoinChip(
    symbol: String,
    onClick: () -> Unit,
    accent: Color
) {
    Surface(
        color = Color(0xFF2A2A2A),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = symbol,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 4.dp)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select cryptocurrency",
                tint = accent,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun CryptoSelectionDialog(
    title: String,
    cryptos: List<CryptoBo>,
    onDismiss: () -> Unit,
    onCryptoSelected: (CryptoBo) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    accent: Color,
    cardBg: Color,
    cardStroke: Color,
    hint: Color
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f)
                .border(1.dp, cardStroke, RoundedCornerShape(16.dp)),
            color = cardBg,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Dialog title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    placeholder = { Text("Search cryptocurrencies") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = hint
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChanged("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = hint
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF2A2A2A),
                        unfocusedContainerColor = Color(0xFF2A2A2A),
                        focusedIndicatorColor = accent,
                        unfocusedIndicatorColor = hint,
                        cursorColor = accent
                    ),
                    singleLine = true
                )

                // Crypto list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cryptos) { crypto ->
                        CryptoItem(
                            crypto = crypto,
                            onClick = {
                                onCryptoSelected(crypto)
                                onDismiss()
                            },
                            accent = accent,
                            hint = hint
                        )
                    }

                    if (cryptos.isEmpty() && searchQuery.isNotEmpty()) {
                        item {
                            Text(
                                text = "No cryptocurrencies found",
                                color = hint,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CryptoItem(
    crypto: CryptoBo,
    onClick: () -> Unit,
    accent: Color,
    hint: Color
) {
    Surface(
        color = Color(0xFF2A2A2A),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = crypto.symbol,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = crypto.name,
                    color = hint,
                    fontSize = 14.sp
                )
            }

            Text(
                text = "$${String.format("%,.2f", crypto.usd.price)}",
                color = accent,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
