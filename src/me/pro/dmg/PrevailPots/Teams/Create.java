package me.pro.dmg.PrevailPots.Teams;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Core.MySQL;
import me.pro.dmg.PrevailPots.Core.TeamSubCommand;

public class Create extends TeamSubCommand {

	TeamDataFile td = TeamDataFile.getInstance();
	Cache cache = Cache.getInstance();
	MySQL sql = MySQL.getInstance();

	@Override
	public void onCommand(Player p, String[] args) {

		if (cache.isInTeam(p)) {

			p.sendMessage(Main.prefix + ChatColor.RED + "You are already in a team!");
			return;
		}

		if (args.length < 1) {
			p.sendMessage(Main.prefix + ChatColor.GOLD + "Use /team create <name>");
			return;
		}

		if (td.isOnCreateCooldown(p)) {
			p.sendMessage(
					Main.prefix + ChatColor.RED + "You have recently created a ranked team and are still on cooldown!");
			return;
		}

		if (!cache.getDueler(p).hasFinishedPlacements()) {
			p.sendMessage(Main.prefix + ChatColor.RED
					+ "You must complete your placement matches before creating a ranked team!");
			return;
		}

		if (args[0].length() > 10) {
			p.sendMessage(Main.prefix + ChatColor.RED + "Please use a maximum of 10 characters in your team name!");
			return;
		}

		for (String blockedName : Main.getPlugin().getConfig().getStringList("Blocked-Team-Names")) {

			if (blockedName.equalsIgnoreCase(args[0])) {
				p.sendMessage(Main.prefix + ChatColor.RED + "Please choose a different name");
				return;
			}

		}

		if (td.addFile(p, args[0])) {
			cache.addTeam(p);

			try {
				sql.setTeamData(cache.getTeam(p));

			} catch (SQLException e) {

				e.printStackTrace();
			}

			try {
				sql.setData(cache.getDueler(p), args[0]);
			}

			catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public String name() {

		return "create";
	}

	@Override
	public String info() {

		return "Creates a ranked team";
	}

}
