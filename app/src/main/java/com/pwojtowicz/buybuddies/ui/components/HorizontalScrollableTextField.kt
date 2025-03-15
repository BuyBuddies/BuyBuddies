package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_selected_clr

@Composable
fun HorizontalScrollableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = bb_theme_main_selected_clr,
    fontSize: TextUnit = 16.sp,
    height: Dp = 40.dp
) {
    Box(
        modifier = modifier
            .height(height)
            .background(Color(0x4DFFFFFF))
            .then(
                Modifier.drawBehind {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            )
    ) {
        val scrollState = rememberScrollState()
        val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = textColor,
                fontSize = fontSize
            ),
            singleLine = true,
            onTextLayout = { textLayoutResult.value = it },
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )

        LaunchedEffect(value) {
            textLayoutResult.value?.let { textLayout ->
                val lastCursorOffset = textLayout.getHorizontalPosition(value.length, true)
                scrollState.animateScrollTo(lastCursorOffset.toInt())
            }
        }
    }
}
