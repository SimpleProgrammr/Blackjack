package space.badluck.blackjack

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform