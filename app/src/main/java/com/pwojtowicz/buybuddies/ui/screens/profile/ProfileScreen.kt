package com.pwojtowicz.buybuddies.ui.screens.profile

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pwojtowicz.buybuddies.BuyBuddiesApplication
import com.pwojtowicz.buybuddies.ui.components.ContainerCard
@Composable
fun ProfileScreen(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    Box(modifier = Modifier.padding(paddingValues)){
        Column() {
            ProfileTopContainer()
            ContainerCard(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(10, 10, 0, 0),
            ) {
                Row(modifier = Modifier
                    .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center){
                    Text(text = "Future User Features", fontSize = 32.sp)
                }
            }
        }
    }
}