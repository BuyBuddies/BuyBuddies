package com.pwojtowicz.buybuddies.ui.screens.scanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_background_clr_light

@Composable
fun ScannerScreen() {
    Box(modifier = Modifier
            .fillMaxSize()
            .background(bb_theme_background_clr_light)
    ){
        Column(){
            Row(modifier = Modifier
                .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                Text(text = "Future Scanner", fontSize = 32.sp)

            }

        }
    }
}