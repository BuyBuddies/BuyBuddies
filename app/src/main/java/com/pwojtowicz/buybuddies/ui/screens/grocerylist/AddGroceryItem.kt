package com.pwojtowicz.buybuddies.ui.screens.grocerylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit
import com.pwojtowicz.buybuddies.ui.components.BBOutlinedTextField
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_card_clr_light
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_dark
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroceryItem(
    isVisible: MutableStateFlow<Boolean>,
    onDismiss: () -> Unit,
    onAddItem: (String, Double, MeasurementUnit) -> Unit
) {
    val isCardVisible by isVisible.collectAsState()
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    var selectedUnit by remember { mutableStateOf(MeasurementUnit.PIECE) }
    val unitOptions = MeasurementUnit.values()
    var selectedUnitOption by remember { mutableStateOf(unitOptions[0]) }

    var isError by remember { mutableStateOf(false) }

    if (isCardVisible) {
        ModalBottomSheet(
            containerColor = bb_theme_card_clr_light,
            scrimColor = Color(0x33000000),
            onDismissRequest = {
                isVisible.value = false
                onDismiss()
            }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f)
                    .padding(
                        start = 32.dp,
                        end = 32.dp,
                        bottom = 8.dp
                    )
            ) {
                Text(
                    text = "Add New Item",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        color = bb_theme_text_clr_dark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                LazyColumn(
                    Modifier.weight(1.0f)
                ) {
                    item {
                        BBOutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            value = itemName,
                            onValueChange = {
                                itemName = it
                                isError = it.isEmpty()
                            },
                            isError = isError,
                            label = { Text(if (isError) "Item Name - Required" else "Item Name") }
                        )
                    }
                    item {
                        BBOutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            value = itemQuantity,
                            onValueChange = { itemQuantity = it },
                            isError = false,
                            label = { Text("Quantity") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    item {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = "Unit",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FlowRow(
                                mainAxisSpacing = 8.dp,
                                crossAxisSpacing = 8.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                unitOptions.forEach { unit ->
                                    OutlinedButton(
                                        onClick = { selectedUnit = unit },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = if (selectedUnit == unit) ButtonDefaults.outlinedButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        ) else ButtonDefaults.outlinedButtonColors(),
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    ) {
                                        Text(
                                            unit.toShortString(),
                                            fontSize = 14.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(
                    Modifier.height(16.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = {
                            isError = false
                            isVisible.value = false
                            onDismiss()
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .height(48.dp)
                            .weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (itemName.isBlank()) {
                                isError = true
                            } else {
                                val quantity = itemQuantity.toDoubleOrNull() ?: 0.0
                                onAddItem(itemName, quantity, selectedUnit)
                                isVisible.value = false
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .height(48.dp)
                            .weight(1f)
                    ) {
                        Text("Add Item")
                    }
                }
            }
        }
    }
}