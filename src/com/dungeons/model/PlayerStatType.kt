package com.dungeons.model

enum class PlayerStatType {
    Health,
    Armour;

    val initialValue: Int
        get() = when (this) {
            Health -> maxValue
            Armour -> 0
        }

    val maxValue: Int
        get() = when (this) {
            Health -> 100
            Armour -> 100
        }
}