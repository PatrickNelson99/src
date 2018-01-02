package me.pro.dmg.PrevailPots.Arenas;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ArenaManager {

	ArenaDataFile ad = ArenaDataFile.getInstance();

	private static ArenaManager instance = new ArenaManager();

	public static ArenaManager getInstance() {

		return instance;
	}

	public enum Team {
		RED, BLUE;
	}

	public Map<Arena, Boolean> arenas = new LinkedHashMap<Arena, Boolean>();

	public void setup() {
		if (ad.getData().getConfigurationSection("Arenas") == null) {

			return;
		}
		for (String s : (ad.getData().getConfigurationSection("Arenas").getKeys(false))) {
			int i = Integer.parseInt(s);
			arenas.put(new Arena(i), false);
		}

	}

	public Arena getArena(int id) {
		for (Arena a : arenas.keySet()) {
			if (a.getID() == id)
				return a;
		}
		return null;
	}

	public boolean inUse(Arena a) {

		for (Arena arena : arenas.keySet()) {

			if (arenas.get(arena) == false) {

				return true;

			}

		}
		return false;

	}

	public Arena getEmptyArena() {

		List<Arena> empty = new ArrayList<Arena>();

		for (Arena arena : arenas.keySet()) {

			if (arenas.get(arena) == false) {

				empty.add(arena);

			}

		}

		if (!(empty.isEmpty())) {

			Random r = new Random();

			return empty.get(r.nextInt(empty.size()));
		}

		return null;

	}

}
