package com.pwojtowicz.buybuddies.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel(
){
    private val _isMenuProfileExpanded = MutableStateFlow(false)
    val isMenuProfileExpanded = _isMenuProfileExpanded.asStateFlow()


    fun toggleMenu() {
        _isMenuProfileExpanded.value = !_isMenuProfileExpanded.value
    }
}