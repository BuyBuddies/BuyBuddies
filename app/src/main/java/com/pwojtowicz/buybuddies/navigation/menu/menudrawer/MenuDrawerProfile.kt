package com.pwojtowicz.buybuddies.navigation.menu.menudrawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.ui.components.user.ProfilePicture
import com.pwojtowicz.buybuddies.ui.components.user.UsernameText
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_light

@Composable
fun MenuDrawerProfile(
    onNavToProfile: () -> Unit,
    onLogout: () -> Unit,
    isGuestMode: Boolean,
    username: String,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 15.dp)
                .clickable {
                    if(!isGuestMode) { isExpanded = !isExpanded }
                    else { onLogout() } },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfilePicture(height = 40.dp, width = 40.dp)
                Spacer(modifier = Modifier.width(10.dp))
                UsernameText(
                    fontSize = 20.sp,
                    username = username,
                    textColor = bb_theme_text_clr_light
                )
            }

            if(!isGuestMode) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.2f))
                        .padding(4.dp)
                )
            }

        }

        MenuDrawerDivider()

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            if(!isGuestMode) {
                DrawerProfileItems(
                    onNavToProfile = onNavToProfile,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF009688)
@Composable
fun MenuDrawerProfilePreview() {
    Box(){
        MenuDrawerProfile(
            onNavToProfile = {},
            username = "testName",
            isGuestMode = false,
            onLogout = {}
        )
    }
}


