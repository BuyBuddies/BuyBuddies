package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BBOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        isError = isError,
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = if (isError) Color.Red else Color.Gray,
            cursorColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.Red,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.DarkGray,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = Color.Gray
        )
    )
}