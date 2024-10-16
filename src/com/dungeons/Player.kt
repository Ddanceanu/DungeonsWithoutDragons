package com.dungeons

class Player(
    private val name: String?,
) {
    var health: Int = 100
    var armour: Int = 0
    private var weapon = Weapon(Weapons.FIST)
    var gameStatus = true
    var healthPotions = 0
    var armourPotions = 0

    fun playFight(event: Int) {
        var enemy: Enemy? = null
        when (event) {
            1 -> enemy = Skeleton()
            2 -> enemy = Goblin()
            3 -> enemy = Orc()
            4 -> enemy = Troll()
        }

        when (event) {
            0 -> {
                println("You are in an empty room.\n")
            }
            1, 2, 3, 4 -> {
                println("This room is guarded by a ${enemy?.name}. Prepare for battle!")
                Thread.sleep(2000)
                while (health > 0 && enemy?.health!! > 0) {
                    println("Player health: $health, armour: $armour, your weapon: ${weapon.getWeaponName()}")
                    println("${enemy.name} health: ${enemy.health}")
                    if (armour > 0 && armour >= enemy.damage) {
                        armour -= enemy.damage
                    } else if (armour > 0 && armour <= enemy.damage) {
                        val rest = enemy.damage - armour
                        armour = 0
                        health -= rest
                    } else {
                        health -= enemy.damage
                    }
                    enemy.health -= weapon.weaponDamage()
                    if (health < 0)health = 0
                    if (armour < 0)armour = 0
                    if (enemy.health < 0)enemy.health = 0
                    Thread.sleep(1000)
                }
                println("Player health: $health, armour: $armour, your weapon: ${weapon.getWeaponName()}")
                println("${enemy!!.name} health: ${enemy.health}")
                if (health <= 0) {
                    gameStatus = false
                    println("You have been killed by the ${enemy.name}. Good luck next time!")
                } else {
                    if (enemy is Troll) {
                        println("Congratulations! You killed the ${enemy.name}. You finished the game and find the treasure.")
                        gameStatus = false
                    } else {
                        ("Congratulations! You killed the ${enemy.name}. Now you can move forward!\n")
                    }
                }
            }

            5 -> {
                println(
                    "You found a potion that fully restores your health to 100%. It has been added to your inventory." +
                        " To use it, type health before selecting a new door.\n",
                )
                healthPotions++
            }
            6 -> {
                println(
                    "You found a potion that adds 25% to your armor. It has been added to your inventory." +
                        " To use it, type health before selecting a new door.\n",
                )
                armourPotions++
            }
            7 -> {
                println("You are in an empty room.\n")
            }
            8 -> {
                print("You found a pistol. ")
                if (weapon.weaponDamage() < weapon.getPistolDamage()) {
                    weapon.getPistol()
                    print("Now this is your weapon.\n")
                } else if (weapon.weaponDamage() == weapon.getPistolDamage()) {
                    println("You already have this weapon.")
                } else {
                    print("Your current weapon (${weapon.getWeaponName()}) is better than a pistol. You can't replace it.\n")
                }
            }
            9 -> {
                print("You found a rifle. ")
                if (weapon.weaponDamage() < weapon.getRifleDamage()) {
                    weapon.getRifle()
                    print("Now this is your weapon.\n")
                } else if (weapon.weaponDamage() == weapon.getRifleDamage()) {
                    println("You already have this weapon.")
                } else {
                    print("Your current weapon (${weapon.getWeaponName()}) is better than a rifle. You can't replace it.\n")
                }
            }
            10 -> {
                print("You found a MachineGun. ")
                if (weapon.weaponDamage() == weapon.getMachineGunDamage()) {
                    println("You already have this weapon.")
                } else {
                    weapon.getMachineGun()
                    println("Now this is your weapon.")
                }
            }
        }
    }
}
