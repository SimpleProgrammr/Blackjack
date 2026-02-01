package space.badluck.blackjack.Managers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import space.badluck.blackjack.VectorImages.*
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
            2 -> "Hearts"
            3 -> "Spades"
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
    fun getCardImage(colorIndex: Int, valueIndex: Int): ImageVector {
        return when (colorIndex) {
            0 -> {
                when (valueIndex) {
                    0 -> ClubsAce
                    1 -> Clubs2
                    2 -> Clubs3
                    3 -> Clubs4
                    4 -> Clubs5
                    5 -> Clubs6
                    6 -> Clubs7
                    7 -> Clubs8
                    8 -> Clubs9
                    9 -> Clubs10
                    10 -> ClubsJack
                    11 -> ClubsKing
                    12 -> ClubsQueen
                    else -> CardPlaceholderRefresh

                }
            }

            1 -> {
                when (valueIndex) {
                    0 -> DiamondsAce
                    1 -> Diamonds2
                    2 -> Diamonds3
                    3 -> Diamonds4
                    4 -> Diamonds5
                    5 -> Diamonds6
                    6 -> Diamonds7
                    7 -> Diamonds8
                    8 -> Diamonds9
                    9 -> Diamonds10
                    10 -> DiamondsJack
                    11 -> DiamondsKing
                    12 -> DiamondsQueen
                    else -> CardPlaceholderRefresh
                }
            }

            2 -> {
                when (valueIndex) {
                    0 -> HeartsAce
                    1 -> Hearts2
                    2 -> Hearts3
                    3 -> Hearts4
                    4 -> Hearts5
                    5 -> Hearts6
                    6 -> Hearts7
                    7 -> Hearts8
                    8 -> Hearts9
                    9 -> Hearts10
                    10 -> HeartsJack
                    11 -> HeartsKing
                    12 -> HeartsQueen
                    else -> CardPlaceholderRefresh
                }
            }

            3 -> {
                when (valueIndex) {
                    0 -> SpadesAce
                    1 -> Spades2
                    2 -> Spades3
                    3 -> Spades4
                    4 -> Spades5
                    5 -> Spades6
                    6 -> Spades7
                    7 -> Spades8
                    8 -> Spades9
                    9 -> Spades10
                    10 -> SpadesJack
                    11 -> SpadesKing
                    12 -> SpadesQueen
                    else -> CardPlaceholderRefresh
                }
            }

            else -> CardPlaceholderRefresh
        }

    }
}