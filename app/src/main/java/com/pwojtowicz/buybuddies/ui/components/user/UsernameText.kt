package com.pwojtowicz.buybuddies.ui.components.user

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.R
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_light

@Composable
fun UsernameText(
    username: String,
    fontSize: TextUnit = 16.sp,
    textColor: Color = bb_theme_text_clr_light
){
//TODO: change the user name shown to being send in as argument

//    val currentUser by authViewModel.currentUser.collectAsState()
//    val displayName = currentUser?.email?.let { email ->
//        val truncatedEmail = email.takeWhile { char ->
//            char != '@' && char != '.' && char != '_'
//        }
//        truncatedEmail.take(10)
//    } ?: ""

    val rampartOneFamily = FontFamily(Font(R.font.rampart_one_regular))
    Text(
        text = username,
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
    UsernameText(username = "Username")
}