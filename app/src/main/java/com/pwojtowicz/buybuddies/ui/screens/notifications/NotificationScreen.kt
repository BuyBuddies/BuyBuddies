package com.pwojtowicz.buybuddies.ui.screens.notifications


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.navigation.NavItems
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_background_clr_light
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color

@Composable
fun NotificationScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(bb_theme_background_clr_light)
    ){
        Column(){
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(bb_theme_main_color)
                .padding(20.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "NotificationScreen", fontSize = 22.sp)
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = NavItems.Notification.icon,
                    contentDescription = "icon",
                    modifier = Modifier.height(60.dp)
                )

            }
            Row(modifier = Modifier
                .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                Text(text = "PLACE FOR NOTIFICATIONS", fontSize = 32.sp)

            }

        }
    }
}