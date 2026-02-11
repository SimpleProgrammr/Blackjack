package space.badluck.blackjack

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import space.badluck.blackjack.Managers.SCREENS
import space.badluck.blackjack.Managers.ScreenManager
import space.badluck.blackjack.screens.GameScreen
import space.badluck.blackjack.screens.WinningScreen

@Composable
@Preview
fun App() {

    var globalStorage by remember { mutableStateOf(GlobalStorage(screenManager = mutableStateOf(ScreenManager(SCREENS.GAME)))) }

        MaterialTheme(
            colorScheme = when (globalStorage.theme){
                ColorThemes.THEME.LIGHT -> lightColorScheme(
                    background = ColorThemes.lightColorTheme.BACKGROUND_COLOUR,
                    surface = ColorThemes.lightColorTheme.BACKGROUND_COLOUR,
                    onBackground = ColorThemes.lightColorTheme.BASIC_TEXT_COLOUR,
                    onSurface = ColorThemes.lightColorTheme.BASIC_TEXT_COLOUR,
                    primary = ColorThemes.lightColorTheme.BUTTON_COLOUR,
                    onPrimary = ColorThemes.lightColorTheme.BUTTON_TEXT_COLOUR,
                    outline = ColorThemes.lightColorTheme.OUTLINE_COLOUR
                )
                ColorThemes.THEME.DARK -> darkColorScheme(
                    background = ColorThemes.darkColorTheme.BACKGROUND_COLOUR,
                    surface = ColorThemes.darkColorTheme.BACKGROUND_COLOUR,
                    onBackground = ColorThemes.darkColorTheme.BASIC_TEXT_COLOUR,
                    onSurface = ColorThemes.darkColorTheme.BASIC_TEXT_COLOUR,
                    primary = ColorThemes.darkColorTheme.BUTTON_COLOUR,
                    onPrimary = ColorThemes.darkColorTheme.BUTTON_TEXT_COLOUR,
                    outline = ColorThemes.darkColorTheme.OUTLINE_COLOUR
                );
            },

        ) {
            when (globalStorage.screenManager.value.currentScreen.value) {
                SCREENS.GREETING -> GreetingScreen(globalStorage)
                SCREENS.GAME -> GameScreen(globalStorage)
                SCREENS.WIN -> WinningScreen(globalStorage)
                else -> {}
            }
        }
}