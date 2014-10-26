package io.github.apfelcreme.MbPets;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionManager {

	private static DatabaseConnectionManager instance = null;

	public static DatabaseConnectionManager getInstance() {
		if (instance == null) {
			instance = new DatabaseConnectionManager();
		}
		return instance;
	}

	private Connection connection;
	boolean dbConnectionAvailableAfterCheck;

	boolean dbConnectionAvailableBeforeCheck;

	public DatabaseConnectionManager() {
		connection = null;
		MbPets.getInstance()
				.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(
						MbPets.getInstance(),
						new Runnable() {

							public void run() {
								dbConnectionAvailableBeforeCheck = (connection == null) ? false
										: true;
								connection = getConnection();
								dbConnectionAvailableAfterCheck = (connection == null) ? false
										: true;
								if (!dbConnectionAvailableBeforeCheck
										&& dbConnectionAvailableAfterCheck) {
									MbPets.getInstance()
											.getLogger()
											.info("Regained Database connection");
								} else if (dbConnectionAvailableBeforeCheck
										&& !dbConnectionAvailableAfterCheck) {
									MbPets.getInstance().getLogger()
											.severe("Lost Database connection");
								}
								MbPets.getInstance()
										.setDbConnection(connection);
								MbPets.getInstance().setDbIsCurrentlyAvailable(
										connection != null);
							}

						},
						30L,
						MbPets.getInstance().getConfig()
								.getLong("db.refresh", 200L));
	}

	/**
	 * creates the necessary tables
	 * 
	 * @param database
	 */
	public void createDatabase(final String database) {
		MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(),	new Runnable() {
			public void run() {
				try {
					connection.createStatement().execute(
							"CREATE DATABASE IF NOT EXISTS " + database);
					connection.createStatement().execute(
							"CREATE TABLE IF NOT EXISTS MbPets_Player("
									+ "playerid BIGINT auto_increment not null,"
									+ "playername VARCHAR(50) UNIQUE NOT NULL,"
									+ "uuid VARCHAR(50) UNIQUE NOT NULL,"
									+ "PRIMARY KEY (playerid));");
					connection
							.createStatement()
							.execute(
									"CREATE TABLE IF NOT EXISTS MbPets_Pet("
											+ "petId BIGINT auto_increment not null,"
											+ "playerid BIGINT,"
											+ "customname VARCHAR(50),"
											+ "type VARCHAR(50),"
											+ "color VARCHAR(50),"
											+ "horsecolor VARCHAR(50),"
											+ "ocelotstyle VARCHAR(50),"
											+ "horsestyle VARCHAR(50),"
											+ "baby boolean,"
											+ "number BIGINT,"
											+ "FOREIGN KEY (playerid) REFERENCES MbPets_Player(playerid),"
											+ "PRIMARY KEY (petId));");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * returns the connection object
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() {
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://"
							+ MbPets.getInstance().getConfig()
									.getString("mysql.url", "")
							+ "/"
							+ MbPets.getInstance().getConfig()
									.getString("mysql.database", ""),
					MbPets.getInstance().getConfig()
							.getString("mysql.dbuser", ""),
					MbPets.getInstance().getConfig()
							.getString("mysql.dbpassword", ""));
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
	}

	/**
	 * inits the db connection
	 * 
	 * @param dbuser
	 * @param dbpassword
	 * @param database
	 * @param url
	 * @return
	 */
	public Connection initConnection(String dbuser, String dbpassword,
			String database, String url) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + url
					+ "/" + "", dbuser, dbpassword);
			if (database.equals(null) || database.isEmpty()) {
				return null;
			}
			if (connection != null) {
				try {
					connection.createStatement().execute(
							"CREATE DATABASE IF NOT EXISTS " + database);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			connection = DriverManager.getConnection("jdbc:mysql://" + url
					+ "/" + database, dbuser, dbpassword);
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
			return null;
		}
		createDatabase(database);
		return connection;
	}

}
