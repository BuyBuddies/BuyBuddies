package com.pwojtowicz.buybuddies.ui.screens.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.ui.components.user.SideUserProfile

@Composable
fun HomeTopContainer(modifier: Modifier, onProfileClick: () -> Unit) {
    val height = 100.dp

    Row(
        modifier
            .padding(0.dp, 10.dp, 0.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        SideUserProfile(height = height, onProfileClick)
        Spacer(Modifier.weight(1f))
        BuyBuddiesLogo(height = height)
        Spacer(Modifier.weight(0.1f))
    }
}

@Preview
@Composable
fun HomeTopContainerPreview() {
    MaterialTheme{
        HomeTopContainer(modifier = Modifier.fillMaxSize(), {})
    }
}
