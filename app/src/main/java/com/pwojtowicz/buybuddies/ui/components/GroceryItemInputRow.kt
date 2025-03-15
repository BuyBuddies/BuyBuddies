package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.ui.theme.*

@Composable
fun GroceryItemInputRow(onItemAdded: (String, Int) -> Unit, onCancel: () -> Unit) {
    var itemName by remember { mutableStateOf("") }
    var itemCount by remember { mutableStateOf("") }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .weight(3f),
                label = { Text("Item Name", style = TextStyle(fontSize = 14.sp)) },
                value = itemName,
                onValueChange = { itemName = it },
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                modifier = Modifier
                    .weight(1f),
                placeholder = { Text(text = "Amount", style = TextStyle(fontSize = 14.sp)) },
                value = itemCount,
                onValueChange = { itemCount = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier
                    .weight(0.40f)
                    .height(32.dp),
                shape = RectangleShape,
                onClick = onCancel,
                ) {
                Icon(Icons.Default.Close, contentDescription = "Cancel", tint = LightGray)
            }
            Spacer(Modifier.width(8.dp))
            Button(
                modifier = Modifier
                    .weight(0.40f)
                    .height(32.dp),
                shape = RectangleShape,
                onClick = { onItemAdded(itemName, itemCount.toIntOrNull() ?: 0) },
            ) {
                Icon(Icons.Default.Check, contentDescription = "Add", tint = LightGray)
            }
        }
    }
}


@Preview
@Composable
fun GroceryItemInputRowPreview(){
    GroceryItemInputRow(
        onItemAdded = { name, count -> {} },
        onCancel = {}
    )
}