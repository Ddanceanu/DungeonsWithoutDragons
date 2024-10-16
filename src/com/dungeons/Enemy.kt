package com.dungeons

abstract class Enemy {
    abstract val name: String
    abstract var health: Int
    abstract var damage: Int
}

class Skeleton : Enemy() {
    override val name: String = "Skeleton"
    override var health: Int = 30
    override var damage: Int = 5
}

class Goblin : Enemy() {
    override val name: String = "Goblin"
    override var health: Int = 40
    override var damage: Int = 7
}

class Orc : Enemy() {
    override val name: String = "Orc"
    override var health: Int = 55
    override var damage: Int = 10
}

class Troll : Enemy() {
    override val name: String = "Troll"
    override var health: Int = 99
    override var damage: Int = 20
}
