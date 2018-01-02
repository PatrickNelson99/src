package me.pro.dmg.PrevailPots.Listeners;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Teams.Team;

public class OnTeamKill implements Listener {

	GameTypeManager gtm = GameTypeManager.getInstance();
	Cache cache = Cache.getInstance();
	Join join = Join.getInstance();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onAnyDame(EntityDamageEvent e) {

		if (e.getEntityType() == EntityType.PLAYER) {

			Player p = (Player) e.getEntity();

			if ((((Damageable) p).getHealth() - e.getFinalDamage()) <= 0) {

				if (gtm.isInTeamGame(p)) {
					e.setCancelled(true);
					p.setHealth(20);
					p.setFoodLevel(20);
					gtm.killTeamPlayer(p.getName());

				}
			}

		}

	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {

		if (e.getEntity() instanceof Player) {

			if (e.getDamager() instanceof Player) {

				Player p = (Player) e.getEntity();
				Player damager = (Player) e.getDamager();

				if (gtm.isInTeamGame(p)) {

					Team t = cache.getTeam(p);

					if (t.getMembers().contains(damager.getUniqueId().toString())) {
						e.setCancelled(true);
					}

				}
			}

		}
	}

	@EventHandler
	public void onTeamLog(PlayerQuitEvent e) {

		Player p = e.getPlayer();

		if (gtm.isInTeamGame(p)) {
			gtm.killTeamPlayer(p.getName());
		}

	}

}
