package me.pro.dmg.PrevailPots.Teams;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Core.TeamSubCommand;

public class TeamChat extends TeamSubCommand {

	Cache cache = Cache.getInstance();

	@Override
	public void onCommand(Player p, String[] args) {

		if (!cache.isInTeam(p)) {

			p.sendMessage(Main.prefix + ChatColor.RED + "You must be in a team to use this command!");
			return;

		}

		if (args.length == 0) {
			p.sendMessage(Main.prefix + ChatColor.RED + "Please enter a message to send");
		}

		Team t = cache.getTeam(p);
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < args.length; i++) {

			sb.append(args[i] + " ");

		}

		String message = sb.toString();

		t.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + t.getName() + ChatColor.DARK_GRAY + "] "
				+ p.getDisplayName() + ChatColor.DARK_GRAY + " > " + ChatColor.GRAY + message);

	}

	@Override
	public String name() {

		return "chat";
	}

	@Override
	public String info() {

		return "Chat amongst your team members";
	}

}
