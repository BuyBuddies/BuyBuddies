package com.pwojtowicz.buybuddies.ui.components.user


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwojtowicz.buybuddies.ui.components.ContainerCard
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_selected_clr
import com.pwojtowicz.buybuddies.viewmodel.AuthViewModel

@Composable
fun SideUserProfile(
    height: Dp = 100.dp,
    onProfileClick: () -> Unit
) {
    ContainerCard(
        modifier = Modifier
            .height(height * 0.8f)
            .width(height * 1.0f)
            .clickable { onProfileClick() },
        colors = CardDefaults.cardColors (bb_theme_main_selected_clr),
        shape = RoundedCornerShape(0, 10, 10 ,0),
        contentPadding = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            ProfilePicture(height = height * 0.4f, width= height * 0.4f)
            UsernameText()
        }
    }
}


@Preview
@Composable
fun SideUserProfilePreview(){
    MaterialTheme{
        SideUserProfile(height = 100.dp, {})
    }
}