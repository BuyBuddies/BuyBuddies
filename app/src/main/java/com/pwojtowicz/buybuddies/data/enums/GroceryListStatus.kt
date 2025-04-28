package com.pwojtowicz.buybuddies.data.enums

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_card_clr_light
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_error
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_error_outline
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_success
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_success_outline
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_warning
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_warning_outline

enum class GroceryListStatus {
    ACTIVE,
    DROPPED,
    DONE;


    fun getColor(): Color {
        return when (this) {
            ACTIVE -> Color.Green
            DROPPED -> Color.Red
            DONE -> Color.Gray
        }
    }

    /**
     * Returns the enum name in Title Case format
     */
    fun getFormattedName(): String {
        return name.lowercase().replaceFirstChar { it.uppercase() }
    }

    companion object {
        /**
         * Returns a Composable that shows the appropriate status indicator
         * based on the status name provided as a string
         */
        @Composable
        fun getStatusIndicator(statusName: String, modifier: Modifier = Modifier) {
            val status = try {
                valueOf(statusName)
            } catch (e: IllegalArgumentException) {
                null
            }

            when (valueOf(statusName)) {
                ACTIVE -> ActiveStatusIndicator(modifier)
                DROPPED -> DroppedStatusIndicator(modifier)
                DONE -> DoneStatusIndicator(modifier)
                null -> FallbackStatusIndicator(modifier)
            }

        }

        /**
         * Get color for the status provided as String
         */
        fun getColorForStatus(statusName: String): Color {
            return try {
                valueOf(statusName).getColor()
            } catch (e: IllegalArgumentException) {
                Color.Magenta
            }
        }

        /**
         * Format status name to Title Case format
         */
        fun getFormattedStatusName(statusName: String): String {
            return try {
                valueOf(statusName).getFormattedName()
            } catch (e: IllegalArgumentException) {
                statusName
            }
        }

    }
}

/**
 * Composable for Active status: green outline with a white check inside
 */
@Composable
private fun ActiveStatusIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(bb_theme_card_clr_light, CircleShape)
            .aspectRatio(1f)
            .border(1.5.dp, bb_theme_success, CircleShape)
            .size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Active",
            tint = bb_theme_success,
            modifier = Modifier.size(12.dp)
        )
    }
}

/**
 * Composable for Dropped status: red circle with a white cross inside
 */
@Composable
private fun DroppedStatusIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .size(24.dp)
            .background(bb_theme_error, CircleShape)
            .border(1.5.dp, bb_theme_error_outline, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Dropped",
            tint = bb_theme_card_clr_light,
            modifier = Modifier.size(12.dp)
        )
    }
}

/**
 * Composable for Done status: green circle with darker outline and white check inside
 */
@Composable
private fun DoneStatusIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .size(24.dp)
            .background(bb_theme_success, CircleShape)
            .border(1.5.dp, bb_theme_success_outline, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Done",
            tint = bb_theme_card_clr_light,
            modifier = Modifier.size(12.dp)
        )
    }
}

/**
 * Composable for wrong input.
 */
@Composable
private fun FallbackStatusIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .size(24.dp)
            .background(bb_theme_warning, shape = CircleShape)
            .border(1.5.dp, bb_theme_warning_outline, CircleShape),
    )
}

@Preview(showBackground = true)
@Composable
fun StatusIndicatorsPreview() {
    val size = 12.dp
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Active:")
            ActiveStatusIndicator(modifier = Modifier.size(size))

            Spacer(modifier = Modifier.height(16.dp))

            Text("Dropped:")
            DroppedStatusIndicator(modifier = Modifier.size(size))

            Spacer(modifier = Modifier.height(16.dp))

            Text("Done:")
            DoneStatusIndicator(modifier = Modifier.size(size))

            Spacer(modifier = Modifier.height(16.dp))

            Text("Invalid Status:")
            FallbackStatusIndicator(modifier = Modifier.size(size))
        }
    }
}