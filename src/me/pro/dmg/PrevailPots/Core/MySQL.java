package me.pro.dmg.PrevailPots.Core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;

import me.pro.dmg.PrevailPots.Player.Dueler;
import me.pro.dmg.PrevailPots.Teams.Team;

public class MySQL {

	private MySQL() {
	}

	static MySQL instance = new MySQL();

	public static MySQL getInstance() {
		return instance;
	}

	Cache cache = Cache.getInstance();

	private Connection connection;

	private String host;
	private String port;
	private String database;
	private String username;
	private String password;

	public void openConnection() throws SQLException, ClassNotFoundException {

		host = Main.getPlugin().getConfig().getString("MySQL.Host");
		port = Main.getPlugin().getConfig().getString("MySQL.Port");
		database = Main.getPlugin().getConfig().getString("MySQL.Databse");
		username = Main.getPlugin().getConfig().getString("MySQL.Username");
		password = Main.getPlugin().getConfig().getString("MySQL.Password");

		if (connection != null && !connection.isClosed()) {
			return;
		}
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
				this.username, this.password);

		PreparedStatement statementTeams = connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS TeamData(Name varchar(50) , Rank varchar(50), Tier varchar(50), Points int, Elo int, Wins int, Losses int, PRIMARY KEY (Name));");
		statementTeams.executeUpdate();
		statementTeams.close();

		PreparedStatement statement = connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS PlayerData(UUID varchar(50) , Rank varchar(50), Tier varchar(50), Points int, Elo int, Wins int, Losses int, TeamName varchar(50), PRIMARY KEY (UUID));");
		statement.executeUpdate();
		statement.close();
		// ON DELETE CASCADE ON UPDATE CASCADE
	}

	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();

			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void setTeam(String id, String team) throws SQLException {

		final String uuid = "'" + id + "'";
		final String teamName = "'" + team + "'";

		// if database doesnt contain

		new BukkitRunnable() {
			public void run() {
				PreparedStatement statement;
				try {
					statement = connection.prepareStatement(
							"UPDATE PlayerData SET TeamName=" + teamName + " WHERE UUID=" + uuid + ";");

					statement.executeUpdate();
					statement.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}
		}.runTaskAsynchronously(Main.getPlugin());

	}

	public void setData(Dueler d, String team) throws SQLException {

		final String uuid = "'" + d.getUUID() + "', ";
		final String rank = "'" + d.getRank() + "', ";
		final String tier = "'" + d.getNumeral() + "', ";
		final int points = d.getPoints();
		final int elo = +d.getElo();
		final int wins = d.getWins();
		final int losses = d.getLosses();
		final String teamName = "'" + team + "'";

		// if database doesnt contain

		new BukkitRunnable() {
			public void run() {
				PreparedStatement statement;
				try {
					statement = connection.prepareStatement(
							"INSERT INTO PlayerData (UUID, Rank, Tier, Points, Elo, Wins, Losses, TeamName) VALUES ( "
									+ uuid + rank + tier + points + ", " + elo + ", " + wins + ", " + losses + ", "
									+ teamName + ") ON DUPLICATE KEY UPDATE Rank=" + rank + "Tier=" + tier + "Points="
									+ points + ", " + "Elo=" + elo + ", " + "Wins=" + wins + ", " + "Losses=" + losses
									+ ", " + "TeamName=" + teamName + ";");

					statement.executeUpdate();
					statement.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}
		}.runTaskAsynchronously(Main.getPlugin());

	}

	public void setTeamData(Team t) throws SQLException {

		final String name = "'" + t.getName() + "', ";
		final String rank = "'" + t.getRank() + "', ";
		final String tier = "'" + t.getNumeral() + "', ";
		final int points = t.getPoints();
		final int elo = +t.getElo();
		final int wins = t.getWins();
		final int losses = t.getLosses();

		// if database doesnt contain

		new BukkitRunnable() {
			public void run() {
				PreparedStatement statement;
				try {
					statement = connection.prepareStatement(
							"INSERT INTO TeamData (Name, Rank, Tier, Points, Elo, Wins, Losses) VALUES ( " + name + rank
									+ tier + points + ", " + elo + ", " + wins + ", " + losses
									+ ") ON DUPLICATE KEY UPDATE Rank=" + rank + "Tier=" + tier + "Points=" + points
									+ ", " + "Elo=" + elo + ", " + "Wins=" + wins + ", " + "Losses=" + losses + ";");

					statement.executeUpdate();
					statement.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}
		}.runTaskAsynchronously(Main.getPlugin());

	}

	public void deleteTeamData(final String name) {

		new BukkitRunnable() {
			public void run() {
				PreparedStatement statement;
				try {
					statement = connection.prepareStatement("DELETE FROM TeamData where Name='" + name + "';");

					statement.executeUpdate();
					statement.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}
		}.runTaskAsynchronously(Main.getPlugin());

	}

}
