package com.dungeons.model

data class Inventory(
    private var numberOfHealthPotions: Int = 0,
    private var numberOfArmourPotions: Int = 0
) {

    fun getPotionCount(potionType: PotionType) = when (potionType) {
        PotionType.Health -> numberOfHealthPotions
        PotionType.Armour -> numberOfArmourPotions
    }

    fun addPotion(potion: PotionType) {
        when (potion) {
            PotionType.Health -> numberOfHealthPotions++
            PotionType.Armour -> numberOfArmourPotions++
        }
    }

    fun removePotion(potion: PotionType) {
        when (potion) {
            PotionType.Health -> numberOfHealthPotions--
            PotionType.Armour -> numberOfArmourPotions--
        }
    }
}