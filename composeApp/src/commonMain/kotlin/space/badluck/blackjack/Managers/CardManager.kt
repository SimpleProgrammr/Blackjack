package space.badluck.blackjack.Managers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import blackjack.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import kotlin.random.Random


class CardManager {
    private var _difficulty = mutableStateOf(1)

    fun getDifficultyLevel(): Int{
        return _difficulty.value
    }
    fun setDifficultyLevel(difficulty: Int) {
        _difficulty.value = difficulty
    }
    data class CARD(
        val name: String,
        val value: Int,
        val color: String,
        val image: Array<Int>,
    )

    private val _cardsUsage = arrayOf(
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0)
    )


    fun getRandomCard(): CARD{
        var cardValue = Random.nextInt(0, 13)
        var cardColor = Random.nextInt(0, 4)

        var tries = 0
        while(_cardsUsage[cardColor][cardValue] >= _difficulty.value){
            cardValue = Random.nextInt(0, 13)
            cardColor = Random.nextInt(0, 4)
            tries++
            if(tries>5){
                if(_cardsUsage.all { it.all { it >= _difficulty.value } }){
                    for (i in 0 until 4)
                        for (j in 0 until 13)
                            _cardsUsage[i][j] = 0
                }
            }
        }
        return CARD(
            name = getCardName(cardValue),
            color = getCardColor(cardColor),
            value = getCardValue(cardValue),
            image = arrayOf(cardColor,cardValue)
        )
    }

    fun getCardName(index: Int): String {
        return when(index){
            0 -> "Ace"
            1 -> "Two"
            2 -> "Three"
            3 -> "Four"
            4 -> "Five"
            5 -> "Six"
            6 -> "Seven"
            7 -> "Eight"
            8 -> "Nine"
            9 -> "Ten"
            10 -> "Jack"
            11 -> "King"
            12 -> "Queen"
            else -> "???"
        }
    }

    fun getCardColor(index: Int): String {
        return when(index){
            0 -> "Clubs"
            1 -> "Diamonds"
            2 -> "Res.drawable.hearts_"
            3 -> "Res.drawable.spades_"
            else -> "???"
        }
    }

    fun getCardValue(index: Int): Int{
        if(index == 0){
            return 11
        }
        if (index in 1..8){
            return index+1
        }
        return 10
    }

    @Composable
    fun getCardImage(colorIndex: Int, valueIndex: Int): DrawableResource {
        return when (colorIndex) {
            0 -> {
                when (valueIndex) {
                    0 -> Res.drawable.clubs_ace
                    1 -> Res.drawable.clubs_2
                    2 -> Res.drawable.clubs_3
                    3 -> Res.drawable.clubs_4
                    4 -> Res.drawable.clubs_5
                    5 -> Res.drawable.clubs_6
                    6 -> Res.drawable.clubs_7
                    7 -> Res.drawable.clubs_8
                    8 -> Res.drawable.clubs_9
                    9 -> Res.drawable.clubs_10
                    10 -> Res.drawable.clubs_jack
                    11 -> Res.drawable.clubs_king
                    12 -> Res.drawable.clubs_queen
                    else -> Res.drawable.card_placeholder

                }
            }

            1 -> {
                when (valueIndex) {
                    0 -> Res.drawable.diamonds_ace
                    1 -> Res.drawable.diamonds_2
                    2 -> Res.drawable.diamonds_3
                    3 -> Res.drawable.diamonds_4
                    4 -> Res.drawable.diamonds_5
                    5 -> Res.drawable.diamonds_6
                    6 -> Res.drawable.diamonds_7
                    7 -> Res.drawable.diamonds_8
                    8 -> Res.drawable.diamonds_9
                    9 -> Res.drawable.diamonds_10
                    10 -> Res.drawable.diamonds_jack
                    11 -> Res.drawable.diamonds_king
                    12 -> Res.drawable.diamonds_queen
                    else -> Res.drawable.card_placeholder
                }
            }

            2 -> {
                when (valueIndex) {
                    0 -> Res.drawable.hearts_ace
                    1 -> Res.drawable.hearts_2
                    2 -> Res.drawable.hearts_3
                    3 -> Res.drawable.hearts_4
                    4 -> Res.drawable.hearts_5
                    5 -> Res.drawable.hearts_6
                    6 -> Res.drawable.hearts_7
                    7 -> Res.drawable.hearts_8
                    8 -> Res.drawable.hearts_9
                    9 -> Res.drawable.hearts_10
                    10 -> Res.drawable.hearts_jack
                    11 -> Res.drawable.hearts_king
                    12 -> Res.drawable.hearts_queen
                    else -> Res.drawable.card_placeholder
                }
            }

            3 -> {
                when (valueIndex) {
                    0 -> Res.drawable.spades_ace
                    1 -> Res.drawable.spades_2
                    2 -> Res.drawable.spades_3
                    3 -> Res.drawable.spades_4
                    4 -> Res.drawable.spades_5
                    5 -> Res.drawable.spades_6
                    6 -> Res.drawable.spades_7
                    7 -> Res.drawable.spades_8
                    8 -> Res.drawable.spades_9
                    9 -> Res.drawable.spades_10
                    10 -> Res.drawable.spades_jack
                    11 -> Res.drawable.spades_king
                    12 -> Res.drawable.spades_queen
                    else -> Res.drawable.card_placeholder
                }
            }

            else -> Res.drawable.card_placeholder
        }

    }
}