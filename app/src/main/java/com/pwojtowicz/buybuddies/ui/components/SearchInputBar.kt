package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_input_clr_light

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInputBar(
    isExpanded: Boolean = true,
    onSearchInput: (String) -> Unit,
//    onExpandChange: (Boolean) -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {  }
            )
            .height(56.dp)
            .padding(horizontal = 16.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = bb_theme_input_clr_light,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
        ),
        shape = RoundedCornerShape(50),
        placeholder = { Text(text = "Search", color = Color.Gray) },
        singleLine = true,
        value = searchText,
        onValueChange = { newText ->
            searchText = newText
            onSearchInput(newText)
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )

        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        )
    )
}
