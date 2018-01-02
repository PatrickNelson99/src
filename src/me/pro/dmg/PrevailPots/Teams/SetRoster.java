package me.pro.dmg.PrevailPots.Teams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Core.TeamSubCommand;

public class SetRoster extends TeamSubCommand {

	Cache cache = Cache.getInstance();
	int teamSize = Main.getPlugin().getConfig().getInt("TeamSize");

	@Override
	public void onCommand(Player p, String[] args) {

		if (!cache.isInTeam(p)) {
			p.sendMessage(Main.prefix + ChatColor.RED + "You must be in a team to use this command!");
			return;
		}

		Team t = cache.getTeam(p);

		// CHANGE TO 5
		if (args.length < teamSize) {
			p.sendMessage(Main.prefix + ChatColor.GOLD + "Use " + ChatColor.RED + "/roster "
					+ "[Player1] [Player2] [Player3] [Player4] [Player5] - Maximum of " + ChatColor.GOLD + teamSize);
			return;
		}
		t.getRoster().clear();
		for (int i = 0; i < teamSize; i++) { // change back to 5

			if (Bukkit.getPlayer(args[i]) == null) {
				p.sendMessage(Main.prefix + ChatColor.GOLD + "Could not find player: " + ChatColor.RED + args[1]);
				return;
			}

			String lowerCase = args[i].toLowerCase();

			if (!t.getMemberNames().contains(lowerCase)) {
				p.sendMessage(Main.prefix + ChatColor.RED + args[i] + ChatColor.GOLD + " is not a team member!");
				return;
			}

			t.addMemberToRoster(args[i]);

		}

		t.sendMessage(Main.prefix + ChatColor.GOLD + "Team roster has been set to: " + ChatColor.GREEN
				+ t.getRoster().toString().replace("[", "").replace("]", ""));

	}

	@Override
	public String name() {

		return "roster";
	}

	@Override
	public String info() {

		return "Add team member to the team roster";
	}

}
