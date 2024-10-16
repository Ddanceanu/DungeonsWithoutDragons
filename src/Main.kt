import com.dungeons.Map
import com.dungeons.Player

fun main() {
    println("Welcome to \"Dungeons without dragons\"!")
    print("Please enter your name: ")
    val playerName = readLine() ?: "Anonymous"
    val player = Player(playerName)
    print("Do you want a random map? (y/n)? ")
    val input = readLine() ?: "y"
    val gameMap =
        if (input == "y") {
            Map(player, true)
        } else {
            Map(player, false)
        }
    // gameMap.displayMap()
    gameMap.createHtmlTableFromMatrix(gameMap.map)

    println("Hello, $playerName! You are now exploring the dungeon!\n")
    while (player.gameStatus) {
        gameMap.displayNextMove()
        gameMap.readNextMove()
    }
}
