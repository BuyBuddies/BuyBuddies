package com.pwojtowicz.buybuddies.ui.screens.home.container

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.enums.GroceryListStatus
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_card_border_clr
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_card_clr_light
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_white

@Composable
fun GroceryListCard(
    modifier: Modifier,
    groceryList: GroceryList
) {
    val roundedCornerShapeSize = 12.dp
    val headerPadding = 8.dp
    val headerHeight = 35.dp

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(bb_theme_main_color)
                    .padding(start = headerPadding, end = headerPadding)
            ) {

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterStart)
                ){
                    GroceryListStatus.getStatusIndicator(
                        statusName = groceryList.listStatus,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = groceryList.name,
                    color = bb_theme_text_clr_white,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
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


@Preview(
    showBackground = true,
    widthDp = 125,
    heightDp = 175
)
@Composable
fun GroceryListCardPreview() {
    val groceryList = GroceryList(name = "test", listStatus = GroceryListStatus.DONE.name)
    GroceryListCard(
        Modifier,
        groceryList
    )
}


