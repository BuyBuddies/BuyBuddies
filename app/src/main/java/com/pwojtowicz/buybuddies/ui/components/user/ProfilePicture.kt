package com.pwojtowicz.buybuddies.ui.components.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.pwojtowicz.buybuddies.R


@Composable
fun ProfilePicture(height: Dp, width: Dp) {
    Box(contentAlignment = Alignment.Center) {
        Image(
            modifier = androidx.compose.ui.Modifier
                .height(height)
                .width(width),
            painter = painterResource(id = R.drawable.bb_default_propfile_picture),
            contentDescription = "Logo"
        )
    }
}