package aquarius0715.man10dice

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChatEvent
import org.bukkit.scheduler.BukkitRunnable
import java.lang.NumberFormatException

class DiceLogic(val plugin: Man10Dice): Listener {

    fun globalDice(player: Player, num: Int) {
        if (isCoolDown(player)) return
        player.sendMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}${player.name}${ChatColor.DARK_AQUA}${ChatColor.BOLD}がダイスを振っています・・・${ChatColor.MAGIC}${ChatColor.BOLD}aaa")
        object : BukkitRunnable() {
            override fun run() {
                val rnd = (1..num).random()
                player.sendMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}${player.name}" +
                        "${ChatColor.YELLOW}${ChatColor.BOLD}${num}" +
                        "${ChatColor.DARK_AQUA}${ChatColor.BOLD}面ダイスを振って" +
                        "${ChatColor.YELLOW}${ChatColor.BOLD}${rnd}" +
                        "${ChatColor.DARK_AQUA}${ChatColor.BOLD}が出た！！！")
                player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 8.0F, 0.0F)
                coolDown()
            }
        }.runTaskLater(plugin, 20 * 3)
    }

    fun localDice(player: Player, num: Int, distance: Double) {
        if (isCoolDown(player)) return
        player.sendMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}${player.name}" +
                "${ChatColor.DARK_AQUA}${ChatColor.BOLD}がダイスを振っています・・・${ChatColor.MAGIC}${ChatColor.BOLD}aaa")
        for (players in player.getNearbyEntities(distance, distance, distance)) {
            if (players is Player) {
                players.sendMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}${player.name}" +
                        "${ChatColor.DARK_AQUA}${ChatColor.BOLD}がダイスを振っています・・・${ChatColor.MAGIC}${ChatColor.BOLD}aaa")
            }
        }
        object : BukkitRunnable() {
            override fun run() {
                val rnd = (1..num).random()
                player.sendMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}${player.name}" +
                        "${ChatColor.DARK_AQUA}${ChatColor.BOLD}が" +
                        "${ChatColor.YELLOW}${ChatColor.BOLD}${num}" +
                        "${ChatColor.DARK_AQUA}${ChatColor.BOLD}面ダイスを振って" +
                        "${ChatColor.YELLOW}${ChatColor.BOLD}${rnd}" +
                        "${ChatColor.DARK_AQUA}${ChatColor.BOLD}が出た！！！")
                player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 8.0F, 0.0F)
                for (players in player.getNearbyEntities(50.0, 50.0, 50.0)) {
                    if (players is Player) {
                        players.sendMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}${player.name}" +
                                "${ChatColor.DARK_AQUA}${ChatColor.BOLD}が" +
                                "${ChatColor.YELLOW}${ChatColor.BOLD}${num}" +
                                "${ChatColor.DARK_AQUA}${ChatColor.BOLD}面ダイスを振って" +
                                "${ChatColor.YELLOW}${ChatColor.BOLD}${rnd}" +
                                "${ChatColor.DARK_AQUA}${ChatColor.BOLD}が出た！！！")
                        players.playSound(players.location, Sound.ENTITY_PLAYER_LEVELUP, 8.0F, 0.0F)
                    }
                }
                coolDown()
            }
        }.runTaskLater(plugin, 20 * 3)
    }

    fun normalDice(player: Player, num: Int) {
        if (isCoolDown(player)) return
        player.sendMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}${num}" +
                "${ChatColor.DARK_AQUA}${ChatColor.BOLD}面ダイスを振って" +
                "${ChatColor.YELLOW}${ChatColor.BOLD}${(1..num).random()}" +
                "${ChatColor.DARK_AQUA}${ChatColor.BOLD}が出ました。")
        player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 8.0F, 0.0F)
        coolDown()
    }
    
    fun adminDice(player: Player, num: Int, time: Int) {
        if (plugin.adminDiceStats) {
            player.sendMessage("${plugin.prefix}${ChatColor.DARK_BLUE}${ChatColor.BOLD}AdminDiceはすでに始まっています。")
            return
        }
        Bukkit.broadcastMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}${player.name}" +
                "${ChatColor.DARK_AQUA}${ChatColor.BOLD}によって" +
                "${ChatColor.YELLOW}${ChatColor.BOLD}${num}D" +
                "${ChatColor.DARK_AQUA}${ChatColor.BOLD}が開始されました。")
        plugin.adminDiceAccept = true
        plugin.adminDiceStats = true
        plugin.adminDiceMax = num
        var time1 = time
        object : BukkitRunnable() {
            override fun run() {
                if (time1 % 60 == 0 && time1 != 0) {
                    Bukkit.broadcastMessage("${plugin.prefix}${ChatColor.DARK_BLUE}${ChatColor.BOLD}" +
                            "残り${ChatColor.YELLOW}${ChatColor.BOLD}" +
                            "${time1 / 60}${ChatColor.DARK_AQUA}${ChatColor.BOLD}分です。")
                }
                if (time1 == 30) {
                    Bukkit.broadcastMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}残り" +
                            "${ChatColor.YELLOW}${ChatColor.BOLD}${time1}" +
                            "${ChatColor.DARK_AQUA}${ChatColor.BOLD}秒です。")
                }
                if (time1 in 0..10) {
                    Bukkit.broadcastMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}残り" +
                            "${ChatColor.YELLOW}${ChatColor.BOLD}${time1}" +
                            "${ChatColor.DARK_AQUA}${ChatColor.BOLD}秒です。")
                }
                if (time1 == -1) {
                    plugin.adminDiceAccept = false
                    Bukkit.broadcastMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}受付が終了しました。")
                    Bukkit.broadcastMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}" +
                            "ダイスを振っています...${ChatColor.MAGIC}aaa")
                }
                if (time1 == -4) {
                    val rnd = (1..num).random()
                    var winnerCount = 0
                    Bukkit.broadcastMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}" +
                            "$num" +
                            "${ChatColor.DARK_AQUA}${ChatColor.BOLD}面ダイスを振って" +
                            "${ChatColor.YELLOW}${ChatColor.BOLD}${rnd}" +
                            "${ChatColor.DARK_AQUA}${ChatColor.BOLD}が出ました。")
                    for (player1 in Bukkit.getOnlinePlayers()) {
                        player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 8.0F, 0.0F)
                        if (plugin.adminDiceMap[player1.uniqueId] == rnd) {
                            Bukkit.broadcastMessage("${plugin.prefix}${ChatColor.GOLD}${ChatColor.BOLD}" +
                                    player.displayName +
                                    "さん${ChatColor.DARK_AQUA}${ChatColor.BOLD}がピッタリです！！！")
                            player.world.spawnEntity(player.location, EntityType.FIREWORK)
                            winnerCount++
                            continue
                        }
                        if (plugin.adminDiceMap[player1.uniqueId] == rnd + 1 || plugin.adminDiceMap[player1.uniqueId] == rnd - 1) {
                            Bukkit.broadcastMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}${player.displayName}" +
                                    "${ChatColor.DARK_AQUA}${ChatColor.BOLD}さんがニアミスです！！！")
                            player.world.spawnEntity(player.location, EntityType.FIREWORK)
                            winnerCount++
                            continue
                        }
                    }
                    if (winnerCount == 0) {
                        Bukkit.broadcastMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}当選者は誰もいませんでした・・・")
                    }
                    plugin.adminDiceMap.clear()
                    plugin.adminDiceStats = false
                    Bukkit.broadcastMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}${ChatColor.YELLOW}${ChatColor.BOLD}" +
                            "${plugin.adminDiceMax}D${ChatColor.DARK_AQUA}${ChatColor.BOLD}が終了しました。")
                    cancel()
                }
                time1--
            }
        }.runTaskTimer(plugin, 0, 20)
    }

    @EventHandler
    fun onSendChat(event: PlayerChatEvent) {
        if (!plugin.adminDiceStats) return
        try {
            event.message.toInt()
        } catch (e: NumberFormatException) {
            return
        }
        event.isCancelled = true
        if (event.message.toInt() <= 0 || event.message.toInt() > plugin.adminDiceMax) {
            event.player.sendMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}1" +
                    "${ChatColor.DARK_AQUA}${ChatColor.BOLD}以上" +
                    "${ChatColor.YELLOW}${ChatColor.BOLD}${plugin.adminDiceMax}" +
                    "${ChatColor.DARK_AQUA}${ChatColor.BOLD}以下の数字を入力してください。")
            return
        }
        if (plugin.adminDiceMap.containsValue(event.message.toInt())) {
            event.player.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}その数字は言われています。")
            return
        }
        plugin.adminDiceMap[event.player.uniqueId] = event.message.toInt()
        event.player.sendMessage("${plugin.prefix}${ChatColor.YELLOW}${ChatColor.BOLD}${event.message}" +
                "${ChatColor.DARK_AQUA}${ChatColor.BOLD}を指定しました！")
    }

    fun coolDown() {
        plugin.coolDown = true
        object : BukkitRunnable() {
            override fun run() {
                plugin.coolDown = false
            }
        }.runTaskLater(plugin, 20 * 3)
    }

    private fun isCoolDown(player: Player): Boolean {
        return if (plugin.coolDown) {
            player.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}現在ダイスがクールダウン中です。")
            true
        } else {
            false
        }
    }
}