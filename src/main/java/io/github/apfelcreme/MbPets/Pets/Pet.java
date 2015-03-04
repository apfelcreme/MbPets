package io.github.apfelcreme.MbPets.Pets;

import java.sql.Connection;
import java.sql.SQLException;

import io.github.apfelcreme.MbPets.DatabaseConnectionManager;
import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Interfaces.DroppedItem;
import io.github.apfelcreme.MbPets.Interfaces.FallingBlock;
import io.github.apfelcreme.MbPets.Tasks.FollowTask;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

import java.sql.PreparedStatement;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

public abstract class Pet {

	OfflinePlayer owner = null;
	String name = null;
	DisguiseType type = null;
	Double price = 0.0;
	Entity entity = null;
	Integer number = null;
	Disguise disguise = null;
	Integer taskId = null;
	
	public Pet(OfflinePlayer owner, String name, DisguiseType type, Integer number) {
		this.owner = owner;
		this.name = stripName(name);
		this.type = type;
		this.price = MbPetsConfig.getPetPrice(type);
		this.number = number;

	}

	/**
	 * returns the pets owner;
	 * @return
	 */
	public OfflinePlayer getOwner() {
		return owner;
	}
	
	/**
	 * sets the pets owner
	 * @param owner
	 */
	public void setOwner(OfflinePlayer owner) {
		this.owner = owner;
	}
	
	/**
	 * returns the name
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * sets the name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * returns the price
	 * @return
	 */
	public Double getPrice() {
		return price;
	}
	
	/**
	 * sets the price
	 * @param price
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	
	/**
	 * returns the stored entity object
	 * @return
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * stores the pets entity object
	 * @param entity
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	/**
	 * returns the pets {@link DisguiseType}
	 * @return
	 */
	public DisguiseType getType() {
		return type;
	}
	
	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	/**
	 * @return the disguise
	 */
	public Disguise getDisguise() {
		return disguise;
	}

	/**
	 * @return the taskId
	 */
	public Integer getTaskId() {
		return taskId;
	}

	/**
	 * replaces chat color codes like "&6" and replaces it with "ChatColor.GOLD"
	 * @param oldName
	 * @return
	 */
	private String stripName(String oldName) {
		if (oldName == null) {
			return null;
		}
		String newName = oldName;
		for (ChatColor c : ChatColor.values()) {
			newName = newName.replace("&" + c.getChar(), c.toString());
		}
		return newName;
	}

	/**
	 * calls the pet
	 */
	public void call() {
		spawn();
	}
	
	/**
	 * uncalls the pet
	 */
	public void uncall() {
		MbPets.getInstance().getServer().getScheduler().cancelTask(taskId);
		if (entity != null) entity.remove();
		MbPets.getInstance().getPets().remove(owner);
	}
	
	/**
	 * spawns the pet
	 */
	public void spawn() {
		final Pet thisPet = this;
		MbPets.getInstance().getServer().getScheduler().runTask(MbPets.getInstance(), new Runnable() {
			@Override
			public void run() {
				Pet oldPet = MbPets.getInstance().getPets().get(owner.getPlayer());
				if (oldPet != null) {
					oldPet.uncall();
				}
				if (thisPet instanceof DroppedItem) {
					disguise = new MiscDisguise(DisguiseType.DROPPED_ITEM);
				} else if ( thisPet instanceof FallingBlock) {
					disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK);
				} else {
					disguise = new MobDisguise(type);
				} 
				FlagWatcher watcher = disguise.getWatcher();
				
				// go into the subclasses and let them modify the watcher
				watcher = prepareWatcher(watcher);
				disguise.setWatcher(watcher);
				disguise.setReplaceSounds(true);
				DisguiseAPI.disguiseNextEntity(disguise);
				entity = owner.getPlayer().getWorld().spawnEntity(owner.getPlayer().getLocation(), EntityType.WOLF);

				((Wolf) entity).setAngry(false);
				((Wolf) entity).setTamed(true);
				((Wolf) entity).setOwner(owner);
				MbPets.getInstance().getPets().put(owner.getPlayer(), thisPet);
				taskId = MbPets.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(MbPets.getInstance(), new FollowTask(thisPet), 0, 50L);
			}});
	}
	
	/**
	 * confirms the pet and writes its attributes to the db
	 */
	public void confirm() {
		MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				try {
					Connection connection = DatabaseConnectionManager.getInstance().getConnection();
					PreparedStatement preparedStatement;
					//Insert a player;
					preparedStatement = connection.prepareStatement("INSERT IGNORE INTO MbPets_Player (uuid, playername) VALUES (?, ?)");
					preparedStatement.setString(1, owner.getUniqueId().toString());	
					preparedStatement.setString(2, owner.getName());	
					preparedStatement.executeUpdate();
					preparedStatement.close();			
					
					//Insert a pet
					preparedStatement = prepareConfirmStatement(connection);
					preparedStatement.executeUpdate();
					preparedStatement.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}});
	}
	
	/**
	 * removes the pet from the database
	 * 
	 * @param connection
	 */
	public void delete() {
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
					public void run() {
						try {
							Connection connection = DatabaseConnectionManager.getInstance().getConnection();
							PreparedStatement statement;
							statement = connection
									.prepareStatement("DELETE from MbPets_Pet WHERE playerid = (Select playerid from MbPets_Player where uuid=?) AND number = ?");
							statement.setString(1, owner.getUniqueId()
									.toString());
							statement.setInt(2, number);
							statement.executeUpdate();
							statement.close();
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	/**
	 * removes the pet from the db and returns a specific percentage of the price
	 */
	public Double sell() {
		Double price = MbPetsConfig.getPetPrice(type) * MbPets.getInstance().getConfig().getDouble("prices.SELL", 0.75);
		EconomyResponse response = MbPets.getInstance().getEconomy().depositPlayer(owner, price);
		if (response.transactionSuccess()) {
			this.delete();
			return price;
		}
		return -1.0;
	}
	
	/**
	 * returns a information String which is later sended to the user
	 * @return
	 */
	public String toString() {		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(MbPetsConfig.getTextNode("info.StatusHeader").replace("{0}", MbPets.getInstance().getConfig().getString("disguiseTypes."+type.name()+".displaytext"))+"\n");
		stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Typ").replace("{1}", MbPets.getInstance().getConfig().getString("disguiseTypes."+type.name()+".displaytext"))+"\n");
		stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Name").replace("{1}", getName() != null && !getName().isEmpty() ? getName() : ChatColor.DARK_GRAY + MbPetsConfig.getTextNode("help.NAME"))+"\n");
		stringBuilder = appendStatus(stringBuilder);
		stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Preis").replace("{1}", getPrice().toString())+"\n");
		stringBuilder.append(MbPetsConfig.getTextNode("info.Confirm"));
		return stringBuilder.toString();
	}
	
	/**
	 * prints the same as toString() but without the price and the first and last line
	 * @return
	 */
	public String toShortString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Typ").replace("{1}", MbPets.getInstance().getConfig().getString("disguiseTypes."+type.name()+".displaytext"))+"\n");
		stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Name").replace("{1}", getName() != null && !getName().isEmpty() ? getName() : MbPetsConfig.getTextNode("help.NAME"))+"\n");
		stringBuilder = appendStatus(stringBuilder);
		return stringBuilder.toString();
	}
	
	/**
	 * adds the specific status information that is later printed to the user
	 * @param stringBuilder
	 * @return
	 */
	public abstract StringBuilder appendStatus(StringBuilder stringBuilder);
	
	/**
	 * prepares the {@link FlagWatcher} for the specific pet type
	 * @param watcher
	 * @return
	 */
	public abstract FlagWatcher prepareWatcher(FlagWatcher watcher);

	/**
	 * prepares the {@link PreparedStatement} for a later database insert
	 * @param connection 
	 * @param statement
	 * @return
	 */
	public abstract PreparedStatement prepareConfirmStatement(Connection connection) throws SQLException;
	
	/**
	 * checks if all necessary attributes for a pets creation have been set
	 * @return
	 */
	public abstract boolean isConfigurationFinished();
	
	/**
	 * returns a String that is appended when a player enters /pet list 
	 * @return
	 */
	public String getListDescription() {
		return MbPetsConfig
				.getTextNode("info.listElement")
				.replace(
						"{0}", number.toString())
				.replace(
						"{1}", name == null || name.isEmpty() ? "unbenannt"
								: name)
				.replace(
						"{2}", MbPets.getInstance().getConfig().getString("disguiseTypes."+type.name()+".displaytext"));
	}
	
}
