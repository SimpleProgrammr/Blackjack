package space.badluck.blackjack

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import space.badluck.blackjack.Managers.CardManager
import space.badluck.blackjack.Managers.ScreenManager
import space.badluck.blackjack.screens.gameStatus

data class GlobalStorage(
    var theme: ColorThemes.THEME = ColorThemes.THEME.DARK,
    var tableColor: Color = ColorThemes.TableColors.RED,
    val screenManager: MutableState<ScreenManager>,
    val cardManager: MutableState<CardManager> = mutableStateOf(CardManager()),
    var currentBet: Long = 100,
    var topScore: Long = 0,
    var currentScore: Long = 1000,
    var playerStand: Boolean = false,
    val runningGameAction: MutableState<gameStatus> = mutableStateOf(gameStatus.GAME_TAKE_BETS)
)


