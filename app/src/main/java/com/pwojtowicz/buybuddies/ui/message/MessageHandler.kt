package com.pwojtowicz.buybuddies.ui.message

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageHandler @Inject constructor() {
    private val _messageFlow = MutableSharedFlow<AppMessage>()
    val messageFlow = _messageFlow.asSharedFlow()

    suspend fun showMessage(message: AppMessage) {
        _messageFlow.emit(message)
    }
}