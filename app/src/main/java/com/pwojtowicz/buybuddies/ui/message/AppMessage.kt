package com.pwojtowicz.buybuddies.ui.message

sealed class AppMessage {
    data class Error(val message: String) : AppMessage()
    data class Success(val message: String) : AppMessage()
    data class Info(val message: String) : AppMessage()
}
