package space.badluck.blackjack.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.badluck.blackjack.GlobalStorage
import space.badluck.blackjack.Managers.CardManager
import space.badluck.blackjack.Managers.SCREENS
import space.badluck.blackjack.VectorImages.*

@Composable
fun GameScreen(globalStorage: GlobalStorage) {

    val isDoubled = remember { mutableStateOf(false) }
    val isBetLocked = remember { mutableStateOf(false) }


    val croupierHand: ArrayList<CardManager.CARD> = remember {
        arrayListOf(
            globalStorage.cardManager.value.getRandomCard(),
            globalStorage.cardManager.value.getRandomCard()
        )
    }
    val playerHand: ArrayList<CardManager.CARD> = remember {
        arrayListOf(
            globalStorage.cardManager.value.getRandomCard(),
            globalStorage.cardManager.value.getRandomCard()
        )
    }
    val playerStand = remember { mutableStateOf(false) }

    val croupierHandValue = remember { mutableStateOf(getHandValue(croupierHand, playerStand.value)) }
    val playerHandValue = remember { mutableStateOf(getHandValue(playerHand, true)) }
    val betText = remember { mutableStateOf(globalStorage.currentBet.toString()) }

    val canDouble = remember { mutableStateOf(globalStorage.currentScore / globalStorage.currentBet > 2) }

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.padding(top = 60.dp, bottom = 30.dp)) {

            // Scores Rows
            Row(modifier = Modifier.fillMaxHeight(0.15f)) {
                Button(
                    modifier = Modifier.weight(0.2f),
                   onClick = { globalStorage.cardManager.value.setDifficultyLevel( (globalStorage.cardManager.value.getDifficultyLevel() + 1) % 3 + 1 )}
                ){Text("Difficulty: " + globalStorage.cardManager.value.getDifficultyLevel().toString())}
                Column(modifier = Modifier.weight(0.35f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(modifier = Modifier.padding(1.dp), text = "Score: " + globalStorage.currentScore)
                    if (globalStorage.currentScore > globalStorage.topScore) {
                        globalStorage.topScore = globalStorage.currentScore
                    }
                    Text(modifier = Modifier.padding(1.dp), text = "Record: " + globalStorage.topScore)
                }
                Button(
                    modifier = Modifier.weight(0.2f),
                    onClick = { globalStorage.screenManager.value.goTo(SCREENS.GREETING) }) { Text("Go to menu") }
            }

            Surface(color = globalStorage.tableColor, modifier = Modifier.fillMaxHeight(0.7f)) {
                // Croupier cards
                Column(Modifier.align(Alignment.CenterHorizontally)) {
                    Text(
                        ("Croupier Cards: " + croupierHandValue.value),
                        Modifier.padding(1.dp),
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                    Row(modifier = Modifier.weight(0.4f).align(Alignment.CenterHorizontally)) {
                        for (i in croupierHand) {

                            var image = globalStorage.cardManager.value.getCardImage(i.image[0], i.image[1])

                            if (i == croupierHand.last() && !playerStand.value) {
                                image = CardBack
                            }
                            Image(
                                imageVector = image,
                                contentDescription = i.name + " of " + i.color,
                                modifier = Modifier.width(90.dp).height(120.dp),
                            )
                        }
                    }

                    // Bet row
                    Row(modifier = Modifier.weight(0.3f).align(Alignment.CenterHorizontally)) {

                        Spacer(Modifier.weight(0.2f))
                        Button(
                            modifier = Modifier.padding(5.dp).align(Alignment.CenterVertically),
                            onClick = {
                                if(globalStorage.currentBet == 0L)
                                    globalStorage.currentBet = 1

                                if (globalStorage.currentBet < 100)
                                    return@Button

                                globalStorage.currentBet -= 100
                                betText.value = globalStorage.currentBet.toString()
                            },
                            enabled = !isBetLocked.value
                            ) { Text("-") }
                        Column(modifier = Modifier.weight(0.3f).align(Alignment.CenterVertically)) {
                            TextField(
                                value = betText.value,
                                onValueChange = {
                                    if (it.isEmpty())
                                        return@TextField
                                    if (!it.all { it.isDigit() })
                                        return@TextField
                                    var newValue = it.toLong()
                                    if(newValue > globalStorage.currentScore)
                                        newValue = globalStorage.currentScore

                                    betText.value = newValue.toString()
                                    globalStorage.currentBet = newValue
                                },
                                modifier = Modifier.onFocusEvent({
                                    if (!it.hasFocus)
                                        if (globalStorage.currentBet <= 0) {
                                            globalStorage.currentBet = 0
                                            betText.value = "0"
                                        }
                                    if (globalStorage.currentBet > globalStorage.currentScore) {
                                        globalStorage.currentBet = globalStorage.currentScore
                                        betText.value = globalStorage.currentBet.toString()
                                    }
                                }),
                                enabled = !isBetLocked.value,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            )
                            Button(
                                modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally),
                                onClick = {
                                    globalStorage.currentScore = globalStorage.currentScore
                                    croupierHand.clear()
                                    playerHand.clear()
                                    playerStand.value = false
                                    croupierHand.add(globalStorage.cardManager.value.getRandomCard())
                                    croupierHand.add(globalStorage.cardManager.value.getRandomCard())
                                    playerHand.add(globalStorage.cardManager.value.getRandomCard())
                                    playerHand.add(globalStorage.cardManager.value.getRandomCard())
                                    croupierHandValue.value = getHandValue(croupierHand, playerStand.value)
                                    playerHandValue.value = getHandValue(playerHand, true)
                                    isBetLocked.value = true

                                    if(getHandValue(croupierHand, true).contains("21")){
                                        isBetLocked.value = false
                                        playerStand.value = true

                                        if(!playerHandValue.value.contains("21")){
                                            globalStorage.currentScore -= globalStorage.currentBet
                                        }
                                    }

                                },
                                enabled = globalStorage.currentBet > 0 && !isBetLocked.value
                            ) { Text("Bet") }
                        }
                        Button(
                            modifier = Modifier.padding(5.dp).align(Alignment.CenterVertically),
                            onClick = {
                                if(globalStorage.currentBet == 0L)
                                    globalStorage.currentBet = 1

                                if (globalStorage.currentBet > globalStorage.currentScore - 100)
                                    return@Button

                                globalStorage.currentBet += 100
                                betText.value = globalStorage.currentBet.toString()
                            },
                            enabled = !isBetLocked.value
                            ) { Text("+") }
                        Spacer(Modifier.weight(0.2f))
                    }

                    Text(
                        ("Yours Cards: " + playerHandValue.value),
                        Modifier.padding(1.dp),
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                    Row(modifier = Modifier.weight(0.4f).align(Alignment.CenterHorizontally)) {// Players cards


                        for (i in playerHand) {
                            var image = globalStorage.cardManager.value.getCardImage(i.image[0], i.image[1])

                            if (i == croupierHand.last() && !playerStand.value) {
                                image = CardBack
                            }

                            Image(
                                imageVector = image,
                                contentDescription = i.name + " of " + i.color,
                                modifier = Modifier.width(90.dp).height(120.dp),
                            )
                        }
                    }
                }
            }


            Column(// Buttons block
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = {
                        canDouble.value = false
                        playerHand.add(globalStorage.cardManager.value.getRandomCard())
                        playerHandValue.value = getHandValue(playerHand, true)

                        if(playerHandValue.value.substringAfter('/').toInt() > 21){
                            playerStand.value = true
                            isBetLocked.value = false
                            while (croupierHandValue.value.substringAfter('/').toInt() < 17) {
                                croupierHand.add(globalStorage.cardManager.value.getRandomCard())
                                croupierHandValue.value = getHandValue(croupierHand, true)
                            }
                            globalStorage.currentScore -= globalStorage.currentBet
                            globalStorage.currentBet = minOf(globalStorage.currentScore, globalStorage.currentBet)
                            betText.value = globalStorage.currentBet.toString()
                        }

                    },
                    enabled = playerHandValue.value.substringAfter('/').toInt() <= 21 && !isDoubled.value && isBetLocked.value
                ) { Text("Hit") }
                Row {
                    //Button(onClick = { }) { Text("Split") }
                    Button(
                        onClick = {

                            playerStand.value = true
                            croupierHandValue.value = getHandValue(croupierHand, playerStand.value)



                            while (croupierHandValue.value.substringAfter('/').toInt() < 17) {
                                croupierHand.add(globalStorage.cardManager.value.getRandomCard())
                                croupierHandValue.value = getHandValue(croupierHand, playerStand.value)
                            }


                            val maxValue = croupierHandValue.value.substringAfter('/').toInt()
                            if(maxValue < playerHandValue.value.substringAfter('/').toInt() || maxValue > 21) {
                                if(playerHand.size == 2 && playerHandValue.value.contains("21"))
                                    globalStorage.currentScore += globalStorage.currentBet / 2
                                globalStorage.currentScore += globalStorage.currentBet
                            }
                            else if (maxValue > playerHandValue.value.substringAfter('/').toInt())
                                globalStorage.currentScore -= globalStorage.currentBet

                            isBetLocked.value = false
                            isDoubled.value = false
                            canDouble.value = true

                            globalStorage.currentBet = minOf(globalStorage.currentScore, globalStorage.currentBet)
                            betText.value = globalStorage.currentBet.toString()

                        },
                        enabled = playerHandValue.value.substringAfter('/').toInt() <= 21 && !isDoubled.value && isBetLocked.value
                    )
                    { Text("Stand") }
                }
                Button(
                    onClick = {
                        globalStorage.currentBet *= 2
                        betText.value = globalStorage.currentBet.toString()

                        playerStand.value = true
                        isBetLocked.value = false

                        playerHand.add(globalStorage.cardManager.value.getRandomCard())
                        playerHandValue.value = getHandValue(playerHand, true)

                        if(playerHandValue.value.substringAfter('/').toInt() > 21){
                            while (croupierHandValue.value.substringAfter('/').toInt() < 17) {
                                croupierHand.add(globalStorage.cardManager.value.getRandomCard())
                                croupierHandValue.value = getHandValue(croupierHand, playerStand.value)
                            }
                            globalStorage.currentScore -= globalStorage.currentBet
                            return@Button
                        }
                        while (croupierHandValue.value.substringAfter('/').toInt() < 17) {
                            croupierHand.add(globalStorage.cardManager.value.getRandomCard())
                            croupierHandValue.value = getHandValue(croupierHand, playerStand.value)
                        }
                        if(croupierHandValue.value.substringAfter('/').toInt() < playerHandValue.value.toInt() || croupierHandValue.value.substringAfter('/').toInt() > 21) {
                            if(playerHand.size == 2 && playerHandValue.value.substringAfter('/').toInt() ==21)
                                globalStorage.currentScore += globalStorage.currentBet / 2
                            globalStorage.currentScore += globalStorage.currentBet
                        }
                        else if (croupierHandValue.value.substringAfter('/').toInt() > playerHandValue.value.toInt())
                            globalStorage.currentScore -= globalStorage.currentBet

                        globalStorage.currentBet = minOf(globalStorage.currentScore, globalStorage.currentBet)
                        betText.value = globalStorage.currentBet.toString()

                    },
                    enabled = playerHandValue.value.substringAfter('/').toInt() <= 21 && canDouble.value && !isDoubled.value && isBetLocked.value
                ) { Text("Double") }
            }
        }
    }

}




fun getHandValue(hand: ArrayList<CardManager.CARD>, playerStand: Boolean): String{
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

