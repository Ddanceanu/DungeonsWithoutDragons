package com.dungeons.model

sealed interface Enemy {
    val name: String
    var health: Int
    val damage: Int

    class Skeleton: Enemy {
        override val name: String = "Skeleton"
        override var health: Int = 30
        override val damage: Int = 5
    }

    class Goblin: Enemy {
        override val name: String = "Goblin"
        override var health: Int = 40
        override val damage: Int = 7
    }

    class Orc: Enemy {
        override val name: String = "Orc"
        override var health: Int = 55
        override val damage: Int = 10
    }

    class Troll: Enemy {
        override val name: String = "Troll"
        override var health: Int = 99
        override val damage: Int = 20
    }
}