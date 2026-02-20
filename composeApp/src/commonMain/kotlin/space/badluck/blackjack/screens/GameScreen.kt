package space.badluck.blackjack.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import blackjack.composeapp.generated.resources.Res
import blackjack.composeapp.generated.resources.card_back
import org.jetbrains.compose.resources.painterResource
import space.badluck.blackjack.ColorThemes
import space.badluck.blackjack.GlobalStorage
import space.badluck.blackjack.Managers.CardManager
import space.badluck.blackjack.Managers.SCREENS


enum class gameStatus{
    GAME_TAKE_BETS,
    GAME_RUNNING,
    GAME_WON,
    GAME_LOST,
    GAME_TIE,
}

@Composable
fun GameScreen(globalStorage: GlobalStorage) {

    val croupierHand: MutableList<CardManager.CARD> = remember {
        mutableStateListOf(
            globalStorage.cardManager.value.getRandomCard(),
            globalStorage.cardManager.value.getRandomCard()
        )
    }
    val playerHand: MutableList<CardManager.CARD> = remember {
        mutableStateListOf(
            globalStorage.cardManager.value.getRandomCard(),
            globalStorage.cardManager.value.getRandomCard())
    }


    val croupierHandValue = remember { mutableStateOf(getHandValue(croupierHand,  globalStorage.playerStand)) }
    val playerHandValue = remember { mutableStateOf(getHandValue(playerHand, true)) }
    val betText = remember { mutableStateOf(globalStorage.currentBet.toString()) }
    val lockDouble = remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
            Column(Modifier.padding(top = 60.dp, bottom = 30.dp)) {

                // Scores Rows
                Row(modifier = Modifier.fillMaxHeight(0.10f)) {
                    Button(
                        modifier = Modifier.weight(0.2f),
                        onClick = { globalStorage.cardManager.value.setDifficultyLevel((globalStorage.cardManager.value.getDifficultyLevel() + 1) % 3 + 1) }
                    ) { Text("Difficulty: " + globalStorage.cardManager.value.getDifficultyLevel().toString()) }
                    Column(modifier = Modifier.weight(0.35f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(modifier = Modifier.padding(1.dp), text = "Score: " + globalStorage.currentScore)
                        if (globalStorage.currentScore > globalStorage.topScore) {
                            globalStorage.topScore = globalStorage.currentScore
                        }
                        Text(modifier = Modifier.padding(1.dp), text = "Record: " + globalStorage.topScore)
                    }
                    Button(
                        modifier = Modifier.weight(0.2f),
                        onClick = { globalStorage.screenManager.value.goTo(SCREENS.GREETING) },
                        enabled = !false
                    ) { Text("Go to menu") }
                }

                Surface(color = globalStorage.tableColor, modifier = Modifier.fillMaxHeight(0.8f)) {
                    // Croupier cards
                    Column(Modifier.align(Alignment.CenterHorizontally)) {
                        Text(
                            ("Croupier Cards: " + croupierHandValue.value),
                            Modifier.padding(1.dp),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Row(modifier = Modifier.weight(0.4f).align(Alignment.CenterHorizontally)) {
                            PrintHandRow(croupierHand, false, globalStorage)
                        }

                        // Bet row
                        Row(modifier = Modifier.weight(0.3f).align(Alignment.CenterHorizontally)) {

                            Spacer(Modifier.weight(0.2f))
                            Button(
                                modifier = Modifier.padding(5.dp).align(Alignment.CenterVertically),
                                onClick = {
                                    if (globalStorage.currentBet == 0L)
                                        globalStorage.currentBet = 1

                                    if (globalStorage.currentBet < 100)
                                        return@Button

                                    globalStorage.currentBet -= 100
                                    betText.value = globalStorage.currentBet.toString()
                                },
                                enabled = globalStorage.runningGameAction.value == gameStatus.GAME_TAKE_BETS && globalStorage.currentBet > 1
                            ) { Text("-") }
                            Column(modifier = Modifier.weight(0.3f).align(Alignment.CenterVertically)) {
                                TextField(
                                    value = betText.value,
                                    onValueChange = { newVal ->
                                        if (newVal.isEmpty())
                                            return@TextField
                                        if (!newVal.all { it.isDigit() })
                                            return@TextField
                                        var newValue = newVal.toLong()
                                        if (newValue > globalStorage.currentScore)
                                            newValue = globalStorage.currentScore

                                        betText.value = newValue.toString()
                                        globalStorage.currentBet = newValue
                                                                            },
                                    modifier = Modifier.onFocusEvent {
                                        if (!it.hasFocus)
                                            if (globalStorage.currentBet <= 0) {
                                                globalStorage.currentBet = 0
                                                betText.value = "0"
                                            }
                                        if (globalStorage.currentBet > globalStorage.currentScore) {
                                            globalStorage.currentBet = globalStorage.currentScore
                                            betText.value = globalStorage.currentBet.toString()
                                        }
                                    },
                                    enabled = globalStorage.runningGameAction.value == gameStatus.GAME_TAKE_BETS,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                )
                                Button(
                                    modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally),
                                    onClick = {
                                        globalStorage.currentScore = globalStorage.currentScore
                                        croupierHand.clear()
                                        playerHand.clear()
                                        globalStorage.playerStand = false
                                        croupierHand.add(globalStorage.cardManager.value.getRandomCard())
                                        croupierHand.add(globalStorage.cardManager.value.getRandomCard())
                                        playerHand.add(globalStorage.cardManager.value.getRandomCard())
                                        playerHand.add(globalStorage.cardManager.value.getRandomCard())
                                        croupierHandValue.value = getHandValue(croupierHand, globalStorage.playerStand)
                                        playerHandValue.value = getHandValue(playerHand, true)

                                        if (getHandValue(croupierHand, true).contains("21")) {
                                            globalStorage.playerStand = true
                                            globalStorage.runningGameAction.value = gameStatus.GAME_TIE
                                            if (!playerHandValue.value.contains("21")) {
                                                globalStorage.currentScore -= globalStorage.currentBet
                                                globalStorage.currentBet = minOf(globalStorage.currentScore, globalStorage.currentBet)
                                                betText.value = globalStorage.currentBet.toString()
                                                globalStorage.runningGameAction.value = gameStatus.GAME_TAKE_BETS
                                            }
                                        }
                                        lockDouble.value = false
                                        globalStorage.runningGameAction.value = gameStatus.GAME_RUNNING

                                    },
                                    enabled = globalStorage.currentBet > 0 && globalStorage.runningGameAction.value == gameStatus.GAME_TAKE_BETS
                                ) { Text("Bet") }
                            }
                            Button(
                                modifier = Modifier.padding(5.dp).align(Alignment.CenterVertically),
                                onClick = {
                                    if (globalStorage.currentBet == 0L)
                                        globalStorage.currentBet = 1

                                    if (globalStorage.currentBet > globalStorage.currentScore - 100)
                                        return@Button

                                    globalStorage.currentBet += 100
                                    betText.value = globalStorage.currentBet.toString()
                                },
                                enabled = globalStorage.runningGameAction.value == gameStatus.GAME_TAKE_BETS && (globalStorage.currentScore - globalStorage.currentBet) > 1
                            ) { Text("+") }
                            Spacer(Modifier.weight(0.2f))
                        }

                        Text(
                            ("Yours Cards: " + playerHandValue.value),
                            Modifier.padding(1.dp),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Row(modifier = Modifier.weight(0.4f).align(Alignment.CenterHorizontally)) {
                            PrintHandRow(playerHand, true, globalStorage)
                        }
                    }
                }

                Column(// Buttons block
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = {
                            playerHand.add(globalStorage.cardManager.value.getRandomCard())
                            playerHandValue.value = getHandValue(playerHand, true)

                            if (playerHandValue.value.substringAfter('/').toInt() > 21) {
                                globalStorage.playerStand = true

                                while (croupierHandValue.value.substringAfter('/').toInt() < 17) {
                                    croupierHand.add(globalStorage.cardManager.value.getRandomCard())
                                    croupierHandValue.value = getHandValue(croupierHand, true)
                                }

                                globalStorage.currentScore -= globalStorage.currentBet

                                globalStorage.currentBet = minOf(globalStorage.currentScore, globalStorage.currentBet)
                                betText.value = globalStorage.currentBet.toString()

                                globalStorage.runningGameAction.value = gameStatus.GAME_LOST
                            }
                            lockDouble.value = true

                        },
                        enabled = globalStorage.runningGameAction.value == gameStatus.GAME_RUNNING
                    ) { Text("Hit") }
                    Row {
                        Button(
                            onClick = {
                                globalStorage.runningGameAction.value = gameStatus.GAME_TAKE_BETS
                                globalStorage.playerStand = true
                                croupierHandValue.value = getHandValue(croupierHand, globalStorage.playerStand)

                                while (croupierHandValue.value.substringAfter('/').toInt() < 17) {
                                    croupierHand.add(globalStorage.cardManager.value.getRandomCard())
                                    croupierHandValue.value = getHandValue(croupierHand, globalStorage.playerStand)
                                }

                                processScores(
                                    croupierHandValue =  croupierHandValue.value.substringAfter('/').toInt(),
                                    playerHandValue = playerHandValue.value.substringAfter('/').toInt(),
                                    playerHand = playerHand, globalStorage
                                )


                                globalStorage.currentBet = minOf(globalStorage.currentScore, globalStorage.currentBet)
                                betText.value = globalStorage.currentBet.toString()

                            },
                            enabled = globalStorage.runningGameAction.value == gameStatus.GAME_RUNNING
                        )
                        { Text("Stand") }
                    }
                    Button(
                        onClick = {
                            globalStorage.runningGameAction.value= gameStatus.GAME_TAKE_BETS
                            globalStorage.currentBet *= 2
                            betText.value = globalStorage.currentBet.toString()

                            globalStorage.playerStand = true
                            playerHand.add(globalStorage.cardManager.value.getRandomCard())
                            playerHandValue.value = getHandValue(playerHand, true)

                            while (croupierHandValue.value.substringAfter('/').toInt() < 17) {
                                croupierHand.add(globalStorage.cardManager.value.getRandomCard())
                                croupierHandValue.value = getHandValue(croupierHand, globalStorage.playerStand)
                            }

                            if (playerHandValue.value.substringAfter('/').toInt() > 21) {

                                globalStorage.currentScore -= globalStorage.currentBet
                                return@Button
                            }

                            processScores(
                                croupierHandValue.value.substringAfter('/').toInt(),
                                playerHandValue.value.substringAfter('/').toInt(),
                                playerHand, globalStorage
                            )
                            globalStorage.runningGameAction.value= gameStatus.GAME_LOST

                            globalStorage.currentBet = minOf(globalStorage.currentScore, globalStorage.currentBet)
                            betText.value = globalStorage.currentBet.toString()



                        },
                        enabled = (globalStorage.currentBet*2 < globalStorage.currentScore) && globalStorage.runningGameAction.value == gameStatus.GAME_RUNNING
                    ) { Text("Double") }
                }
            }
        }
    }



    when(globalStorage.runningGameAction.value) {
        gameStatus.GAME_WON -> {
            QuickPopUp(onDismiss = {globalStorage.runningGameAction.value = gameStatus.GAME_TAKE_BETS}, heightFill = 0.3f, widthFill = 0.9f){
                WinningScreen(globalStorage)
            }
        }
        gameStatus.GAME_LOST -> {
            QuickPopUp(
                onDismiss = {globalStorage.runningGameAction.value = gameStatus.GAME_TAKE_BETS },
            ){
                 Text("You lost")
            }
        }
        gameStatus.GAME_TIE -> {globalStorage.runningGameAction.value = gameStatus.GAME_TAKE_BETS}

        else -> {}
    }

}



fun getHandValue(hand: MutableList<CardManager.CARD>, playerStand: Boolean): String{
    var value = 0
    var isAce = false
    var result: String
    for(card in hand){
        value += card.value
        if(card.name == "Ace"){
            isAce = true
        }
    }
    if(!playerStand)
        value -= hand.last().value
    if(isAce){
        if(value>21){
            value -= 10
            result = value.toString()
        }
        else if(playerStand){
            result = (value-10).toString() + '/' + value.toString()
        }
        else
            result = value.toString()
    }
        else result = value.toString()
    return result
}

fun processScores(croupierHandValue: Int,
                  playerHandValue: Int, playerHand: List<CardManager.CARD>,
                  globalStorage: GlobalStorage){

    if(croupierHandValue !in playerHandValue..21 && playerHandValue <= 21) {
        if(playerHand.size == 2 && playerHandValue==21)
            globalStorage.currentScore += globalStorage.currentBet / 2
        globalStorage.currentScore += globalStorage.currentBet
        globalStorage.runningGameAction.value= gameStatus.GAME_WON
    }
    else if (croupierHandValue > playerHandValue){
        globalStorage.currentScore -= globalStorage.currentBet
        globalStorage.runningGameAction.value= gameStatus.GAME_LOST
    }
    else{
        globalStorage.runningGameAction.value= gameStatus.GAME_TIE
    }
}

@Composable
fun PrintHandRow(hand: MutableList<CardManager.CARD>, isPlayers: Boolean, globalStorage: GlobalStorage) {
    
   // Players cards
        for (i in hand) {
            var image = painterResource( globalStorage.cardManager.value.getCardImage(i.image[0], i.image[1]))

            if(!isPlayers){
                if (i == hand.last() && !globalStorage.playerStand) {
                    image = painterResource(Res.drawable.card_back)
                }
            }

            Image(
                painter = image,
                contentDescription = i.name + " of " + i.color,
                modifier = Modifier.width(90.dp).height(120.dp),
            )
        
    }
}

@Composable
fun Modifier.roundedRectBackground(color: Color): Modifier{
    return this then Modifier.background(color, shape = RoundedCornerShape(20.dp)).shadow(elevation = 5.dp)
}

@Composable
fun QuickPopUp(onDismiss: () -> Unit,heightFill: Float = 0.25f, widthFill: Float = 0.7f, contents: @Composable () -> Unit) {
    Popup(
        onDismissRequest = onDismiss,
        alignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .roundedRectBackground(ColorThemes.darkColorTheme.BACKGROUND_COLOUR)
                .fillMaxHeight(heightFill).fillMaxWidth(widthFill)
                .clickable(onClick = { onDismiss() }),
            contentAlignment = Alignment.Center) {
            contents()
        }
    }
}