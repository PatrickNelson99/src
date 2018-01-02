package me.pro.dmg.PrevailPots.Matchmaking;

import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Core.PlayerSubCommand;

public class MatchRecapCommand extends PlayerSubCommand {

	GameTypeManager gtm = GameTypeManager.getInstance();

	@Override
	public void onCommand(Player p, String[] args) {

		gtm.getRecap(p);

	}

	@Override
	public String name() {

		return "recap";
	}

	@Override
	public String info() {

		return "Views last match recap";
	}

}
