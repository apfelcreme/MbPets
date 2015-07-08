package io.github.apfelcreme.MbPets;

import io.github.apfelcreme.MbPets.Listener.*;
import io.github.apfelcreme.MbPets.Pets.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.zaiyers.AnimalProtect.AnimalProtect;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * MbPets 
 * Copyright (C) 2015 Lord36 aka Apfelcreme
 * 
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Lord36 aka Apfelcreme
 * 
 */
public class MbPets extends JavaPlugin {

	/**
	 * a {@link HashMap} which contains players with their currently spawned
	 * pets
	 */
	private HashMap<Player, Pet> pets;

	/**
	 * a {@link HashMap} which contains all current configurations (= Pets which
	 * are currently in configuration and not yet spawned nor confirmed)
	 */
	private HashMap<Player, Pet> configurations;

	/**
	 * the instance of the {@link AnimalProtect} plugin
	 */
	private AnimalProtect pluginAnimalProtect = null;

	/**
	 * the instance of the {@link Vault} plugin
	 */
	private Vault pluginVault = null;

	/**
	 * the event listener for the animal2Pet converting
	 */
	private ConvertRightclickListener convertRightclickListener;

	public void onEnable() {
		//set enabled on plugin load
		// create config if necessary
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
			getConfig().set("mysql.dbuser", "");
			getConfig().set("mysql.dbpassword", "");
			getConfig().set("mysql.database", "");
			getConfig().set("mysql.url", "");
			MbPetsConfig.init();
			getLogger()
					.severe("Bitte die Datenbank-Informationen in die config.yml eintragen");
			saveConfig();
		}
		
		// initialize some lists
		pets = new HashMap<Player, Pet>();
		configurations = new HashMap<Player, Pet>();
		convertRightclickListener = new ConvertRightclickListener();

		// register commands and listener
		getServer().getPluginCommand("pet").setExecutor(new MbPetsCommand());
		getServer().getPluginCommand("pet").setTabCompleter(
				new MbPetsTabCompleter());
		getServer().getPluginManager().registerEvents(
				convertRightclickListener, this);
		getServer().getPluginManager().registerEvents(
				new EntityDamageListener(), this);
		getServer().getPluginManager().registerEvents(
				new EntityInteractListener(), this);
		getServer().getPluginManager().registerEvents(
				new NetherPortalEnterListener(), this);
		getServer().getPluginManager().registerEvents(
				new PlayerLogoutListener(), this);
		getServer().getPluginManager().registerEvents(
				new EntityRightClickListener(), this);
		getServer().getPluginManager().registerEvents(
				new EntityDamagesEntityListener(), this);
		getServer().getPluginManager().registerEvents(
				new PlayerTeleportListener(), this);

		// initialize the db connection
		DatabaseConnectionManager.getInstance().initConnection(
				getConfig().getString("mysql.dbuser"),
				getConfig().getString("mysql.dbpassword"),
				getConfig().getString("mysql.database"),
				getConfig().getString("mysql.url"));

		// get the plugin instances
		if (getServer().getPluginManager().getPlugin("UUIDDB") == null) {
			getLogger().severe("Plugin UUIDDB nicht gefunden!");
			getServer().getPluginManager().disablePlugin(this);
		}
		if (getServer().getPluginManager().getPlugin("AnimalProtect") != null) {
			pluginAnimalProtect = (AnimalProtect) getServer()
					.getPluginManager().getPlugin("AnimalProtect");
		}
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			pluginVault = (Vault) getServer().getPluginManager().getPlugin(
					"Vault");
		}

	}

	public void onDisable() {
		try {
			DatabaseConnectionManager.getInstance().getConnection().close();
		} catch (SQLException e) {
		}
		for (Entry<Player, Pet> entry : getPets().entrySet()) {
			if (entry.getValue().getEntity() != null) {
				entry.getValue().getEntity().remove();
			}
		}
	}

	/**
	 * returns the plugin instance
	 * 
	 * @return
	 */
	public static MbPets getInstance() {
		return (MbPets) Bukkit.getServer().getPluginManager()
				.getPlugin("MbPets");
	}

	/**
	 * @return the pets
	 */
	public HashMap<Player, Pet> getPets() {
		return pets;
	}

	/**
	 * @return the configurations
	 */
	public HashMap<Player, Pet> getConfigurations() {
		return configurations;
	}

	/**
	 * @return the convertRightclickListener
	 */
	public ConvertRightclickListener getConvertRightclickListener() {
		return convertRightclickListener;
	}

	/**
	 * @return the pluginAnimalProtect
	 */
	public AnimalProtect getPluginAnimalProtect() {
		return pluginAnimalProtect;
	}

	/**
	 * @return the pluginUuidDb
	 */
	public Vault getPluginVault() {
		return pluginVault;
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
	 * retrieves information from the database and stores it into a {@link Pet}
	 * object. should only be accessed from an asynchronous task
	 * 
	 * @param player
	 * @param number
	 * @return
	 * @throws SQLException
	 */
	public Pet getPet(OfflinePlayer player, Integer number) throws SQLException {
		if (number == null || player == null) {
			return null;
		}
		PreparedStatement statement;
		ResultSet res;
		Connection connection = DatabaseConnectionManager.getInstance()
				.getConnection();
		statement = connection
				.prepareStatement("SELECT * FROM MbPets_Pet WHERE playerid = (Select playerid from MbPets_Player where uuid = ?) and number = ? ");
		statement.setString(1, player.getUniqueId().toString());
		statement.setInt(2, number);
		res = statement.executeQuery();
		Pet pet = null;
		if (res.first()) { 
			switch (MbPetsConfig.parseType(res.getString("type"))) {
			case CHICKEN:
				pet = new ChickenPet(player, res.getString("petname"), number,
						res.getBoolean("baby"));
				break;
			case COW:
				pet = new CowPet(player, res.getString("petname"), number,
						res.getBoolean("baby"));
				break;
			case DROPPED_ITEM:
				switch (MbPetsConfig.parseMaterial(res.getString("material"))) {
				case OBSIDIAN:
					pet = new DevilPet(player, res.getString("petname"), number);
					break;
				case SNOW_BLOCK:
					pet = new AngelPet(player, res.getString("petname"), number);
					break;
				default:
					pet = new DroppedItemPet(player, res.getString("petname"),
							number, MbPetsConfig.parseMaterial(res.getString("material")));
					break;
				}
				break;
			case ENDERMAN: 
				pet = new EndermanPet(player, res.getString("petname"), number);
				break;
			case FALLING_BLOCK:
				pet = new FallingBlockPet(player, res.getString("petname"), number, 
						MbPetsConfig.parseBlock(res.getString("block")));
				break;
			case HORSE:
				pet = new HorsePet(player, res.getString("petname"), number,
						res.getBoolean("baby"),
						MbPetsConfig.parseHorseColor(res.getString("horsecolor")),
						MbPetsConfig.parseHorseStyle(res.getString("horsestyle")));
				break;
			case IRON_GOLEM:
				pet = new IronGolemPet(player, res.getString("petname"), number);
				break;
			case MAGMA_CUBE:
				pet = new MagmaCubePet(player, res.getString("petname"), number, res.getInt("size"));
				break;
			case MUSHROOM_COW:
				pet = new MooshroomPet(player, res.getString("petname"),
						number, res.getBoolean("baby"));
				break;
			case OCELOT:
				pet = new OcelotPet(player, res.getString("petname"), number,
						res.getBoolean("baby"),
						MbPetsConfig.parseOcelotType(res.getString("ocelotstyle")));
				break;
			case PIG:
				pet = new PigPet(player, res.getString("petname"), number,
						res.getBoolean("baby"));
				break;
			case PRIMED_TNT:
				pet = new PrimedTnTPet(player, res.getString("petname"), number);
				break;
			case RABBIT:
				pet = new RabbitPet(player, res.getString("petname"), number,
						res.getBoolean("baby"),
						MbPetsConfig.parseRabbitType(res.getString("rabbittype")));
				break;
			case SLIME:
				pet = new SlimePet(player, res.getString("petname"), number, res.getInt("size"));
				break;
			case SHEEP:
				pet = new SheepPet(player, res.getString("petname"), number,
						res.getBoolean("baby"),
						MbPetsConfig.parseColor(res.getString("sheepcolor")));
				break;
			case SKELETON_HORSE:
				pet = new SkeletonHorsePet(player, res.getString("petname"),
						number, res.getBoolean("baby"));
				break;
			case UNDEAD_HORSE:
				pet = new UndeadHorsePet(player, res.getString("petname"),
						number, res.getBoolean("baby"));
				break;
			case WOLF:
				pet = new WolfPet(player, res.getString("petname"), number,
						res.getBoolean("baby"),
						MbPetsConfig.parseColor(res.getString("wolfcolor")));
				break;
			default:
			}
			connection.close();
			return pet;
		} else {
			return null;
		}
	}
	
	/**
	 * returns the pet object of the given entity or null if the entity isnt a
	 * pet
	 * 
	 * @param entity
	 * @return
	 */
	public Pet getPetByEntity(Entity entity) {
		for (Entry<Player, Pet> petEntry : getPets().entrySet()) {
			if (petEntry.getValue().getEntity().equals(entity)) {
				return petEntry.getValue();
			}
		}
		return null;
	}

	/**
	 * returns the number of pets the given player owns. access only from an
	 * asychronous task
	 * 
	 * @param owner
	 * @return
	 */
	public static int getLatestPetNumber(OfflinePlayer owner) {
		int ret = 0;
		try {
			Connection connection = DatabaseConnectionManager.getInstance()
					.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("Select max(number) as number from MbPets_Pet where playerid = (SELECT playerid from MbPets_Player where uuid = ?)");
			statement.setString(1, owner.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			ret = (res.first() && res.getString("number") != null ? res
					.getInt("number") : 0);
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

}
