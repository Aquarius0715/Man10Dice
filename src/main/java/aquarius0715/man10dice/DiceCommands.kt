package aquarius0715.man10dice

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.NumberFormatException

class DiceCommands(val plugin: Man10Dice): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}あなたはこのコマンドを実行する権限がありません。")
            return false
        }
        when (label) {
            "mdice" -> {
                when (args.size) {
                    0 -> {
                        if (isMan10DiceAdmin(sender)) return false
                        plugin.dice.normalDice(sender, 6)
                    }
                    1 -> {
                        when (args[0]) {
                            "reload" -> {
                                if (isMan10DiceAdmin(sender)) return false
                                plugin.reload()
                                sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}プラグインをリロードしました。")
                                return true
                            }
                            "help" -> {
                                if (isMan10DiceAdmin(sender)) return false
                                sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}/mdice : 6面ダイスを振ります。")
                                sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}/mdice help : この説明画面を開きます。")
                                sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}/mdice local [面] [半径] : 範囲、面を指定して、範囲内に通知するダイスを振ります。")
                                sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}/mdice global [面] : 面を指定して全体に通知するダイスを振ります。")
                                sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}/mdice normal [面] : 通常のダイスを面を指定して振ります。")
                                sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}/mdice admin [面] : 面を指定してdを開始します。")
                                sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}/mdice 100d : 100dを開催します。")
                                sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}/mdice 50d : 50dを開催します。")
                                sender.sendMessage("${plugin.prefix}${ChatColor.AQUA}${ChatColor.BOLD}Created By Aquarius0715")
                                return true
                            }
                            "100d" -> {
                                if (isMan10DiceAdmin(sender)) return false
                                plugin.dice.adminDice(sender, 100, 60)
                                return true
                            }
                            "50d" -> {
                                if (isMan10DiceAdmin(sender)) return false
                                plugin.dice.adminDice(sender, 50, 60)
                                return true
                            }
                        }
                    }
                    2 -> {
                        when (args[0]) {
                            "admin" -> {
                                if (isMan10DiceAdmin(sender)) return false
                                if (!isNumber(args = args)) {
                                    sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}正の数を入力してください。")
                                    return false
                                }
                                plugin.dice.adminDice(sender, args[1].toInt(), 60)
                                return true
                            }
                            "normal" -> {
                                if (isMan10DiceAdmin(sender)) return false
                                if (!isNumber(args = args)) {
                                    sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}正の数を入力してください。")
                                    return false
                                }
                                plugin.dice.normalDice(sender, args[1].toInt())
                                return true
                            }
                            "global" -> {
                                if (isMan10DiceAdmin(sender)) return false
                                if (!isNumber(args = args)) {
                                    sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}正の数を入力してください。")
                                    return false
                                }
                                plugin.dice.globalDice(sender, args[1].toInt())
                                return true
                            }
                        }
                    }
                    3 -> {
                        when (args[0]) {
                            "local" -> {
                                if (isMan10DiceAdmin(sender)) return false
                                if (!isNumber(args = args)) {
                                    sender.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}正の数を入力してください。")
                                    return false
                                }
                                plugin.dice.localDice(sender, args[1].toInt(), args[2].toDouble())
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    private fun isMan10DiceAdmin(player: Player): Boolean {
        if (!player.hasPermission("mdice.admin")) {
            player.sendMessage("${plugin.prefix}${ChatColor.DARK_AQUA}${ChatColor.BOLD}あなたはこのコマンドを実行する権限がありません")
           return false
        }
       return true
    }

    private fun isNumber(args: Array<out String>): Boolean {
        try {
            for ((count, number) in args.withIndex()) {
                if (count == 0) continue
                number.toInt()
            }
        } catch (e: NumberFormatException) {
            return false
        }
        for ((count, number) in args.withIndex()) {
            if (count == 0) continue
            if (number.toInt() < 1) return false
            if (number.toInt() > 2147483647) return false
        }
        return true
    }
}