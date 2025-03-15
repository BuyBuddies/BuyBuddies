package com.pwojtowicz.buybuddies.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.R
import com.pwojtowicz.buybuddies.ui.theme.BuyBuddiesTheme


@Composable
fun ProfileTopContainer(){
    val height = 140.dp
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp, 30.dp, 20.dp, 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Column{
            AccountNameBox("Account Name")
            Row(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ){
                AccountNameBox("ChooseAvatar")
            }
        }
    }


}


@Composable
fun AccountNameBox(accountName: String){
    val museoModernoRegular = FontFamily(Font(R.font.museo_moderno_regular))
    Box(
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier,
                text = accountName,
                style = TextStyle(
                    fontFamily = museoModernoRegular,
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                thickness = 1.dp,
                modifier = Modifier.padding(30.dp, 0.dp)
            )
        }
    }
}

@Preview
@Composable
fun ProfileTopContainerPreview() {
    BuyBuddiesTheme {
        Surface {
            ProfileTopContainer()
        }
    }
}