package com.pwojtowicz.buybuddies.navigation.menu.menudrawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_light

@Composable
fun DrawerProfileItems(
    onNavToProfile: () -> Unit,
    onLogout: () -> Unit
) {
    Column {
        DrawerProfileItem(
            text = "Profile",
            icon = Icons.Default.AccountCircle,
            onClick = onNavToProfile
        )

        DrawerProfileItem(
            text = "Logout",
            icon = Icons.Default.KeyboardArrowLeft,
            onClick = onLogout
        )
    }
}

@Composable
private fun DrawerProfileItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = bb_theme_text_clr_light
        )
        Spacer(Modifier.width(16.dp))
        Text(
            fontSize = 12.sp,
            text = text,
            color = bb_theme_text_clr_light
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MenuDrawerProfileItems(){
    DrawerProfileItems(onLogout = {}, onNavToProfile = {})
}