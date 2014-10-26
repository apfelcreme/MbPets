package io.github.apfelcreme.MbPets;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import me.libraryaddict.disguise.LibsDisguises;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.zaiyers.AnimalProtect.AnimalProtect;
import net.zaiyers.bukkit.UUIDDB.UUIDDB;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MbPets extends JavaPlugin implements Listener {

	/**
	 * returns instance
	 * 
	 * @return
	 */
	public static MbPets getInstance() {
		return (MbPets) Bukkit.getServer().getPluginManager()
				.getPlugin("MbPets");
	}

	ConvertRightClickListener convertRightClickListener;
	Connection dbConnection;
	boolean dbIsCurrentlyAvailable;

	HashMap<Player, Pet> pets;

	AnimalProtect pluginAnimalProtect;
	LibsDisguises pluginLibsDisguises;

	UUIDDB pluginUuidDb;
	Vault pluginVault;

	HashMap<Player, Pet> preparedPets;

	/**
	 * adds a pet to the List
	 */
	public void addPet(Pet pet) {
		pets.put(pet.getOwner().getPlayer(), pet);
	}

	/**
	 * cancels all interacts on entities
	 * @param e
	 */
	@EventHandler
	public void onEntityInteract(EntityInteractEvent e) {
		if (getPets().containsValue(getPetByEntity(e.getEntity()))) {
			// a pet is rightclicked
			e.setCancelled(true);
		}
	}
	
	/**
	 * adds a pet to the List
	 */
	public void addPreparedPet(Pet pet) {
		preparedPets.put(pet.getOwner().getPlayer(), pet);
	}

	/**
	 * @return the convertRightClickListener
	 */
	public ConvertRightClickListener getConvertRightClickListener() {
		return convertRightClickListener;
	}

	/**
	 * @return the dbConnection
	 */
	public Connection getDbConnection() {
		return dbConnection;
	}

	/**
	 * returns the servers Economy
	 * 
	 * @return Vault economy object
	 */
	public Economy getEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = MbPets
				.getInstance().getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		Economy economy = null;
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		return economy;
	}

	/**
	 * returns a pet from the list
	 * 
	 * @param player
	 * @return
	 */
	public Pet getPet(Player player) {
		return pets.get(player);
	}

	public Pet getPetByEntity(Entity entity) {
		for (Pet p : pets.values()) {
			if (p.getEntity().equals(entity)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * @return the pets
	 */
	public HashMap<Player, Pet> getPets() {
		return pets;
	}

	/**
	 * @return the pluginAnimalProtect
	 */
	public AnimalProtect getPluginAnimalProtect() {
		return pluginAnimalProtect;
	}

	/**
	 * @return the pluginLibsDisguises
	 */
	public LibsDisguises getPluginLibsDisguises() {
		return pluginLibsDisguises;
	}

	/**
	 * @return the pluginUuidDb
	 */
	public UUIDDB getPluginUuidDb() {
		return pluginUuidDb;
	}

	/**
	 * @return the pluginVault
	 */
	public Vault getPluginVault() {
		return pluginVault;
	}

	/**
	 * returns a pet from the list
	 * 
	 * @param player
	 * @return
	 */
	public Pet getPreparedPet(Player player) {
		return preparedPets.get(player);
	}

	/**
	 * @return the preparedPets
	 */
	public HashMap<Player, Pet> getPreparedPets() {
		return preparedPets;
	}

	/**
	 * @return the dbIsCurrentlyAvailable
	 */
	public boolean isDbIsCurrentlyAvailable() {
		return dbIsCurrentlyAvailable;
	}

	/**
	 * closes the db connection
	 */
	@Override
	public void onDisable() {
		if (!getConfig().getString("mysql.dbuser").isEmpty()
				&& !getConfig().getString("mysql.dbpassword").isEmpty()
				&& !getConfig().getString("mysql.database").isEmpty()
				&& !getConfig().getString("mysql.url").isEmpty()) {
			try {
				DatabaseConnectionManager.getInstance().getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		for (Pet pet : pets.values()) {
			pet.getEntity().remove();
		}
		pets.clear();
		preparedPets.clear();
	}

	/**
	 * onEnable
	 */
	@Override
	public void onEnable() {
		pets = new HashMap<Player, Pet>();
		preparedPets = new HashMap<Player, Pet>();
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
			getConfig().set("mysql.dbuser", "");
			getConfig().set("mysql.dbpassword", "");
			getConfig().set("mysql.database", "");
			getConfig().set("mysql.url", "");
			getLogger()
					.severe("Bitte die Datenbank-Informationen in die config.yml eintragen");
			saveConfig();
			MbPetsUtils.init();
			getPluginLoader().disablePlugin(this);
			return;
		}
		dbConnection = DatabaseConnectionManager.getInstance().initConnection(
				getConfig().getString("mysql.dbuser", ""),
				getConfig().getString("mysql.dbpassword", ""),
				getConfig().getString("mysql.database", ""),
				getConfig().getString("mysql.url", ""));
		if (dbConnection == null) {
			getLogger().severe("Database connection could not be established");
		}
		pluginUuidDb = (UUIDDB) getServer().getPluginManager().getPlugin(
				"UUIDDB");
		if (pluginUuidDb == null) {
			getLogger().severe(
					"Plugin UUIDDB fehlt!. Plugin wurde nicht aktiviert.");
			getServer().getPluginManager().disablePlugin(this);
		}
		pluginLibsDisguises = (LibsDisguises) getServer().getPluginManager()
				.getPlugin("LibsDisguises");
		if (pluginLibsDisguises == null) {
			getLogger()
					.severe("Plugin LibsDisguises fehlt!. Plugin wurde nicht aktiviert.");
			getServer().getPluginManager().disablePlugin(this);
		}
		pluginVault = (Vault) getServer().getPluginManager().getPlugin("Vault");
		if (pluginVault == null) {
			getLogger()
					.warning(
							"Plugin Vault fehlt! Das Kaufen und Converten von Pets wird auf diesem Server nicht funktionieren!");
		}

		pluginAnimalProtect = (AnimalProtect) getServer().getPluginManager()
				.getPlugin("AnimalProtect");
		if (pluginAnimalProtect == null) {
			getLogger().warning("Plugin AnimalProtect fehlt!");
		}
		getServer().getPluginCommand("pet").setExecutor(new MbPetsCommand());
		getServer().getPluginManager().registerEvents(this, this);
		convertRightClickListener = new ConvertRightClickListener();
		getServer().getPluginManager().registerEvents(
				convertRightClickListener, this);
	}

	/**
	 * removes Entity from the activePets-List when going through a Nether
	 * Portal bc this caused some kind of endless replication bug...
	 * 
	 * @param event
	 */
	@EventHandler
	public void onEnterNetherPortal(EntityPortalEnterEvent event) {
		if (event.getEntity() instanceof Wolf) {// all pets are disguised wolves
			if (getPetByEntity(event.getEntity()) != null) {
				// a pet ran through a portal
				getPetByEntity(event.getEntity()).getEntity().remove();
				removePet(getPetByEntity(event.getEntity()));
			}
		}
	}

	/**
	 * cancels all damage events to make pets invulnerable
	 * 
	 * @param event
	 */
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (getPetByEntity(event.getEntity()) != null) {
			event.setCancelled(true);
		}
	}

	/**
	 * removes active pets on player logout
	 * 
	 * @param e
	 */
	@EventHandler
	public void onLogout(PlayerQuitEvent e) {
		if (pets.containsKey(e.getPlayer())) {
			getPet(e.getPlayer()).getEntity().remove();
			pets.remove(e.getPlayer());
		}
	}

	/**
	 * removes a pet from the list
	 * 
	 * @param pet
	 */
	public void removePet(Pet pet) {
		pets.remove(pet.getOwner().getPlayer());
	}

	/**
	 * removes a pet from the list
	 * 
	 * @param player
	 */
	public void removePet(Player player) {
		pets.remove(player);
	}

	/**
	 * removes a pet from the list
	 * 
	 * @param pet
	 */
	public void removePreparedPet(Pet pet) {
		preparedPets.remove(pet.getOwner().getPlayer());
	}

	/**
	 * removes a pet from the list
	 * 
	 * @param player
	 */
	public void removePreparedPet(Player player) {
		preparedPets.remove(player);
	}

	/**
	 * @param dbConnection
	 *            the dbConnection to set
	 */
	public void setDbConnection(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}

	/**
	 * @param dbIsCurrentlyAvailable
	 *            the dbIsCurrentlyAvailable to set
	 */
	public void setDbIsCurrentlyAvailable(boolean dbIsCurrentlyAvailable) {
		this.dbIsCurrentlyAvailable = dbIsCurrentlyAvailable;
	}

}
