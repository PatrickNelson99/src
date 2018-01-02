package me.pro.dmg.PrevailPots.Teams;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Core.MySQL;
import me.pro.dmg.PrevailPots.Core.TeamSubCommand;

public class Leave extends TeamSubCommand {

	Cache cache = Cache.getInstance();
	MySQL sql = MySQL.getInstance();

	@Override
	public void onCommand(Player p, String[] args) {

		if (!cache.isInTeam(p)) {
			p.sendMessage(Main.prefix + ChatColor.RED + "You must be in a team to use this command");
			return;
		}

		Team t = cache.getTeam(p);

		if (t.getLeaderName().equals(p.getName())) {

			p.sendMessage(Main.prefix + ChatColor.RED + "You are the leader of the team!");
			return;
		}

		t.sendMessage(Main.prefix + ChatColor.RED + p.getName() + ChatColor.GOLD + " has left the team!");
		t.getRoster().remove(p.getName());
		t.kickMember(p.getUniqueId().toString());
		try {
			sql.setTeam(p.getUniqueId().toString(), "0");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String name() {

		return "leave";
	}

	@Override
	public String info() {

		return "Leaves current team";
	}

}
