package space.badluck.blackjack

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Blackjack",
    ) {
        App()
    }
}