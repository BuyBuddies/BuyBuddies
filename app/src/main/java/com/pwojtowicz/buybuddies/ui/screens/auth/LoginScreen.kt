import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.R
import com.pwojtowicz.buybuddies.auth.SignInState
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_background_light
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_reject_red
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_dark
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_gray
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
    onCleanError: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.signInError) {
        if (state.signInError != null) {
            delay(5000)
            onCleanError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bb_theme_main_color)
    ) {
        AnimatedVisibility(
            visible = state.signInError != null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 64.dp),
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it }
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = bb_theme_reject_red.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = state.signInError ?: "",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bb_logo),
                    contentDescription = "Buy Buddies Logo",
                    modifier = Modifier
                        .size(320.dp)
                        .padding(24.dp),
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = bb_theme_background_light
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome_message),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = bb_theme_text_clr_dark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Sign in to continue to Buy Buddies",
                        style = MaterialTheme.typography.bodyLarge,
                        color = bb_theme_text_clr_gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    if (state.isLoading) {
                        CircularProgressIndicator(color = bb_theme_main_color)
                    } else {
                        ElevatedButton(
                            onClick = onSignInClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = bb_theme_main_color,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.bb_default_propfile_picture),
                                    contentDescription = "Google logo",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Sign in with Google",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}