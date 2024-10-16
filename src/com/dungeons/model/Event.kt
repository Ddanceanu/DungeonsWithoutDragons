package com.dungeons.model

sealed interface Event {
    data object EmptyRoom: Event

    @JvmInline
    value class EnemyEncountered(val enemy: Enemy): Event

    @JvmInline
    value class PotionFound(val potion: PotionType): Event

    @JvmInline
    value class WeaponFound(val weapon: Weapon): Event
}