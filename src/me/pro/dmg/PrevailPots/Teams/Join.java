package me.pro.dmg.PrevailPots.Teams;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Core.MySQL;
import me.pro.dmg.PrevailPots.Core.TeamSubCommand;

public class Join extends TeamSubCommand {

	Cache cache = Cache.getInstance();
	MySQL sql = MySQL.getInstance();

	@Override
	public void onCommand(Player p, String[] args) {

		if (!cache.getDueler(p).hasFinishedPlacements()) {
			p.sendMessage(Main.prefix + ChatColor.RED + "You must finish your placements first!");
			return;
		}

		if (!cache.teamInvites.containsKey(p.getName())) {
			p.sendMessage(Main.prefix + ChatColor.GOLD + "You have not been invited to any teams!");
			return;
		}

		Team t = cache.teamInvites.get(p.getName());

		cache.teamInvites.remove(p.getName());
		t.addMember(p.getUniqueId().toString());
		t.sendMessage(Main.prefix + ChatColor.GREEN + p.getName() + ChatColor.GOLD + " has joined your team!");
		try {
			sql.setData(cache.getDueler(p), t.getName());
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public String name() {

		return "join";
	}

	@Override
	public String info() {

		return "Accepts a pending team invite";
	}

}
