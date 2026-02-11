package space.badluck.blackjack.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import blackjack.composeapp.generated.resources.Res
import blackjack.composeapp.generated.resources.you_win_text
import org.jetbrains.compose.resources.painterResource
import space.badluck.blackjack.GlobalStorage

@Composable
fun WinningScreen(globalStorage: GlobalStorage) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().clickable { globalStorage.screenManager.value.goBack() }
            .safeContentPadding(), verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource( Res.drawable.you_win_text),
                contentDescription = "You won",
                alignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().size(190.dp)
            )
            Text("Click anywhere to go back")
        }
    }
}