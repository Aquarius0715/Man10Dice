package aquarius0715.man10dice

import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Man10Dice : JavaPlugin() {
    var coolDown = false
    val prefix = "${ChatColor.BOLD}[" +
            "${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}M" +
            "${ChatColor.WHITE}${ChatColor.BOLD}a" +
            "${ChatColor.GREEN}${ChatColor.BOLD}n" +
            "${ChatColor.WHITE}${ChatColor.BOLD}10" +
            "${ChatColor.YELLOW}${ChatColor.BOLD}Dice" +
            "${ChatColor.WHITE}${ChatColor.BOLD}]"
    var adminDiceStats = false
    var adminDiceAccept = false
    var adminDiceMax = 0
    val adminDiceMap = mutableMapOf<UUID, Int>()
    val dice = DiceLogic(this)

    override fun onEnable() {
        server.pluginManager.registerEvents(DiceLogic(this), this)
        getCommand("mdice")!!.setExecutor(DiceCommands(this))
    }

    fun reload () {

    }
}