package com.ruben.apiremota.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruben.apiremota.data.PokemonRepository
import com.ruben.apiremota.ui.screens.RollsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RollsViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _uiState : MutableStateFlow<RollsScreenState> = MutableStateFlow(
        RollsScreenState.Loading)
    val uiState: StateFlow<RollsScreenState> = _uiState.asStateFlow()

    fun getRolls(){
        viewModelScope.launch{
            val rolls = repository.getRolls()
            _uiState.value = RollsScreenState.Success(rolls)
        }
    }
}