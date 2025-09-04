package com.danielbelmonte.cryptoapp.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danibelmonte.cryptoapp.domain.entity.CryptoBo
import com.danibelmonte.cryptoapp.domain.useCase.CryptoCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UiState(
    val loading: Boolean = true,
    val error: String? = null,
    val cryptos: List<CryptoBo> = emptyList(),
    val fromCrypto: CryptoBo? = null,
    val toCrypto: CryptoBo? = null,
    val amount: String = "1.0",
    val convertedAmount: String = "0.0",
    val isFromDropdownExpanded: Boolean = false,
    val isToDropdownExpanded: Boolean = false,
    val searchQuery: String = "",
    val filteredCryptos: List<CryptoBo> = emptyList()
)

@HiltViewModel
class CryptoConverterViewModel @Inject constructor(
    private val useCase: CryptoCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCryptos()
        print("coins loaded: ${uiState.value.cryptos}")
    }

    fun loadCryptos() {
        viewModelScope.launch {
            val cryptos = useCase.getCryptoList()
            _uiState.update { it.copy(cryptos = cryptos) }
        }
    }


    fun onFromCryptoSelect(crypto: CryptoBo) {
        _uiState.update { it.copy(fromCrypto = crypto, isFromDropdownExpanded = false) }
        updateConversion()
    }

    fun onToCryptoSelect(crypto: CryptoBo) {
        _uiState.update { it.copy(toCrypto = crypto, isToDropdownExpanded = false) }
        updateConversion()
    }

    fun onAmountChange(amount: String) {
        _uiState.update { it.copy(amount = amount.ifEmpty { "0" }) }
        updateConversion()
    }

    fun swapCurrencies() {
        _uiState.update { state ->
            state.copy(
                fromCrypto = state.toCrypto,
                toCrypto = state.fromCrypto,
                amount = state.convertedAmount.ifEmpty { "0" },
                convertedAmount = state.amount
            )
        }
    }

    fun onFromDropdownExpanded(expanded: Boolean) {
        _uiState.update { it.copy(isFromDropdownExpanded = expanded) }
    }

    fun onToDropdownExpanded(expanded: Boolean) {
        _uiState.update { it.copy(isToDropdownExpanded = expanded) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filteredCryptos = filterCryptos(state.cryptos, query)
            )
        }
    }

    fun setFrom(symbol: String) {
        val current = _uiState.value
        val picked = current.cryptos.find { it.symbol.equals(symbol, ignoreCase = true) }
        if (picked != null) _uiState.update { it.copy(fromCrypto = picked) }
    }

    fun setTo(symbol: String) {
        val current = _uiState.value
        val picked = current.cryptos.find { it.symbol.equals(symbol, ignoreCase = true) }
        if (picked != null) _uiState.update { it.copy(toCrypto = picked) }
    }


    private fun updateConversion() {
        val state = _uiState.value
        val fromCrypto = state.fromCrypto ?: return
        val toCrypto = state.toCrypto ?: return
        val amount = state.amount.toDoubleOrNull() ?: 0.0

        if (amount <= 0) {
            _uiState.update { it.copy(convertedAmount = "0.0") }
            return
        }

        // Convert through USD as intermediate currency
        val amountInUsd = amount * fromCrypto.usd.price
        val convertedAmount = if (toCrypto.usd.price > 0) amountInUsd / toCrypto.usd.price else 0.0

        _uiState.update {
            it.copy(convertedAmount = String.format("%.8f", convertedAmount).trimEnd('0').trimEnd('.'))
        }
    }

    private fun filterCryptos(list: List<CryptoBo>, query: String): List<CryptoBo> {
        if (query.isBlank()) return list
        return list.filter {
            it.symbol.contains(query, ignoreCase = true) ||
                it.name.contains(query, ignoreCase = true)
        }
    }
}
