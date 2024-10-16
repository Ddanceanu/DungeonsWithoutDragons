@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.dungeons

import com.dungeons.model.*

enum class GameStatus {
    Playing,
    Won,
    Lost,
}

class Game(
    val player: Player,
    val map: GameMap,
) {
    var gameStatus = GameStatus.Playing
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

    fun processUserCommand(command: UserCommand) {
        when (command) {
            is UserCommand.Move -> {
                runCatching {
                    check(
                        playerPositionRow + command.getRowIndexDelta() in 0..map.mapMatrix.size &&
                            playerPositionCol + command.getColumnIndexDelta() in 0..map.mapMatrix.first().size,
                    )

                    playerPositionRow += command.getRowIndexDelta()
                    playerPositionCol += command.getColumnIndexDelta()

                    onEvent(map.mapMatrix[playerPositionRow][playerPositionCol])
                }.onFailure {
                    println("Invalid move. Please try again!")
                }
            }

            is UserCommand.UsePotion -> {
                val statType = command.potion.correlatedStatType()

                when {
                    player.isPotionRedundant(command.potion) -> {
                        println("Your ${statType.name.lowercase()} is already ${statType.maxValue}%.\n")
                    }

                    player.inventory.getPotionCount(command.potion) > 0 -> {
                        player.usePotion(command.potion)
                        println("Now you ${statType.name.lowercase()} is ${statType.maxValue}%.\n")
                    }

                    else -> {
                        println("You don't have any ${command.potion.name.lowercase()} potions.\n")
                    }
                }
            }

            UserCommand.Exit -> {
                gameStatus = GameStatus.Lost
            }
        }
    }

    private fun onEvent(event: Event) {
        when (event) {
            Event.EmptyRoom -> {
                println("You are in an empty room.\n")
            }

            is Event.EnemyEncountered -> {
                println("This room is guarded by a ${event.enemy.name}. Prepare for battle!")
                Thread.sleep(2000)
                while (player.getStat(PlayerStatType.Health) > 0 && event.enemy.health > 0) {
                    println(
                        "Player player.health: $player.health, armour: ${
                            player.getStat(
                                PlayerStatType.Armour,
                            )
                        }, your weapon: ${player.currentWeapon.name}",
                    )
                    println("${event.enemy.name} player.health: ${event.enemy.health}")
                    if (player.getStat(PlayerStatType.Armour) > 0 && player.getStat(PlayerStatType.Armour) >= event.enemy.damage) {
                        player.takeArmour(event.enemy.damage)
                    } else if (player.getStat(PlayerStatType.Armour) > 0 && player.getStat(PlayerStatType.Armour) <= event.enemy.damage) {
                        val damageReceived = event.enemy.damage - player.getStat(PlayerStatType.Armour)
                        player.setArmour(0)
                        player.takeHealth(damageReceived)
                    } else {
                        player.takeHealth(event.enemy.damage)
                    }
                    event.enemy.health -= player.currentWeapon.damage
                    if (player.getStat(PlayerStatType.Health) < 0) player.setHealth(0)
                    if (player.getStat(PlayerStatType.Armour) < 0) player.setArmour(0)
                    if (event.enemy.health < 0) event.enemy.health = 0
                    Thread.sleep(1000)
                }
                println(
                    "Player player.health: ${
                        player.getStat(
                            PlayerStatType.Health,
                        )
                    }, armour: ${player.getStat(PlayerStatType.Armour)}, your weapon: ${player.currentWeapon.name}",
                )
                println("${event.enemy.name} player.health: ${event.enemy.health}")

                if (player.getStat(PlayerStatType.Health) <= 0) {
                    gameStatus = GameStatus.Lost
                    println("You have been killed by the ${event.enemy.name}. Good luck next time!")
                } else {
                    if (event.enemy is Enemy.Troll) {
                        println("Congratulations! You killed the ${event.enemy.name}. You finished the game and find the treasure.")
                        gameStatus = GameStatus.Won
                    } else {
                        ("Congratulations! You killed the ${event.enemy.name}. Now you can move forward!\n")
                    }
                }
            }

            is Event.PotionFound -> {
                when (event.potion) {
                    PotionType.Health -> {
                        println(
                            "You found a potion that fully restores your health to 100%. It has been added to your inventory." +
                                " To use it, type health before selecting a new door.\n",
                        )
                    }

                    PotionType.Armour -> {
                        println(
                            "You found a potion that adds 25% to your armor. It has been added to your inventory." +
                                " To use it, type armour before selecting a new door.\n",
                        )
                    }
                }

                player.inventory.addPotion(event.potion)
            }

            is Event.WeaponFound -> {
                print("You found a ${event.weapon.name}. ")
                if (player.currentWeapon.damage < event.weapon.damage) {
                    player.setWeapon(event.weapon)
                    print("Now this is your weapon.\n")
                } else if (player.currentWeapon == event.weapon) {
                    println("You already have this weapon.")
                } else {
                    print("Your current weapon (${player.currentWeapon.name}) is better than a pistol. You can't replace it.\n")
                }
            }
        }
    }

    private fun UserCommand.Move.getRowIndexDelta() = 1

    private fun UserCommand.Move.getColumnIndexDelta() =
        when (this) {
            UserCommand.Move.Left -> -1
            UserCommand.Move.Right -> 1
            UserCommand.Move.Front -> 0
        }
}