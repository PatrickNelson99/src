package me.pro.dmg.PrevailPots.Teams;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Core.MySQL;
import me.pro.dmg.PrevailPots.Core.TeamSubCommand;

public class Kick extends TeamSubCommand {

	Cache cache = Cache.getInstance();
	MySQL sql = MySQL.getInstance();
	

	@Override
	public void onCommand(Player p, String[] args) {

		if (args.length == 0) {
			p.sendMessage(Main.prefix + ChatColor.RED + "Please specify a player!");
		}

		if (cache.isInTeam(p)) {

			Team t = cache.getTeam(p);

			if (t.getLeader().equals(p.getUniqueId().toString())) {

				if (args.length == 0) {
					p.sendMessage(Main.prefix + ChatColor.RED + "Please specify a player!");
					return;
				}

				for (String id : t.getMembers()) {

					if (Bukkit.getOfflinePlayer(UUID.fromString(id)).getName().equalsIgnoreCase(args[0])) {
						t.sendMessage(
								Main.prefix + ChatColor.RED + Bukkit.getOfflinePlayer(UUID.fromString(id)).getName()
										+ ChatColor.GOLD + " has been kicked out of the team!");
						try {
							sql.setTeam(id, "0");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						t.kickMember(id);
						

						return;

					}

				}

				p.sendMessage(Main.prefix + ChatColor.RED + args[0] + ChatColor.GOLD + " is not in your team!");

			}

			else {
				p.sendMessage(Main.prefix + ChatColor.RED + "You must be the team leader to use this command!");
			}

		}

	}

	@Override
	public String name() {

		return "kick";
	}

	@Override
	public String info() {

		return "Kicks a player from your team";
	}

}
