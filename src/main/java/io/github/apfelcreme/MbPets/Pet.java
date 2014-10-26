package io.github.apfelcreme.MbPets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.AnimalColor;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.AgeableWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.DroppedItemWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.HorseWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.OcelotWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SheepWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.WolfWatcher;
import net.milkbowl.vault.economy.plugins.Economy_iConomy6;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;

public class Pet {
	
	private AnimalColor color;
	private Material droppedItemType;
	private Entity entity;
	private Horse.Color horseColor;
	private Horse.Style horseStyle;
	private boolean isBaby;
	private boolean isModified;
	private String name;
	private int number;
	private Ocelot.Type ocelotStyle;
	private OfflinePlayer owner;
	private double price;
	private DisguiseType type;
	private boolean coloredTag;

	public Pet() {
		type = null;
		init();
	}

	public Pet(DisguiseType type) {
		this.type = type;
		init();
	}

	public Pet(Material droppedItemType) {
		this.droppedItemType = droppedItemType;
		init();
	}

	/**
	 * init
	 */
	private void init() {
		owner = null;
		name = "";
		number = 0;
		isBaby = false;
		horseColor = null;
		horseStyle = null;
		color = null;
		ocelotStyle = null;
		price = 0.0f;
		isModified = false;
		coloredTag = true;
	}

	/**
	 * is called on a stub pet-object and fills it with information from the
	 * database
	 * 
	 * @param player
	 * @throws SQLException
	 */
	public void call(final Player player) throws SQLException {
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
					public void run() {
						Connection con = DatabaseConnectionManager
								.getInstance().getConnection();
						PreparedStatement statement;
						ResultSet res;
						if (getNumber() > MbPetsUtils.getNumberOfPets(player)) {
							player.sendMessage(MbPetsUtils
									.getTextNode("error.notThatManyPets"));
							return;
						}
						try {
							statement = con
									.prepareStatement("Select * from MbPets_Pet where playerid = (Select playerid from MbPets_Player where uuid = ?) and number = ?;");
							statement.setString(1, player.getUniqueId()
									.toString());
							statement.setInt(2, getNumber());
							res = statement.executeQuery();
							if (res.first()) {
								setColor(MbPetsUtils.parseColor(res
										.getString("color")));
								setBaby(res.getBoolean("baby"));
								setHorseColor(MbPetsUtils.parseHorseColor(res
										.getString("horsecolor")));
								setHorseStyle(MbPetsUtils.parseHorseStyle(res
										.getString("horsestyle")));
								setOcelotStyle(MbPetsUtils.parseOcelotType(res
										.getString("ocelotstyle")));
								setOwner(player);
								if (MbPetsUtils.parseType(res.getString("type")) != null
										&& DisguiseType.valueOf(res
												.getString("type")) != null) {
									setType(MbPetsUtils.parseType(res
											.getString("type")));
								} else {
									if (MbPetsUtils.parseItemStack(res
											.getString("type")) != null) {
										setDroppedItemType(MbPetsUtils
												.parseItemStack(res
														.getString("type")));
										setType(DisguiseType.DROPPED_ITEM);
									} else {
										MbPets.getInstance()
												.getLogger()
												.severe("Beim Call-Prozess für Pet #"
														+ getNumber()
														+ " des Spielers "
														+ player.getName()
														+ " ist ein Fehler aufgetreten: Der in der Datenbank eingetragene Type-Wert kann keinem Entity-Typ zugeordnet werden");
									}
								}
								setName(res.getString("customname"));
								setPrice();
								MbPets.getInstance()
										.getServer()
										.getScheduler()
										.runTask(MbPets.getInstance(),
												new Runnable() {
													public void run() {
														spawn(MbPetsUtils
																.getTargetBlockLocation(player));
													}
												});
							} else {
								player.sendMessage(MbPetsUtils.getTextNode(
										"error.noPetToCall").replace("{0}",
										"Du"));
								return;
							}
						} catch (SQLException e) {

						}

					}
				});

	}

	/**
	 * confirms the pet by entering it into the db
	 * 
	 * @param number
	 * @throws SQLException
	 */
	public void confirm(final Player player) throws SQLException {
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
					public void run() {
						Connection con = DatabaseConnectionManager
								.getInstance().getConnection();
						if (MbPets.getInstance().getEconomy() == null
								|| !(MbPets.getInstance().getEconomy() instanceof Economy_iConomy6)) {
							player.sendMessage(MbPetsUtils
									.getTextNode("error.noVault"));
							return;
						}
						if (MbPets.getInstance().getEconomy().getBalance(owner) < getPrice()) {
							player.sendMessage(MbPetsUtils
									.getTextNode("error.notThatMuchMoney"));
							return;
						}
						try {
							PreparedStatement statement;
							// insert the player into database if it does not
							// exist yet
							statement = con
									.prepareStatement("INSERT IGNORE INTO MbPets_Player (playername, uuid) "
											+ "VALUES (?, ?);");
							statement.setString(1, getOwner().getName());
							statement.setString(2, getOwner().getUniqueId()
									.toString());
							statement.executeUpdate();
							statement.close();
							// deletes
							statement = con
									.prepareStatement("DELETE FROM MbPets_Pet WHERE playerid = (SELECT playerid from MbPets_Player where uuid = ?) AND number = ?");
							statement.setString(1, getOwner().getUniqueId()
									.toString());
							statement.setInt(2, getNumber());
							statement.executeUpdate();
							statement.close();
							statement = con
									.prepareStatement("INSERT INTO MbPets_Pet (playerid, type, customname, baby, color, horsecolor, horsestyle, ocelotstyle, number) "
											+ "VALUES ("
											+ "(SELECT playerid from MbPets_Player where uuid = ?),"
											+ "? , ? , ? , ? , ? , ? , ? , ? );");
							statement.setString(1, getOwner().getUniqueId()
									.toString());
							statement
									.setString(
											2,
											getType() != null
													&& getType() != DisguiseType.DROPPED_ITEM ? getType()
													.name()
													: getDroppedItemType()
															.name());
							statement.setString(3,
									getName() != null ? getName() : "");
							statement.setBoolean(4, isBaby());
							statement.setString(5,
									getColor() != null ? getColor().toString()
											: "");
							statement.setString(6,
									getHorseColor() != null ? getHorseColor()
											.toString() : "");
							statement.setString(7,
									getHorseStyle() != null ? getHorseStyle()
											.toString() : "");
							statement.setString(8,
									getOcelotStyle() != null ? getOcelotStyle()
											.toString() : "");
							statement.setInt(9, getNumber());
							statement.executeUpdate();
							statement.close();
						} catch (SQLException e) {

						}
					}
				});
		MbPets.getInstance()
				.getEconomy()
				.withdrawPlayer(
						getOwner(),
						isModified ? MbPets.getInstance().getConfig()
								.getDouble("prices.MODIFY") : getPrice());
		spawn(MbPetsUtils.getTargetBlockLocation(player));
		MbPets.getInstance().removePreparedPet(
				MbPets.getInstance().getPreparedPet(player));
		getOwner().getPlayer().sendMessage(
				MbPetsUtils.getTextNode("info.petConfirmed"));
	}

	/**
	 * @return the color
	 */
	public AnimalColor getColor() {
		return color;
	}

	/**
	 * @return the droppedItemType
	 */
	public Material getDroppedItemType() {
		return droppedItemType;
	}

	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * @return the horseColor
	 */
	public Horse.Color getHorseColor() {
		return horseColor;
	}

	/**
	 * @return the horseStyle
	 */
	public Horse.Style getHorseStyle() {
		return horseStyle;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @return the ocelotStyle
	 */
	public Ocelot.Type getOcelotStyle() {
		return ocelotStyle;
	}

	/**
	 * @return the owner
	 */
	public OfflinePlayer getOwner() {
		return owner;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return the type
	 */
	public DisguiseType getType() {
		return type;
	}

	/**
	 * @return the isBaby
	 */
	public boolean isBaby() {
		return isBaby;
	}

	/**
	 * @return the coloredTag
	 */
	public boolean isColoredTag() {
		return coloredTag;
	}

	/**
	 * @return the isModified
	 */
	public boolean isModified() {
		return isModified;
	}

	/**
	 * prints the current attributes of a players preparedPet
	 * 
	 * @param player
	 */
	public void printStatus(boolean isInfo) {
		getOwner().getPlayer().sendMessage(
				MbPetsUtils.getTextNode("info.StatusHeader").replace(
						"{0}",
						WordUtils.capitalize(getType().name().replace("_", " ")
								.toLowerCase())));
		getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Typ").replace("{1}", WordUtils.capitalize(getType().name().replace("_", " ").toLowerCase())));
		getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Name").replace("{1}", getName() != null && !getName().isEmpty() ? getName() : MbPetsUtils.getTextNode("help.NAME")));
		switch (getType()) {
		case CHICKEN:
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby()).toLowerCase())));
			break;
		case COW:
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby()).toLowerCase())));
			break;
		case DROPPED_ITEM:
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Typ").replace("{1}", getDroppedItemType() != null ? WordUtils.capitalize(getDroppedItemType().name().replace("_", " ").toLowerCase()): MbPetsUtils.getTextNode("help.DROPPEDITEMTYPE")));
			break;
		case HORSE:
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby()).toLowerCase())));
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Color").replace("{1}", getHorseColor() != null ? WordUtils.capitalize(getHorseColor().name().replace("_", " ").toLowerCase()): MbPetsUtils.getTextNode("help.HORSECOLOR")));
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Style").replace("{1}", getHorseStyle() != null ? WordUtils.capitalize(getHorseStyle().name().replace("_", " ").toLowerCase()): MbPetsUtils.getTextNode("help.HORSESTYLE")));
			break;
		case IRON_GOLEM:
			break;
		case MUSHROOM_COW:
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby()).toLowerCase())));
			break;
		case OCELOT:
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby()).toLowerCase())));
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Style").replace("{1}", getOcelotStyle() != null ? WordUtils.capitalize(getOcelotStyle().name().replace("_", " ").toLowerCase()) : MbPetsUtils.getTextNode("help.OCELOTSTYLE")));
			break;
		case PIG:
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby()).toLowerCase())));
			break;
		case SHEEP:
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby()).toLowerCase())));
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Color").replace("{1}", getColor() != null ? WordUtils.capitalize(getColor().name().replace("_", " ").toLowerCase()) : MbPetsUtils.getTextNode("help.COLOR")));
			break;
		case SKELETON_HORSE:
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby()).toLowerCase())));
			break;
		case UNDEAD_HORSE:
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby()).toLowerCase())));
			break;
		case WOLF:
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby()).toLowerCase())));
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Color").replace("{1}", getColor() != null ? WordUtils.capitalize(getColor().name().replace("_", " ").toLowerCase()) : MbPetsUtils.getTextNode("help.COLOR")));
			break;
		default:
			break;		
		}
		if (!isInfo) {
			getOwner().getPlayer().sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", "Preis").replace("{1}", isModified() ? Double.toString(MbPets.getInstance().getConfig().getDouble("prices.MODIFY")) : Double.toString(getPrice())));
			getOwner().getPlayer().sendMessage(
					MbPetsUtils.getTextNode("info.Confirm"));
		}

	}

	
	/**
	 * controlls if the all necessary attributes are set
	 */
	public boolean isConfigurationFinished() {

		switch (getType()) {
		case CHICKEN:
			return (getType() != null && (getName() != null && !getName().isEmpty()));
		case COW:
			return (getType() != null && (getName() != null && !getName().isEmpty()));
		case DROPPED_ITEM:
			return (getType() != null && (getName() != null && !getName().isEmpty()) && getDroppedItemType() != null);
		case HORSE:
			return (getType() != null && (getName() != null && !getName().isEmpty()) && getHorseColor() != null && getHorseStyle() != null);
		case IRON_GOLEM:
			return (getType() != null && (getName() != null && !getName().isEmpty()));
		case MUSHROOM_COW:
			return (getType() != null && (getName() != null && !getName().isEmpty()));
		case OCELOT:
		case PIG:
			return (getType() != null && (getName() != null && !getName().isEmpty()));
		case SHEEP:
			return (getType() != null && (getName() != null && !getName().isEmpty()) && getColor() != null);
		case SKELETON_HORSE:
			return (getType() != null && (getName() != null && !getName().isEmpty()));
		case UNDEAD_HORSE:
			return (getType() != null && (getName() != null && !getName().isEmpty()));
		case WOLF:
			return (getType() != null && (getName() != null && !getName().isEmpty()) && getColor() != null);
		default:
			return true;		
		}
	}
	/**
	 * @param isBaby
	 *            the isBaby to set
	 */
	public void setBaby(boolean isBaby) {
		this.isBaby = isBaby;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(AnimalColor color) {
		this.color = color;
	}

	/**
	 * @param coloredTag
	 *            the coloredTag to set
	 */
	public void setColoredTag(boolean coloredTag) {
		this.coloredTag = coloredTag;
	}

	/**
	 * @param droppedItemType
	 *            the droppedItemType to set
	 */
	public void setDroppedItemType(Material droppedItemType) {
		this.droppedItemType = droppedItemType;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * @param horseColor
	 *            the horseColor to set
	 */
	public void setHorseColor(Horse.Color horseColor) {
		this.horseColor = horseColor;
	}

	/**
	 * @param horseStyle
	 *            the horseStyle to set
	 */
	public void setHorseStyle(Horse.Style horseStyle) {
		this.horseStyle = horseStyle;
	}

	/**
	 * @param isModified
	 *            the isModified to set
	 */
	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @param ocelotStyle
	 *            the ocelotStyle to set
	 */
	public void setOcelotStyle(Ocelot.Type ocelotStyle) {
		this.ocelotStyle = ocelotStyle;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(OfflinePlayer owner) {
		this.owner = owner;
	}

	/**
	 */
	public void setPrice() {
		if (getType() == null) {
			return;
		}
		price = 0.0f;
		String reducedName = getName();
		setColoredTag(false);
		for (ChatColor c : ChatColor.values()) {
			if (reducedName.contains(c.toString()) || reducedName.contains("&"+c.getChar())){
				setColoredTag(true);
			}
			reducedName = reducedName.replace("&" + c.getChar(), c.toString());
		}
		if (isColoredTag()) {
			// name contains colors
			price += getPrice() + MbPets.getInstance().getConfig().getDouble("prices.COLOREDNAMETAG",1000);
			setName(reducedName);
		}
		if (getType() != null) {
			price += MbPets.getInstance().getConfig()
					.getDouble("prices." + getType().name(), 15000);
		} else {
			price += MbPets
					.getInstance()
					.getConfig()
					.getDouble("prices." + getDroppedItemType().name(),
							30000);
		}
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(DisguiseType type) {
		this.type = type;
	}

	/**
	 * spawns the pet-entity at the given {@link Location}
	 * 
	 * @param loc
	 * @return
	 */
	public boolean spawn(Location loc) {
		// remove a pet that may be there
		Pet oldPet = MbPets.getInstance().getPet(getOwner().getPlayer());
		if (oldPet != null) {
			oldPet.getEntity().remove();
			MbPets.getInstance().removePet(oldPet);
		}
		Disguise disguise = null;
		if (getType() != null && getType() != DisguiseType.DROPPED_ITEM) {
			disguise = new MobDisguise(getType());
		} else if (getDroppedItemType() != null) {
			disguise = new MiscDisguise(DisguiseType.DROPPED_ITEM);
		} else {
			return false;
		}
		disguise.setReplaceSounds(true);
		FlagWatcher flagWatcher = disguise.getWatcher();
		if (getType() != null) {
			switch (getType()) {
			case CHICKEN:
				((AgeableWatcher) flagWatcher).setBaby(isBaby());
				break;
			case COW:
				((AgeableWatcher) flagWatcher).setBaby(isBaby());
				break;
			case CREEPER:
				break;
			case DROPPED_ITEM:
				((DroppedItemWatcher) flagWatcher).setItemStack(new ItemStack(
						getDroppedItemType()));
				break;
			case HORSE:
				((HorseWatcher) flagWatcher).setBaby(isBaby());
				((HorseWatcher) flagWatcher)
						.setColor(getHorseColor() != null ? getHorseColor()
								: Horse.Color.BROWN);
				((HorseWatcher) flagWatcher)
						.setStyle(getHorseStyle() != null ? getHorseStyle()
								: Horse.Style.NONE);
				break;
			case MUSHROOM_COW:
				((AgeableWatcher) flagWatcher).setBaby(isBaby());
				break;
			case OCELOT:
				((OcelotWatcher) flagWatcher).setBaby(isBaby());
				((OcelotWatcher) flagWatcher)
						.setType(getOcelotStyle() != null ? getOcelotStyle()
								: Ocelot.Type.WILD_OCELOT);
				break;
			case SKELETON_HORSE:
			case UNDEAD_HORSE:
				((HorseWatcher) flagWatcher).setBaby(isBaby());
				break;
			case PIG:
				((AgeableWatcher) flagWatcher).setBaby(isBaby());
				break;
			case SHEEP:
				((SheepWatcher) flagWatcher).setBaby(isBaby());
				((SheepWatcher) flagWatcher)
						.setColor(getColor() != null ? getColor()
								: AnimalColor.WHITE);
				break;
			case WOLF:
				((WolfWatcher) flagWatcher).setBaby(isBaby());
				((WolfWatcher) flagWatcher)
						.setCollarColor(getColor() != null ? getColor()
								: AnimalColor.RED);
				break;
			default:
				break;
			}
			disguise.setWatcher(flagWatcher);
			DisguiseAPI.disguiseNextEntity(disguise);
			// spawn the entity to be disguised
			entity = loc.getWorld().spawnEntity(loc, EntityType.WOLF);
			((Wolf) entity).setAngry(false);
			// ((Wolf) entity).setTarget(getOwner().getPlayer());
			((Wolf) entity).setTamed(true);
			((Wolf) entity).setOwner(getOwner());
			((Wolf) entity).setCustomName(getName() != null
					&& !getName().isEmpty() ? ("[" + getOwner().getName()
					+ "] " + getName()) : "");
			MbPets.getInstance().addPet(this);
			MbPets.getInstance()
					.getLogger()
					.info(getOwner().getName() + "("
							+ getOwner().getUniqueId().toString()
							+ ") spawned a " + getType().name()
							+ " at Location (X:"
							+ getEntity().getLocation().getX() + " Y: "
							+ getEntity().getLocation().getY() + "+ Z: "
							+ getEntity().getLocation().getZ() + ")");
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pet [owner=" + owner + ", entity=" + entity + ", name=" + name
				+ ", isBaby=" + isBaby + ", type=" + type + ", horseColor="
				+ horseColor + ", horseStyle=" + horseStyle + ", color="
				+ color + ", ocelotStyle=" + ocelotStyle + ", droppedItemType="
				+ droppedItemType + ", price=" + price + ", isModified="
				+ isModified + "]";
	}
}
