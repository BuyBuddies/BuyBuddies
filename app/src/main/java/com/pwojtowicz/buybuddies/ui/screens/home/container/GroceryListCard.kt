package com.pwojtowicz.buybuddies.ui.screens.home.container

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_card_border_clr
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_card_clr_light
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_white

@Composable
fun GroceryListCard(
    modifier: Modifier,
    groceryList: GroceryList
) {
    val roundedCornerShapeSize = 24.dp

    Card(
        modifier = modifier
            .fillMaxWidth(0.25f)
            .height(180.dp)
            .padding(8.dp)
            .border(
                width = 2.dp,
                color = bb_theme_card_border_clr,
                shape = RoundedCornerShape(roundedCornerShapeSize )
                )
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(roundedCornerShapeSize),
            )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .background(bb_theme_main_color),
                verticalAlignment = Alignment .CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    modifier = Modifier.padding(6.dp, 2.dp),
                    text = groceryList.name,
                    color = bb_theme_text_clr_white,
                    style = TextStyle(
                        fontSize = 12.sp,
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bb_theme_card_clr_light)
            ) {}
            Spacer(modifier = Modifier.height(60.dp)) // Placeholder for card content
        }
    }
}


@Preview(showBackground = true, widthDp = 125,
    heightDp = 175)
@Composable
fun GroceryListCardPreview() {
    val groceryList = GroceryList(name = "test")
    GroceryListCard(
        Modifier,
        groceryList
    )
}


