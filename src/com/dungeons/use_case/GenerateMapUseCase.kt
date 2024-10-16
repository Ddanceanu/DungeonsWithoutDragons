package com.dungeons.use_case

import com.dungeons.model.*
import kotlin.random.Random

object GenerateMapUseCase {

    operator fun invoke(nonDeterministic: Boolean): GameMap {
        return if (!nonDeterministic) {
            getDeterministicMap()
        } else {
            generateRandomMap()
        }
    }

    private fun getDeterministicMap(): GameMap {
        return GameMap(
            mapMatrix = arrayOf(
                Array(3) { Event.EmptyRoom },
                arrayOf(
                    Event.WeaponFound(Weapon.PISTOL),
                    Event.PotionFound(PotionType.Health),
                    Event.PotionFound(PotionType.Armour)
                ),
                arrayOf(Event.EnemyEncountered(Enemy.Orc()), Event.EmptyRoom, Event.EnemyEncountered(Enemy.Skeleton())),
                arrayOf(Event.EmptyRoom, Event.EnemyEncountered(Enemy.Goblin()), Event.PotionFound(PotionType.Armour)),
                arrayOf(Event.PotionFound(PotionType.Armour), Event.PotionFound(PotionType.Health), Event.EmptyRoom),
                arrayOf(Event.EmptyRoom, Event.WeaponFound(Weapon.PISTOL), Event.PotionFound(PotionType.Armour)),
                arrayOf(Event.EmptyRoom, Event.EnemyEncountered(Enemy.Goblin()), Event.PotionFound(PotionType.Health)),
                arrayOf(
                    Event.EnemyEncountered(Enemy.Orc()), Event.WeaponFound(Weapon.MACHINE_GUN), Event.PotionFound(
                        PotionType.Health
                    )
                ),
                Array(3) { Event.EnemyEncountered(Enemy.Troll()) }
            )
        )
    }

    private fun generateRandomMap(): GameMap {
        val rows = Random.nextInt(7, 12)
        val firstRow: Array<Event> = Array(3) { Event.EmptyRoom }
        val lastRow: Array<Event> = Array(3) { Event.EnemyEncountered(Enemy.Troll()) }
        val middleRows =
            Array(rows - 2) {
                Array(3) { generateRandomNumberWithoutTroll() }
            }

        return GameMap(arrayOf(firstRow, *middleRows, lastRow))
    }

    private val possibleEvents: List<Event> = buildList {
        addAll(listOf(Enemy.Skeleton(), Enemy.Goblin(), Enemy.Orc()).map { Event.EnemyEncountered(it) })
        addAll(PotionType.entries.map { Event.PotionFound(it) })
        addAll(Weapon.entries.map(Event::WeaponFound))
    }

    private fun generateRandomNumberWithoutTroll(): Event {
        return possibleEvents.random()
    }
}