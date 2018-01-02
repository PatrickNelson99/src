package me.pro.dmg.PrevailPots.Listeners;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.pro.dmg.PrevailPots.Arenas.Arena;
import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.MySQL;
import me.pro.dmg.PrevailPots.Matchmaking.GameType;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Player.Dueler;
import me.pro.dmg.PrevailPots.Teams.Team;

public class OneKill implements Listener {

	GameTypeManager gtm = GameTypeManager.getInstance();
	Cache cache = Cache.getInstance();
	MySQL sql = MySQL.getInstance();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onAnyDamage(EntityDamageEvent e) {

		if (e.getEntity().getType() == EntityType.PLAYER) {

			Player p = (Player) e.getEntity();

			Dueler killed = cache.getDueler(p);

			if ((((Damageable) p).getHealth() - e.getFinalDamage()) <= 0) {

				if (gtm.isInOneGame(killed)) {

					for (HashMap<Arena, HashSet<Dueler>> game : gtm.games.values()) {

						for (HashSet<Dueler> duelers : new HashMap<Arena, HashSet<Dueler>>(game).values()) {

							if (duelers.contains(killed)) {

								for (Dueler d : duelers) {

									if (!(d.getName().equals(killed.getName()))) {

										gtm.removeGame(d, killed);
										e.setCancelled(true);
										killed.getPlayer().setHealth(20);
										p.setHealth(20);
										p.setFoodLevel(20);
										String dTeam = "0";
										String killedTeam = "0";

										if (cache.getTeam(d.getPlayer()) != null) {
											Team t = cache.getTeam(d.getPlayer());
											dTeam = t.getName();
										}

										if (cache.getTeam(killed.getPlayer()) != null) {
											Team t = cache.getTeam(killed.getPlayer());
											killedTeam = t.getName();
										}

										try {
											sql.setData(d, dTeam);
											sql.setData(killed, killedTeam);
										} catch (SQLException ex) {

											ex.printStackTrace();
										}

									}
								}

							}
						}
					}

				}

			}

		}

	}

	@EventHandler
	public void onOneLog(PlayerQuitEvent e) {

		Dueler logger = cache.getDueler(e.getPlayer());

		for (GameType gt : gtm.games.keySet()) {

			for (HashSet<Dueler> duelers : gtm.games.get(gt).values()) {

				for (Dueler d : duelers) {

					if (duelers.contains(logger)) {

						if (!d.getName().equals(logger.getName())) {
							// public HashMap<GameType, HashMap<Arena,
							// HashSet<Dueler>>> games = new HashMap<GameType,
							// HashMap<Arena, HashSet<Dueler>>>();
							gtm.removeGame(d, logger);

						}

					}

				}
			}

		}

		if (gtm.recap.containsKey(e.getPlayer().getName())) {
			gtm.recap.remove(e.getPlayer().getName());
		}

	}

}
