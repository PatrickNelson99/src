package me.pro.dmg.PrevailPots.Matchmaking;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Core.PlayerSubCommand;
import me.pro.dmg.PrevailPots.Player.Dueler;

public class DuelAcceptCommand extends PlayerSubCommand {

	Cache cache = Cache.getInstance();
	GameTypeManager gtm = GameTypeManager.getInstance();

	@Override
	public void onCommand(Player p, String[] args) {

		Dueler d = cache.getDueler(p);

		if (gtm.isInOneGame(d)) {
			p.sendMessage(Main.prefix + ChatColor.RED + "You are in a duel!");
			return;
		}

		if (gtm.isEditingKit(d)) {
			p.sendMessage(Main.prefix + ChatColor.RED + "You are editing a kit!");
			return;
		}

		if (gtm.isInTeamGame(p)) {
			p.sendMessage(Main.prefix + ChatColor.RED + "You are currently in a team game!");

			return;
		}

		if (gtm.inInPrivate(d)) {

			Dueler d1 = null;
			for (Dueler dueler : gtm.getAscociatedSet(cache.getDueler(p))) {

				if (dueler.getName() != d.getName()) {
					d1 = dueler;
				}

			}

			gtm.addNewGame(gtm.privateDuels.get(gtm.getAscociatedSet(d)), d, d1);
			gtm.privateDuels.remove(gtm.getAscociatedSet(d1));

		}

		else {
			p.sendMessage(Main.prefix + ChatColor.GOLD + "You haven't been invited to any duels!");
		}
	}

	@Override
	public String name() {

		return "accept";
	}

	@Override
	public String info() {

		return "Accepts a duel request";
	}

}
