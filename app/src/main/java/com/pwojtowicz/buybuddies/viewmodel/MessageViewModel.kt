package com.pwojtowicz.buybuddies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwojtowicz.buybuddies.ui.message.AppMessage
import com.pwojtowicz.buybuddies.ui.message.MessageHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageHandler: MessageHandler
) : ViewModel() {
    val messageFlow = messageHandler.messageFlow

    fun showMessage(message: AppMessage) {
        viewModelScope.launch {
            messageHandler.showMessage(message)
        }
    }
}