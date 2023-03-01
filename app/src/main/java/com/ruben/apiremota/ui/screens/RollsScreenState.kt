package com.ruben.apiremota.ui.screens

sealed class RollsScreenState {
    object Loading: RollsScreenState()
    data class Success(val rolls:Int): RollsScreenState()
}