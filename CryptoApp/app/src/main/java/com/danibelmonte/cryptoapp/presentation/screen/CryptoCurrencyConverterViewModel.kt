// presentation/viewmodel/CryptoPickerViewModel.kt
package com.danibelmonte.cryptoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.danibelmonte.cryptoapp.domain.useCase.CryptoCurrencyUseCase
import com.danibelmonte.cryptoapp.domain.entity.CryptoBo

@HiltViewModel
class CryptoPickerViewModel @Inject constructor(
    private val useCase: CryptoCurrencyUseCase
) : ViewModel() {

    data class UiState(
        val loading: Boolean = true,
        val error: String? = null,
        val query: String = "",
        val items: List<CryptoBo> = emptyList(),
        val filtered: List<CryptoBo> = emptyList(),
        val expanded: Boolean = false,
        val selected: CryptoBo? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            runCatching { useCase.getCryptoList() }
                .onSuccess { list ->
                    _uiState.update { st ->
                        st.copy(
                            loading = false,
                            items = list,
                            filtered = filter(list, st.query)
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(loading = false, error = e.message ?: "Error") }
                }
        }
    }

    fun onExpandedChange(expanded: Boolean) {
        _uiState.update { it.copy(expanded = expanded) }
    }

    fun onQueryChange(q: String) {
        _uiState.update { st ->
            st.copy(
                query = q,
                filtered = filter(st.items, q),
                expanded = true
            )
        }
    }

    fun onSelect(item: CryptoBo) {
        _uiState.update { it.copy(selected = item, expanded = false) }
    }

    private fun filter(list: List<CryptoBo>, q: String): List<CryptoBo> {
        if (q.isBlank()) return list
        return list.filter {
            it.symbol.contains(q, ignoreCase = true) ||
                it.name.contains(q, ignoreCase = true)
        }
    }
}
