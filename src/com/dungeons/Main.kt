package com.dungeons

import com.dungeons.model.GameMap
import com.dungeons.model.Player
import com.dungeons.model.PotionType
import com.dungeons.model.UserCommand
import com.dungeons.use_case.ExportMapToHtmlUseCase
import com.dungeons.use_case.GenerateMapUseCase
import java.util.Locale

fun main() {
    println("Welcome to \"Dungeons without dragons\"!")

    val player = initializePlayer()
    val map = initializeMap().also {
        ExportMapToHtmlUseCase.invoke(it)
    }

    val game = Game(
        player = player,
        map = map
    )

    println("Hello, ${player.name}! You are now exploring the dungeon!\n")
    while (game.gameStatus == GameStatus.Playing) {
        game.displayNextMove()

        val userCommand = readNextMove()
        game.processUserCommand(userCommand)
    }
}

private fun initializePlayer(): Player {
    print("Please enter your name: ")
    val playerName = readlnOrNull() ?: "Anonymous"

    return Player(playerName)
}

private fun initializeMap(): GameMap {
    print("Do you want a random map? (y/n)? ")
    val input = readlnOrNull() ?: "y"

    return GenerateMapUseCase.invoke(nonDeterministic = input == "y")
}

private fun readNextMove(): UserCommand {
    val userInput = readlnOrNull()?.lowercase()?.replaceFirstChar { it.uppercase(Locale.getDefault()) }

    return when {
        runCatching { UserCommand.Move.valueOf(userInput.orEmpty()) }.getOrNull() != null -> {
            UserCommand.Move.valueOf(userInput.orEmpty())
        }

        userInput == "Health" -> UserCommand.UsePotion(potion = PotionType.Health)
        userInput == "Armour" -> UserCommand.UsePotion(potion = PotionType.Armour)
        userInput == "Exit" -> UserCommand.Exit
        else -> UserCommand.Move.Front
    }
}