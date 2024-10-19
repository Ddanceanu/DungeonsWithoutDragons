package com.dungeons.model

enum class PotionType(val statIncrement: Int) {
    Health(statIncrement = 100),
    Armour(statIncrement = 25);

    fun correlatedStatType(): PlayerStatType = when (this) {
        Health -> PlayerStatType.Health
        Armour -> PlayerStatType.Armour
    }
}