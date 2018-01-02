package me.pro.dmg.PrevailPots.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.pro.dmg.PrevailPots.Matchmaking.Queue;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import net.md_5.bungee.api.ChatColor;

public class GameTypesCommand implements CommandExecutor {

	QueueManager qm = QueueManager.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {

		if (cmd.getName().equalsIgnoreCase("gametypes")) {

			sender.sendMessage(ChatColor.GOLD + "GameTypes: ");

			for (Queue q : qm.queue) {
				sender.sendMessage(org.bukkit.ChatColor.GREEN + q.getType().getName());
			}

		}

		return false;
	}

}
