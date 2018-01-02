package me.pro.dmg.PrevailPots.Matchmaking;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Kits.KitDataFile;
import me.pro.dmg.PrevailPots.Player.Dueler;

public class QueueManager {

	private QueueManager() {
	}

	static QueueManager instance = new QueueManager();

	public static QueueManager getInstance() {
		return instance;
	}

	KitDataFile kd = KitDataFile.getInstance();
	GameTypeManager gtm = GameTypeManager.getInstance();
	Cache cache = Cache.getInstance();

	public ArrayList<Queue> queue = new ArrayList<Queue>();

	public void setup() {

		for (GameType g : gtm.games.keySet())

			queue.add(new Queue(g, new HashSet<Dueler>()));

	}

	public boolean isInQueue(Dueler d) {

		for (int i = 0; i < queue.size(); i++) {

			if (queue.get(i).getDuelers().contains(d)) {

				return true;

			}

		}
		return false;

	}

	// Get queue from GameType
	public Queue getQueue(GameType gt) {

		for (int i = 0; i < queue.size(); i++) {

			if (queue.get(i).getType() == gt) {

				return queue.get(i);

			}

		}
		return null;

	}

	// Get GameType by name
	public GameType getGameTypeFromQueueByName(String name) {

		for (int i = 0; i < queue.size(); i++) {

			if (queue.get(i).getType().getName().equalsIgnoreCase(name)) {

				return queue.get(i).getType();

			}

		}
		return null;

	}

	public void confirmDuel(Player p, GameType gt) {

		Dueler d = cache.getDueler(p);
		Queue q = getQueue(gt);
		q.addDueler(p);

		HashSet<Dueler> match = new HashSet<Dueler>();
		match.add(d);

		// check for duelers of same skill
		if (q.getDuelers().size() >= 2) {

			for (Dueler newDueler : q.getDuelers()) {

				if (!newDueler.getName().equals(d.getName())) {

					removeFromQueue(d);
					removeFromQueue(newDueler);
					gtm.addNewGame(gt, d, newDueler);

					// start a game with 'Match'
					// dueler is of same skill

					return;

				}
			}

		}

	}

	public void removeFromQueue(Dueler d) {

		for (int i = 0; i < queue.size(); i++) {

			if (queue.get(i).getDuelers().contains(d)) {
				queue.get(i).getDuelers().remove(d);
				d.getPlayer().getInventory().clear();

			}
		}

	}

}
