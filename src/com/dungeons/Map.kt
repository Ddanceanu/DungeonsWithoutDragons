package com.dungeons

import java.io.File
import kotlin.random.Random

class Map(
    private val player: Player,
    private val nonDeterministic: Boolean = false,
) {
    val map: Array<Array<Int>> =
        if (!nonDeterministic) {
            arrayOf(
                arrayOf(0, 0, 0),
                arrayOf(8, 5, 6),
                arrayOf(3, 0, 1),
                arrayOf(0, 2, 6),
                arrayOf(6, 5, 0),
                arrayOf(0, 8, 6),
                arrayOf(0, 2, 5),
                arrayOf(3, 10, 5),
                arrayOf(4, 4, 4),
            )
        } else {
            generateRandomMap()
        }

    private var playerPositionRow = 0
    private var playerPositionCol = 1

    fun displayNextMove() {
        when (playerPositionCol) {
            0 -> {
                println("You have two doors ahead. Do you want to enter the one in front of you, or the one on your right?")
                println("Type: FRONT/RIGHT or health | armour")
            }
            1 -> {
                println(
                    "You have three doors ahead. Do you want to enter the one in front of you, the one on your left or the one on your right?",
                )
                println("Type: FRONT/LEFT/RIGHT or health | armour")
            }
            2 -> {
                println("You have two doors ahead. Do you want to enter the one in front of you, the one on your left?")
                println("Type: FRONT/LEFT or health | armour")
            }
        }
    }

    fun readNextMove() {
        val userInput = (readlnOrNull() ?: "FRONT").uppercase()
        when (userInput) {
            "FRONT" -> {
                playerPositionRow++
                player.playFight(map[playerPositionRow][playerPositionCol])
            }
            "LEFT" -> {
                try {
                    if (playerPositionCol == 0) {
                        throw ArrayIndexOutOfBoundsException("Invalid input")
                    }
                    playerPositionCol--
                    playerPositionRow++
                    player.playFight(map[playerPositionRow][playerPositionCol])
                } catch (e: ArrayIndexOutOfBoundsException) {
                    println("Invalid move. Please try again!")
                }
            }
            "RIGHT" -> {
                try {
                    if (playerPositionCol == 2) {
                        throw ArrayIndexOutOfBoundsException("Invalid input")
                    }
                    playerPositionCol++
                    playerPositionRow++
                    player.playFight(map[playerPositionRow][playerPositionCol])
                } catch (e: ArrayIndexOutOfBoundsException) {
                    println("Invalid move. Please try again!")
                }
            }
            "HEALTH" -> {
                if (player.health == 100 && player.healthPotions > 0) {
                    println("Your health is already 100%.\n")
                } else if (player.healthPotions > 0) {
                    player.health = 100
                    player.healthPotions--
                    println("Now you health is 100%.\n")
                } else {
                    println("You don't have any health potions.\n")
                }
            }
            "ARMOUR" -> {
                if (player.armour == 100 && player.armourPotions > 0) {
                    println("Your armour is already 100%.\n")
                } else if (player.armourPotions > 0) {
                    player.armour += 25
                    player.armourPotions--
                    if (player.armour > 100) {
                        player.armour = 100
                    }
                    println("Now your armour is ${player.armour}%.\n")
                } else {
                    println("You don't have any armour potions.\n")
                }
            }
            "exit" -> {
                player.gameStatus = false
            }
            else -> {
                println("Unknown command.\n")
            }
        }
    }

    private fun generateRandomMap(): Array<Array<Int>> {
        val rows = Random.nextInt(7, 12)
        val firstRow = arrayOf(0, 0, 0)
        val lastRow = arrayOf(4, 4, 4)
        val middleRows =
            Array(rows - 2) {
                arrayOf(generateRandomNumberWithoutTroll(), generateRandomNumberWithoutTroll(), generateRandomNumberWithoutTroll())
            }
        return arrayOf(firstRow, *middleRows, lastRow)
    }

    fun displayMap() {
        map.forEach { rows ->
            println(rows.joinToString(","))
        }
    }

    private fun generateRandomNumberWithoutTroll(): Int {
        val num = Random.nextInt(0, 2)
        return if (num == 0) {
            Random.nextInt(0, 4)
        } else {
            Random.nextInt(5, 11)
        }
    }

    fun createHtmlTableFromMatrix(matrix: Array<Array<Int>>) {
        val htmlContent = StringBuilder()

        htmlContent.append(
            """
            <html>
            <head>
                <title>Game Map</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        margin: 0;
                        background-color: #f5f5f5;
                    }
                    table {
                        border-collapse: collapse;
                        width: 50%;
                        max-width: 600px;
                        text-align: center;
                        background-color: white;
                        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                    }
                    th, td {
                        border: 1px solid #dddddd;
                        padding: 10px;
                        font-size: 18px;
                        max-width: 20ch;
                        word-wrap: break-word;
                    }
                    tr:nth-child(even) {
                        background-color: #f2f2f2;
                    }
                    tr:hover {
                        background-color: #e0e0e0;
                    }
                    caption {
                        font-size: 24px;
                        font-weight: bold;
                        margin-bottom: 10px;
                    }
                </style>
            </head>
            <body>
            """.trimIndent(),
        )

        htmlContent.append("<table>\n<caption>Game Map</caption>\n\n")

        for (row in matrix) {
            htmlContent.append("<tr>")
            for (cell in row) {
                var cellName =
                    when (cell) {
                        0 -> "Empty Room"
                        1 -> "Skeleton"
                        2 -> "Goblin"
                        3 -> "Orc"
                        4 -> "Troll"
                        5 -> "Health"
                        6 -> "Armour"
                        7 -> "Empty Room"
                        8 -> "Pistol"
                        9 -> "Rifle"
                        10 -> "Machine Gun"
                        else -> "Unknown Room"
                    }
                htmlContent.append("<td>$cellName</td>")
            }
            htmlContent.append("</tr>\n")
        }

        htmlContent.append("</table>\n</body>\n</html>")

        File("Map.html").writeText(htmlContent.toString())
    }
}
