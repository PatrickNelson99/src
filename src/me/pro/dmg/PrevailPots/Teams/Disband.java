package me.pro.dmg.PrevailPots.Teams;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Core.MySQL;
import me.pro.dmg.PrevailPots.Core.TeamSubCommand;

public class Disband extends TeamSubCommand {

	Cache cache = Cache.getInstance();
	TeamDataFile td = TeamDataFile.getInstance();
	MySQL sql = MySQL.getInstance();

	@Override
	public void onCommand(Player p, String[] args) {

		if (args.length == 0) {

			if (!cache.isInTeam(p)) {

				p.sendMessage(Main.prefix + ChatColor.RED + "You are not in a team!");
				return;

			}

			Team t = cache.getTeam(p);
			String name = t.getName();

			if (t.getLeaderName().equals(p.getName())) {
				sql.deleteTeamData(name);
				td.disband(p, t.getName());

				if (cache.containsTeam(name)) {
					cache.getTeamByName(name).sendMessage(Main.prefix + ChatColor.GOLD
							+ "Your team has been disbanded by " + ChatColor.RED + p.getName());

					cache.teams.remove(cache.getTeamByName(name));
				}

			}

			else {
				p.sendMessage(Main.prefix + ChatColor.RED + "You must be the team leader to use this command!");
				return;
			}
		}

		else {

			if (p.isOp()) {
				sql.deleteTeamData(args[0]);
				td.disband(p, args[0]);

				if (cache.containsTeam(args[0])) {
					cache.getTeamByName(args[0]).sendMessage(Main.prefix + ChatColor.GOLD
							+ "Your team has been disbanded by " + ChatColor.RED + p.getName());

					cache.teams.remove(cache.getTeamByName(args[0]));
				}

			}

		}

	}

	@Override
	public String name() {

		return "disband";
	}

	@Override
	public String info() {

		return "Disband team";
	}

}
