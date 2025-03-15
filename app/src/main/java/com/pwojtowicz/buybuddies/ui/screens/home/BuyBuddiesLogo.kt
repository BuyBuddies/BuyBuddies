package com.pwojtowicz.buybuddies.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.R

@Composable
fun BuyBuddiesLogo(height: Dp){
    val width = (height.value * 2.25).dp

    Box(contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier
                .height(height)
                .width(width),
            painter = painterResource(id = R.drawable.bb_logo),
            contentDescription = "Logo"
        )
    }
}