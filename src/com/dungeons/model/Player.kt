package com.dungeons.model

class Player(
    val name: String,
) {
    private val stats: MutableMap<PlayerStatType, Int> =
        PlayerStatType.entries
            .associateWith { statType ->
                statType.initialValue
            }.toMutableMap()

    var currentWeapon = Weapon.FIST
        private set

    val inventory: Inventory = Inventory()

    fun getStat(statType: PlayerStatType): Int = stats[statType]!!

    fun takeHealth(amount: Int) {
        val currentHealth = stats[PlayerStatType.Health] ?: 0
        val newHealth = (currentHealth - amount).coerceAtLeast(0)
        stats[PlayerStatType.Health] = newHealth
    }

    fun takeArmour(amount: Int) {
        val currentArmour = stats[PlayerStatType.Armour] ?: 0
        val newArmour = (currentArmour - amount).coerceAtLeast(0)
        stats[PlayerStatType.Armour] = newArmour
    }

    fun setHealth(health: Int) {
        stats[PlayerStatType.Health] = health
    }

    fun setArmour(armour: Int) {
        stats[PlayerStatType.Armour] = armour
    }

    fun setWeapon(weapon: Weapon) {
        currentWeapon = weapon
    }

    fun usePotion(potion: PotionType) {
        stats[potion.correlatedStatType()] = stats[potion.correlatedStatType()]!! + potion.statIncrement
        inventory.removePotion(potion)
    }

    fun isPotionRedundant(potionType: PotionType): Boolean =
        stats[potionType.correlatedStatType()] == potionType.correlatedStatType().maxValue &&
            inventory.getPotionCount(potionType) > 0
}