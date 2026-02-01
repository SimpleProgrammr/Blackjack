package space.badluck.blackjack.Managers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import blackjack.composeapp.generated.resources.*
import blackjack.composeapp.generated.resources.Res.drawable
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random


class CardManager {
    var difficulty = mutableStateOf(1)

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
        while(_cardsUsage[cardColor][cardValue] >= difficulty.value){
            cardValue = Random.nextInt(0, 13)
            cardColor = Random.nextInt(0, 4)
            tries++
            if(tries>5){
                if(_cardsUsage.all { it.all { it >= difficulty.value } }){
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
            0 -> "Ace";
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
            2 -> "Hearts"
            3 -> "Spades"
            else -> "???"
        }
    }

    fun getCardValue(index: Int): Int{
        if(index == 0){
            return 11;
        }
        if (index in 1..8){
            return index+1
        }
        return 10
    }

    @Composable
    fun getCardImage(colorIndex: Int, valueIndex: Int): Painter {
        return when (colorIndex) {
            0 -> {
                when (valueIndex) {
                    0 -> painterResource(drawable.clubs_ace)
                    1 -> painterResource(drawable.clubs_2)
                    2 -> painterResource(drawable.clubs_3)
                    3 -> painterResource(drawable.clubs_4)
                    4 -> painterResource(drawable.clubs_5)
                    5 -> painterResource(drawable.clubs_6)
                    6 -> painterResource(drawable.clubs_7)
                    7 -> painterResource(drawable.clubs_8)
                    8 -> painterResource(drawable.clubs_9)
                    9 -> painterResource(drawable.clubs_10)
                    10 -> painterResource(drawable.clubs_jack)
                    11 -> painterResource(drawable.clubs_king)
                    12 -> painterResource(drawable.clubs_queen)
                    else -> painterResource(drawable.card_placeholder_refresh)

                }
            }

            1 -> {
                when (valueIndex) {
                    0 -> painterResource(drawable.diamonds_ace)
                    1 -> painterResource(drawable.diamonds_2)
                    2 -> painterResource(drawable.diamonds_3)
                    3 -> painterResource(drawable.diamonds_4)
                    4 -> painterResource(drawable.diamonds_5)
                    5 -> painterResource(drawable.diamonds_6)
                    6 -> painterResource(drawable.diamonds_7)
                    7 -> painterResource(drawable.diamonds_8)
                    8 -> painterResource(drawable.diamonds_9)
                    9 -> painterResource(drawable.diamonds_10)
                    10 -> painterResource(drawable.diamonds_jack)
                    11 -> painterResource(drawable.diamonds_king)
                    12 -> painterResource(drawable.diamonds_queen)
                    else -> painterResource(drawable.card_placeholder_refresh)
                }
            }

            2 -> {
                when (valueIndex) {
                    0 -> painterResource(drawable.hearts_ace)
                    1 -> painterResource(drawable.hearts_2)
                    2 -> painterResource(drawable.hearts_3)
                    3 -> painterResource(drawable.hearts_4)
                    4 -> painterResource(drawable.hearts_5)
                    5 -> painterResource(drawable.hearts_6)
                    6 -> painterResource(drawable.hearts_7)
                    7 -> painterResource(drawable.hearts_8)
                    8 -> painterResource(drawable.hearts_9)
                    9 -> painterResource(drawable.hearts_10)
                    10 -> painterResource(drawable.hearts_jack)
                    11 -> painterResource(drawable.hearts_king)
                    12 -> painterResource(drawable.hearts_queen)
                    else -> painterResource(drawable.card_placeholder_refresh)
                }
            }

            3 -> {
                when (valueIndex) {
                    0 -> painterResource(drawable.spades_ace)
                    1 -> painterResource(drawable.spades_2)
                    2 -> painterResource(drawable.spades_3)
                    3 -> painterResource(drawable.spades_4)
                    4 -> painterResource(drawable.spades_5)
                    5 -> painterResource(drawable.spades_6)
                    6 -> painterResource(drawable.spades_7)
                    7 -> painterResource(drawable.spades_8)
                    8 -> painterResource(drawable.spades_9)
                    9 -> painterResource(drawable.spades_10)
                    10 -> painterResource(drawable.spades_jack)
                    11 -> painterResource(drawable.spades_king)
                    12 -> painterResource(drawable.spades_queen)
                    else -> painterResource(drawable.card_placeholder_refresh)
                }
            }

            else -> painterResource(drawable.card_placeholder_refresh)
        }

    }
}