package me.pro.dmg.PrevailPots.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerRespawnEvent;

public class Death implements Listener {

	Join join = Join.getInstance();

	@EventHandler
	public void onDeath(PlayerRespawnEvent e) {

		join.giveSpawnItems(e.getPlayer(), true);
	}
}
