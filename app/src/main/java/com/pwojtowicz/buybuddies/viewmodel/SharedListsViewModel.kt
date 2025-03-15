package com.pwojtowicz.buybuddies.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedListsViewModel @Inject constructor() : ViewModel() {
    private val _refreshTrigger = MutableStateFlow(0)
    val refreshTrigger = _refreshTrigger.asStateFlow()

    fun triggerRefresh() {
        _refreshTrigger.value += 1
    }
}