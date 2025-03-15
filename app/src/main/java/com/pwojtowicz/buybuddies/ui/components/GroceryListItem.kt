package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListStatus

@Composable
fun GroceryListItem(
    groceryList: GroceryList,
    onItemClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 12.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
            .height(80.dp)
            .clickable(onClick = onItemClick) // Corrected usage of clickable
    ) {
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(60f)
                    .fillMaxHeight()
                    .padding(start = 10.dp)

            ){
                GroceryListName(groceryList.name)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(30f)
                    .fillMaxHeight()
            ){
                GroceryListDate(groceryList.createdAt)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(10f)
                    .fillMaxHeight()
            ){
                GroceryListStatus(groceryList.listStatus)
            }
        }
    }
}
@Composable
fun GroceryListName(name: String){
    if (name.length > 20)
        Text(textAlign = TextAlign.Center, text = name.substring(0, 20) + "...")
    else
        Text(textAlign = TextAlign.Center, text = name)
}

@Composable
fun GroceryListDate(date: String) {
        Text(text = date)
}

@Composable
fun GroceryListStatus(status: String) {
    val color = when (status) {
        GroceryListStatus.ACTIVE.name -> Color.Yellow
        GroceryListStatus.DROPPED.name -> Color.Red
        GroceryListStatus.DONE.name -> Color.Green
        else -> Color.Gray
    }
    Box(modifier = Modifier
        .background(color, CircleShape)
        .size(24.dp)
        .padding(4.dp)
    )
}



@Preview(showBackground = true)
@Composable
fun PreviewGroceryItemComponent() {
    GroceryListItem(
        groceryList = GroceryList(
            name = "Apples",
            createdAt = "30-12-2023",
            listStatus = GroceryListStatus.DROPPED.toString()
        ),
        onItemClick = {}
    )
}

