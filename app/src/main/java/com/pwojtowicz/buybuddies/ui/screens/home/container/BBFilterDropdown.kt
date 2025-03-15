
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_background_clr_light
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_dark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BBFilterDropdown(
    selectedValue: String?,
    onValueChange: (String?) -> Unit,
    options: List<String>,
    label: String,
    modifier: Modifier = Modifier,
    textColor: Color = bb_theme_text_clr_dark,
    backgroundColor: Color = bb_theme_background_clr_light,
    dropdownItemColor: Color = bb_theme_text_clr_dark,
    dropdownBackgroundColor: Color = bb_theme_background_clr_light,
    labelColor: Color = bb_theme_text_clr_dark,
    textStyle: TextStyle = LocalTextStyle.current,
    labelStyle: TextStyle = LocalTextStyle.current.copy(color = labelColor)
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedValue ?: "All",
            onValueChange = {},
            label = { Text(text = label, style = labelStyle) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            textStyle = textStyle.copy(color = textColor),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedLabelColor = labelColor,
                unfocusedLabelColor = labelColor
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(dropdownBackgroundColor)
        ) {
            options.forEach { option: String ->
                DropdownMenuItem(
                    text = { Text(text = option, color = dropdownItemColor) },
                    onClick = {
                        expanded = false
                        onValueChange(option)
                    }
                )
            }
            DropdownMenuItem(
                text = { Text(text = "All", color = dropdownItemColor) },
                onClick = {
                    expanded = false
                    onValueChange(null)
                }
            )
        }
    }
}