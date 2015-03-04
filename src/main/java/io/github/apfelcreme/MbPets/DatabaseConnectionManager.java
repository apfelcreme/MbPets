package io.github.apfelcreme.MbPets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

	private static DatabaseConnectionManager instance = null;
	
	/**
	 * getInstance
	 * @return the classes only instance
	 */
	public static DatabaseConnectionManager getInstance() {
		if (instance == null) {
			instance = new DatabaseConnectionManager();
		}
		return instance;
	}
	
	/**
	 * initializes the database connection
	 * @param dbuser
	 * @param dbpassword
	 * @param database
	 * @param url
	 * @return
	 */
	public Connection initConnection(String dbuser, String dbpassword, String database, String url) {
		Connection connection;
		try {
			if (database.isEmpty() || database == null) {
				return null;
			} else {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + url
						+ "/" + "", dbuser, dbpassword);
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
				createDatabase(database);
				return connection;
			}
		} catch(SQLException e) {
		} catch (ClassNotFoundException e1) {
		}
		return null;
	}
	
	/**
	 * returns the database connection to work with
	 * @return
	 */
	public Connection getConnection() {		
		try {
			return DriverManager.getConnection("jdbc:mysql://" + 
					MbPets.getInstance().getConfig().getString("mysql.url")
					+ "/" + 
					MbPets.getInstance().getConfig().getString("mysql.database"), 
					MbPets.getInstance().getConfig().getString("mysql.dbuser"), 
					MbPets.getInstance().getConfig().getString("mysql.dbpassword"));
		} catch (SQLException e) {
			MbPets.getInstance().getLogger().severe("Database connection could not be built");
		}
		return null;
	}
	
	/**
	 * creates the necessary tables
	 * 
	 * @param database the database name
	 */
	private void createDatabase(final String database) {
		MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(),	new Runnable() {
			public void run() {
				try {
					getConnection().createStatement().execute(
							"CREATE DATABASE IF NOT EXISTS " + database);
					getConnection().createStatement().execute(
							"CREATE TABLE IF NOT EXISTS MbPets_Player("
									+ "playerid BIGINT auto_increment not null,"
									+ "playername VARCHAR(50) NOT NULL,"
									+ "uuid VARCHAR(50) UNIQUE NOT NULL,"
									+ "PRIMARY KEY (playerid));");
					getConnection()
							.createStatement()
							.execute(
									"CREATE TABLE IF NOT EXISTS MbPets_Pet("
											+ "petId BIGINT auto_increment not null,"
											+ "playerid BIGINT,"
											+ "petname VARCHAR(50),"
											+ "type VARCHAR(50),"
											+ "sheepcolor VARCHAR(50),"
											+ "wolfcolor VARCHAR(50),"
											+ "horsecolor VARCHAR(50),"
											+ "horsestyle VARCHAR(50),"
											+ "ocelotstyle VARCHAR(50),"
											+ "rabbittype VARCHAR(50),"
											+ "material VARCHAR(50),"
											+ "block VARCHAR(50),"
											+ "baby boolean,"
											+ "size TINYINT,"
											+ "number BIGINT,"
											+ "FOREIGN KEY (playerid) REFERENCES MbPets_Player(playerid),"
											+ "PRIMARY KEY (petId));");
					getConnection().close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
}
