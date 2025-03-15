package com.pwojtowicz.buybuddies.ui.components.user

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwojtowicz.buybuddies.R
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_light
import com.pwojtowicz.buybuddies.viewmodel.AuthViewModel

@Composable
fun UsernameText(
    name: String = "Username",
    fontSize: TextUnit = 16.sp,
    textColor: Color = bb_theme_text_clr_light,
    authViewModel: AuthViewModel = hiltViewModel()
){
    val currentUser by authViewModel.currentUser.collectAsState()
    val displayName = currentUser?.email?.let { email ->
        val truncatedEmail = email.takeWhile { char ->
            char != '@' && char != '.' && char != '_'
        }
        truncatedEmail.take(10)
    } ?: ""

    LaunchedEffect(Unit) {
        Log.d("UsernameText", "Raw email: ${currentUser?.email}")
        Log.d("UsernameText", "Raw user: $currentUser")
    }

    val rampartOneFamily = FontFamily(Font(R.font.rampart_one_regular))
    Text(
        text = displayName.toString(),
        style = TextStyle(
            fontFamily = rampartOneFamily,
            fontSize = fontSize,
            color = textColor
        )
    )
}
@Preview
@Composable
fun UsernameTextPreview() {
    UsernameText()
}