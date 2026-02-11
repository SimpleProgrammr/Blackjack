package space.badluck.blackjack.Managers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class ScreenManager(startScreen: SCREENS) {

    private val _currentScreen = mutableStateOf(startScreen)
    val currentScreen: State<SCREENS> = _currentScreen

    private val _direction = mutableStateOf(DIRECTIONS.FORWARD)
    val direction: State<DIRECTIONS> = _direction

    private val screenStack: MutableList<SCREENS> = mutableListOf(startScreen)

    fun goTo(screen: SCREENS) {
        _direction.value = DIRECTIONS.FORWARD
        _currentScreen.value = screen
        screenStack.add(currentScreen.value)
    }

    fun goToAndClearStack(screen: SCREENS) {
        _direction.value = DIRECTIONS.JUMP
        _currentScreen.value = screen
        screenStack.clear()
        screenStack.add(currentScreen.value)
    }

    fun goBack() {
        if(screenStack.size > 1) {
            _direction.value = DIRECTIONS.BACK
            screenStack.removeLast()
            _currentScreen.value = screenStack.last()
        }
    }

}

enum class SCREENS{
    GREETING,
    GAME,
    WIN,
    LOSE,
}

enum class DIRECTIONS{
    FORWARD,
    BACK,
    JUMP
}