package com.pwojtowicz.buybuddies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.Identity
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.navigation.Navigation
import com.pwojtowicz.buybuddies.ui.message.AppMessageHandler
import com.pwojtowicz.buybuddies.ui.theme.BuyBuddiesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BuyBuddiesTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Navigation()
                    }

                    AppMessageHandler(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}