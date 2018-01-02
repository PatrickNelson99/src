package me.pro.dmg.PrevailPots.Matchmaking;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Kits.KitDataFile;
import me.pro.dmg.PrevailPots.Listeners.Join;
import me.pro.dmg.PrevailPots.Player.Dueler;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class PrivateDuel implements Listener {

	KitDataFile kd = KitDataFile.getInstance();

	GameTypeManager gtm = GameTypeManager.getInstance();
	Cache cache = Cache.getInstance();
	QueueManager queue = QueueManager.getInstance();
	Join join = Join.getInstance();

	private PrivateDuel() {
	}

	static PrivateDuel instance = new PrivateDuel();

	public static PrivateDuel getInstance() {
		return instance;
	}

	@EventHandler
	public void onclick(InventoryClickEvent e) {

		if (e.getInventory().getName().contains("Duel - ")) {

			if (e.getCurrentItem() != null) {
				if (e.getCurrentItem().hasItemMeta()) {
					if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
						e.setCancelled(true);

						String currentName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

						final String playerName = e.getInventory().getName().replace("Duel - ", "");
						final Player target = Bukkit.getPlayer(playerName);
						final Player p = (Player) e.getWhoClicked();

						final Dueler inviter = cache.getDueler(p);
						Dueler invited = cache.getDueler(target);

						if (gtm.inInPrivate(invited)) {

							p.sendMessage(
									Main.prefix + ChatColor.RED + "Player has already been invited to a private duel!");
							p.closeInventory();
							return;
						}

						if (gtm.isInOneGame(invited)) {
							p.sendMessage(Main.prefix + ChatColor.RED + "Player is in a duel!");
							p.closeInventory();
							return;
						}

						if (gtm.isEditingKit(invited)) {
							p.sendMessage(Main.prefix + ChatColor.RED + "Player is editing a kit!");
							p.closeInventory();
							return;
						}

						if (gtm.isInTeamGame(target)) {
							p.sendMessage(Main.prefix + ChatColor.RED + "Player is currently in a team game!");
							p.closeInventory();

							return;
						}

						gtm.inviteToDuel(inviter, invited, queue.getGameTypeFromQueueByName(currentName));

						ItemStack is = new ItemStack(Material.REDSTONE, 1);
						ItemMeta im = is.getItemMeta();
						im.setDisplayName(ChatColor.RED + "Leave");
						is.setItemMeta(im);
						p.getInventory().clear();
						p.getInventory().setItem(0, is);

						target.sendMessage(Main.prefix + ChatColor.RED + p.getName() + ChatColor.GOLD
								+ " has requested a duel.\n You have " + ChatColor.RED + "10 seconds " + ChatColor.GOLD
								+ "to accept!");

						TextComponent message = new TextComponent("\n" + ChatColor.GOLD + "-== " + ChatColor.GREEN
								+ "Click Here To Accept" + ChatColor.GOLD + " ==-");
						message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d accept"));
						message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder(ChatColor.GOLD + "Click to accept the duel").create()));
						target.spigot().sendMessage(message);

						p.sendMessage(Main.prefix + ChatColor.GOLD + "You have invited " + ChatColor.GREEN + playerName
								+ ChatColor.GOLD + " to a duel!");
						p.closeInventory();

						new BukkitRunnable() {

							public void run() {

								if (Bukkit.getPlayer(playerName) == null) {
									this.cancel();
									return;
								}

								if (gtm.inInPrivate(inviter)) {
									gtm.privateDuels.remove(gtm.getAscociatedSet(inviter));
									target.sendMessage(Main.prefix + ChatColor.GOLD + "Duel request from "
											+ ChatColor.GREEN + p.getName() + ChatColor.GOLD + " has expired");

									p.sendMessage(Main.prefix + ChatColor.GOLD + "Duel request to " + ChatColor.GREEN
											+ target.getName() + ChatColor.GOLD + " has expired!");
									join.giveSpawnItems(p, false);

								}

							}

						}.runTaskLater(Main.getPlugin(), 200);

					}

				}
			}
		}

	}

	public void openInventory(Player p, String invitedName) {

		Inventory inv = Bukkit.createInventory(null, 9, "Duel - " + invitedName);
		int size = kd.getData().getConfigurationSection("Kits").getKeys(false).size();

		if (size <= 9) {
			inv = Bukkit.createInventory(null, 9, "Duel - " + invitedName);

		} else if (size <= 18) {
			inv = Bukkit.createInventory(null, 18, "Duel - " + invitedName);

		} else if (size <= 27) {
			inv = Bukkit.createInventory(null, 27, "Duel - " + invitedName);

		} else if (size <= 36) {
			inv = Bukkit.createInventory(null, 36, "Duel - " + invitedName);

		} else if (size <= 45) {
			inv = Bukkit.createInventory(null, 45, "Duel - " + invitedName);

		}

		for (String key : kd.getData().getConfigurationSection("Kits").getKeys(false)) {

			String name = kd.getData().getString("Kits." + key + ".Name");

			if (!name.equalsIgnoreCase("Ranked") && !name.equalsIgnoreCase("TeamRanked")) {

				Material item = Material.matchMaterial(kd.getData().getString("Kits." + key + ".Material"));

				ItemStack is = new ItemStack(item, 1);

				ItemMeta im = is.getItemMeta();

				im.setDisplayName(ChatColor.GREEN + name);

				is.setItemMeta(im);

				inv.addItem(is);

			}

			p.openInventory(inv);
		}

	}

}
