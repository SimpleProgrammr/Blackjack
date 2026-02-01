package space.badluck.blackjack

import androidx.compose.ui.graphics.Color

object ColorThemes {
    object darkColorTheme{
        val BACKGROUND_COLOUR = Color(43, 45, 48, 255)
        val BASIC_TEXT_COLOUR = Color(217, 217, 217, 255)
        val OUTLINE_COLOUR = Color(245, 134, 78, 255)
        val BUTTON_COLOUR = Color(235, 130, 77, 255)
        val BUTTON_TEXT_COLOUR = Color(0, 0, 0, 255)
    }

    object lightColorTheme{
        val BACKGROUND_COLOUR = Color(229, 229, 229, 255)
        val BASIC_TEXT_COLOUR = Color(29, 29, 29, 255)
        val OUTLINE_COLOUR = Color(255, 15, 112, 255)
        val BUTTON_COLOUR = Color(0, 137, 255, 255)
        val BUTTON_TEXT_COLOUR = Color(18, 231, 13, 255)
    }

    object TableColors{
        val GREEN = Color(19, 173, 0, 255)
        val RED = Color(204, 0, 0, 255)
    }

    enum class THEME {
        DARK,
        LIGHT,
    }
}
