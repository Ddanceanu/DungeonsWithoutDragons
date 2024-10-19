package com.dungeons.model

sealed interface UserCommand {

    enum class Move: UserCommand {
        Left,
        Right,
        Front
    }

    class UsePotion(val potion: PotionType): UserCommand

    data object Exit: UserCommand
}