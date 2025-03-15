package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_background_clr_light

@Composable
fun ContainerCard(
    modifier: Modifier = Modifier,
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    shape: CornerBasedShape = RoundedCornerShape(2.dp),
    contentPadding: Dp = 16.dp,
    colors: CardColors = CardDefaults.cardColors(
        bb_theme_background_clr_light),
    content: @Composable () -> Unit
){
    Card(
        modifier = modifier,
        elevation = elevation,
        shape = shape,
        colors = colors
    ) {
        Box(
             modifier = Modifier.padding(contentPadding)
        ){
            content.invoke()
        }
    }
}

@Preview
@Composable
fun ContainerCardPreview() {
    MaterialTheme {
        ContainerCard(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(10, 10, 0, 0),
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = "Preview", fontSize = 22.sp)
            }
        }
    }
}