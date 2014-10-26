package io.github.apfelcreme.MbPets;

import io.github.apfelcreme.MbPets.MbPetsUtils.Keys;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class MbPetsCommand implements CommandExecutor, TabCompleter {

	MbPets plugin;

	public MbPetsCommand() {
		plugin = MbPets.getInstance();
	}

	/**
	 * calls the wanted pet.
	 * 
	 * @param argsMap
	 * @param player
	 * @throws SQLException
	 */
	private void callPet(HashMap<MbPetsUtils.Keys, String> argsMap,
			Player player) throws SQLException {
		Connection con = DatabaseConnectionManager.getInstance()
				.getConnection();
		if (!player.hasPermission("MbPets.call")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		if (con == null) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noDbConnection"));
			return;
		}
		Pet pet = new Pet();
		pet.setNumber(argsMap.get(MbPetsUtils.Keys.NUMBER) != null ? Integer
				.parseInt(argsMap.get(MbPetsUtils.Keys.NUMBER)) : (MbPetsUtils
				.getNumberOfPets(player)));
		pet.call(player);
	}

	/**
	 * removes the players preparedPet from the preparedPets-List, if there is
	 * one
	 * 
	 * @param player
	 *            or commandSender
	 */
	private void cancelPreparation(Player player) {
		if (!player.hasPermission("MbPets.buy")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		if (MbPets.getInstance().getPreparedPets().containsKey(player)) {
			MbPets.getInstance().getPreparedPets().remove(player);
			player.sendMessage(MbPetsUtils
					.getTextNode("info.preparedPetCanceled"));
		} else {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPreparedPets"));
		}

	}

	/**
	 * finishes the confirming process and enters the pet into the database
	 * 
	 * @param argsMap
	 *            the chat input in a HashMap<MbPetUtils.Keys, String)
	 * @param player
	 *            or commandSender
	 * @throws SQLException
	 */
	private void confirmPet(HashMap<MbPetsUtils.Keys, String> argsMap,
			Player player) throws SQLException {
		Connection con = DatabaseConnectionManager.getInstance()
				.getConnection();
		if (!player.hasPermission("MbPets.confirm")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		if (con == null) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noDbConnection"));
			return;
		}
		if (MbPets.getInstance().getPluginVault() == null) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noVault"));
			return;
		}
		Pet pet = MbPets.getInstance().getPreparedPet(player);
		if (pet != null) {
			System.out.println(pet.toString());
			if (!pet.isModified()) {
				pet.setNumber(argsMap.get(MbPetsUtils.Keys.NUMBER) != null ? Integer
						.parseInt(argsMap.get(MbPetsUtils.Keys.NUMBER))
						: (MbPetsUtils.getNumberOfPets(player) + 1));
			}
			if (pet.isConfigurationFinished()) {
				if (pet.getEntity() != null) {
					// if the to-spawn entity is a requested convert, the "old"
					// entity is stored in pet.entity.
					// it gets despawned now
					if (MbPets.getInstance().getPluginAnimalProtect() != null) {
						if (MbPets
								.getInstance()
								.getPluginAnimalProtect()
								.hasOwner(
										pet.getEntity().getUniqueId()
												.toString())) {
							MbPets.getInstance()
									.getPluginAnimalProtect()
									.unprotectAnimal(
											pet.getEntity().getUniqueId()
													.toString());
							MbPets.getInstance()
									.getLogger()
									.info("Animal "
											+ pet.getEntity().getUniqueId()
													.toString()
											+ "/ "
											+ pet.getEntity().getType()
													.toString()
											+ " has been removed. The protection was deleted!");
						}
					}
					pet.getEntity().remove();

				}
				pet.confirm(player);
			} else {
				player.sendMessage(MbPetsUtils
						.getTextNode("error.petNotFinished"));
			}
		} else {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPreparedPets"));
		}
	}

	/**
	 * adds a convert-request that lasts for 10 seconds
	 * 
	 * @param player
	 */
	private void convertPet(Player player) {
		if (!player.hasPermission("MbPets.convert")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		if (MbPets.getInstance().getPluginAnimalProtect() == null) {
			return;
		}
		MbPets.getInstance().getConvertRightClickListener().addConvert(player);
	}

	/**
	 * removes the given pet from the db
	 * 
	 * @param argsMap
	 *            the chat input in a HashMap<MbPetUtils.Keys, String)
	 * @throws SQLException
	 */
	private void deletePet(final CommandSender sender,
			final HashMap<Keys, String> argsMap) throws SQLException {
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
					public void run() {
						PreparedStatement statement;
						OfflinePlayer targetPlayer;
						Connection con = DatabaseConnectionManager
								.getInstance().getConnection();
						if (!sender.hasPermission("MbPets.delete")) {
							sender.sendMessage(MbPetsUtils
									.getTextNode("error.noPermission"));
							return;
						}
						if (con == null) {
							sender.sendMessage(MbPetsUtils
									.getTextNode("error.noDbConnection"));
							return;
						}
						if (argsMap.get(MbPetsUtils.Keys.PLAYER) == null) {
							sender.sendMessage(MbPetsUtils.getTextNode(
									"error.missingValue").replace("{0}",
									"Player"));
							return;
						}
						String targetPlayerUUID = MbPets
								.getInstance()
								.getPluginUuidDb()
								.getUUIDByName(
										argsMap.get(MbPetsUtils.Keys.PLAYER));
						if (targetPlayerUUID == null) {
							sender.sendMessage(MbPetsUtils
									.getTextNode("error.playerUnknown"));
							return;
						} else {
							targetPlayer = MbPets
									.getInstance()
									.getServer()
									.getOfflinePlayer(
											UUID.fromString(targetPlayerUUID));
						}
						int number = (argsMap.get(MbPetsUtils.Keys.NUMBER) == null ? MbPetsUtils
								.getNumberOfPets(targetPlayer) : Integer
								.parseInt(argsMap.get(MbPetsUtils.Keys.NUMBER)));
						try {
							statement = con
									.prepareStatement("DELETE from MbPets_Pet WHERE playerid = (Select playerid from MbPets_Player where uuid=?) AND number = ?");
							statement.setString(1, targetPlayerUUID);
							statement.setInt(2, number);
							statement.executeUpdate();
							statement.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						sender.sendMessage(MbPetsUtils
								.getTextNode("info.petDeleted"));
					}
				});
	}

	/**
	 * uses the given args-array to put the values in a HashMap
	 * 
	 * @return
	 */
	private HashMap<MbPetsUtils.Keys, String> explodeChatInput(String[] args,
			CommandSender commandsender) {
		HashMap<MbPetsUtils.Keys, String> ret = new HashMap<MbPetsUtils.Keys, String>();
		if (args.length < 1) {
			commandsender.sendMessage(MbPetsUtils.getTextNode("help.Options"));
			return null;
		}
		if (MbPetsUtils.Functions.contains(args[0])) {
			ret.put(MbPetsUtils.Keys.COMMAND, args[0]);
		} else {
			commandsender.sendMessage(MbPetsUtils.getTextNode(
					"error.wrongFunction").replace("{0}", args[0]));
			return null;
		}
		for (int i = 0; i < args.length; i++) {
			if (NumberUtils.isNumber(args[i])) {
				ret.put(MbPetsUtils.Keys.NUMBER, args[i]);
				continue;
			}
			for (int j = 0; j < MbPetsUtils.Keys.values().length; j++)
				if (args[i].equalsIgnoreCase(MbPetsUtils.Keys.values()[j]
						.name())) {
					if (i + 1 < args.length) {
						ret.put(MbPetsUtils.Keys.values()[j], args[i + 1]);
					} else {
						commandsender.sendMessage(MbPetsUtils.getTextNode(
								"error.missingValue")
								.replace(
										"{0}",
										WordUtils.capitalize(MbPetsUtils.Keys
												.values()[j].name()
												.toLowerCase())));
						return null;
					}
				}
		}
		return ret;
	}

	/**
	 * deletes all pets from one player
	 * 
	 * @param sender
	 * @param argsMap
	 *            the chat input in a HashMap<MbPetUtils.Keys, String)
	 * @throws SQLException
	 */
	private void flushPets(CommandSender sender, HashMap<Keys, String> argsMap)
			throws SQLException {
		final Connection con = DatabaseConnectionManager.getInstance()
				.getConnection();
		if (!sender.hasPermission("MbPets.delete")) {
			sender.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		if (con == null) {
			sender.sendMessage(MbPetsUtils.getTextNode("error.noDbConnection"));
			return;
		}
		if (argsMap.get(MbPetsUtils.Keys.PLAYER) == null) {
			sender.sendMessage(MbPetsUtils.getTextNode("error.missingValue")
					.replace("{0}", "Player"));
			return;
		}
		final String targetPlayerUUID = MbPets.getInstance().getPluginUuidDb()
				.getUUIDByName(argsMap.get(MbPetsUtils.Keys.PLAYER));
		if (targetPlayerUUID == null) {
			sender.sendMessage(MbPetsUtils.getTextNode("error.playerUnknown"));
			return;
		}
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
					public void run() {
						PreparedStatement statement;
						try {
							statement = con
									.prepareStatement("DELETE from MbPets_Pet WHERE playerid = (Select playerid from MbPets_Player where uuid = ?)");
							statement.setString(1, targetPlayerUUID);
							statement.executeUpdate();
							statement.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
		sender.sendMessage(MbPetsUtils.getTextNode("info.petsFlushed"));
	}

	/**
	 * modifies a pet
	 * 
	 * @param argsMap
	 *            the chat input in a HashMap<MbPetUtils.Keys, String)
	 * @param player
	 *            or commandSender
	 * @throws SQLException
	 */
	private void modifyPet(final HashMap<Keys, String> argsMap,
			final Player player) throws SQLException {
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
					public void run() {
						if (MbPets.getInstance().getPluginVault() == null) {
							player.sendMessage(MbPetsUtils
									.getTextNode("error.noVault"));
							return;
						}
						Pet pet = MbPets.getInstance().getPreparedPet(player) != null ? MbPets
								.getInstance().getPreparedPet(player)
								: new Pet();
						final Connection con = DatabaseConnectionManager
								.getInstance().getConnection();
						if (!player.hasPermission("MbPets.modify")) {
							player.sendMessage(MbPetsUtils
									.getTextNode("error.noPermission"));
							return;
						}
						if (con == null) {
							player.sendMessage(MbPetsUtils
									.getTextNode("error.noDbConnection"));
							return;
						}
						int number;
						final ResultSet res;
						PreparedStatement preparedStatement;
						number = argsMap.get(MbPetsUtils.Keys.NUMBER) == null ? MbPetsUtils
								.getNumberOfPets(player) : Integer
								.parseInt(argsMap.get(MbPetsUtils.Keys.NUMBER));
						try {
							preparedStatement = con
									.prepareStatement("Select * from MbPets_Pet where playerid = (Select playerid from MbPets_Player where uuid = ?) and number = ?");
							preparedStatement.setString(1, player.getUniqueId()
									.toString());
							preparedStatement.setInt(2, number);
							res = preparedStatement.executeQuery();
							if (res.first()) {
								// reconstruct the pet
								pet.setType(MbPetsUtils.parseType(res
										.getString("type")));
								pet.setDroppedItemType(MbPetsUtils
										.parseItemStack(res.getString("type")));
								pet.setColor(MbPetsUtils.parseColor(res
										.getString("color")));
								pet.setHorseColor(MbPetsUtils
										.parseHorseColor(res
												.getString("horsecolor")));
								pet.setHorseStyle(MbPetsUtils
										.parseHorseStyle(res
												.getString("horsestyle")));
								pet.setOcelotStyle(MbPetsUtils
										.parseOcelotType(res
												.getString("ocelotstyle")));
								pet.setName(res.getString("customname") != null ? res
										.getString("customname") : null);
								pet.setBaby(res.getBoolean("baby"));
								pet.setOwner(player);
								pet.setPrice();
								pet.setModified(true);
							} else {
								player.sendMessage(MbPetsUtils.getTextNode(
										"error.noPetToCall").replace("{0}",
										"Du"));
								return;
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

						if (argsMap.get(MbPetsUtils.Keys.NAME) != null) {
							pet.setName(argsMap.get(MbPetsUtils.Keys.NAME));
						}
						if (argsMap.get(MbPetsUtils.Keys.COLOR) != null) {
							pet.setHorseColor(MbPetsUtils
									.parseHorseColor(argsMap
											.get(MbPetsUtils.Keys.COLOR)));
							pet.setColor(MbPetsUtils.parseColor(argsMap
									.get(MbPetsUtils.Keys.COLOR)));
						}
						if (argsMap.get(MbPetsUtils.Keys.STYLE) != null) {
							pet.setHorseStyle(MbPetsUtils
									.parseHorseStyle(argsMap
											.get(MbPetsUtils.Keys.STYLE)));

							pet.setOcelotStyle(MbPetsUtils
									.parseOcelotType(argsMap
											.get(MbPetsUtils.Keys.STYLE)));
						}
						if (argsMap.get(MbPetsUtils.Keys.BABY) != null) {
							pet.setBaby(argsMap.get(MbPetsUtils.Keys.BABY)
									.equalsIgnoreCase("true"));
						}
						pet.setNumber(number);
						MbPets.getInstance().addPreparedPet(pet);
						pet.printStatus(false);
						// player.sendMessage(MbPetsUtils.getTextNode("petReadyToModify"));

					}
				});
	}

	/**
	 * onCommand
	 */
	public boolean onCommand(CommandSender commandSender, Command cmd,
			String arg2, String[] args) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			HashMap<MbPetsUtils.Keys, String> argsMap = explodeChatInput(args,
					player);
			if (argsMap != null) {
				try {
					switch (MbPetsUtils.Functions.valueOf(argsMap.get(
							MbPetsUtils.Keys.COMMAND).toUpperCase())) {
					case BABY:
					case COLOR:
					case NAME:
					case NUMBER:
					case STYLE:
					case TYPE:
						setUpPet(argsMap, player);
						break;
					case CALL:
						callPet(argsMap, player);
						break;
					case CONFIRM:
						confirmPet(argsMap, player);
						break;
					case CANCEL:
						cancelPreparation(player);
						break;
					case LIST:
						showList(argsMap, player);
						break;
					case DELETE:
						deletePet(player, argsMap);
						break;
					case CONVERT:
						convertPet(player);
						break;
					case MODIFY:
						modifyPet(argsMap, player);
						break;
					case HELP:
						showHelp(argsMap, player);
						break;
					case STATUS:
						showStatus(player);
						break;
					case UNCALL:
						uncallPet(player);
						break;
					case FLUSH:
						flushPets(player, argsMap);
						break;
					case MONITOR:
						showMonitor(player);
						break;
					case INFO:
						showInfo(player, argsMap);
						break;
					case RELOAD:
						reloadConfig(player);
						break;
					case REGENERATE:
						regenerateConfig(player);
					default:
						break;
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			MbPets.getInstance().getLogger()
					.info("Command can only be run by a player!");
		}
		return false;
	}

	/**
	 * regenerates the plugins config. saves the old one before overwriting it
	 * @param player
	 */
	private void regenerateConfig(Player player) {
		if (!player.hasPermission("MbPets.regenerate")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		new File(MbPets.getInstance().getDataFolder().getAbsoluteFile()+"/configOld.yml").delete();
		File oldConfig = new File(MbPets.getInstance().getDataFolder().getAbsoluteFile()
				+ "/config.yml");
		oldConfig.renameTo(new File(MbPets.getInstance().getDataFolder().getAbsoluteFile()+"/configOld.yml"));
		String dbUser = MbPets.getInstance().getConfig().getString("mysql.dbuser");
		String dbPassword = MbPets.getInstance().getConfig().getString("mysql.dbpassword");
		String database = MbPets.getInstance().getConfig().getString("mysql.database");
		String url = MbPets.getInstance().getConfig().getString("mysql.url");
		new File(MbPets.getInstance().getDataFolder().getAbsoluteFile()
				+ "/config.yml").delete();
		MbPets.getInstance().getDataFolder().mkdir();
		MbPets.getInstance().getConfig().set("mysql.dbuser", dbUser);
		MbPets.getInstance().getConfig().set("mysql.dbpassword", dbPassword);
		MbPets.getInstance().getConfig().set("mysql.database", database);
		MbPets.getInstance().getConfig().set("mysql.url", url);
		MbPets.getInstance().saveConfig();
		MbPetsUtils.init();
		MbPets.getInstance().reloadConfig();
		player.sendMessage(MbPetsUtils.getTextNode("info.configReloaded"));
	}

	/**
	 * reloads the config file
	 * 
	 * @param player
	 */
	private void reloadConfig(Player player) {
		if (!player.hasPermission("MbPets.reload")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		MbPets.getInstance().reloadConfig();
	}

	/**
	 * prints a pet list for the given player s
	 * 
	 * @param offlinePlayer
	 * @throws SQLException
	 */
	private void printList(final CommandSender sender,
			final OfflinePlayer offlinePlayer) throws SQLException {
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
					public void run() {
						PreparedStatement statement;
						Connection con = DatabaseConnectionManager
								.getInstance().getConnection();
						if (!sender.hasPermission("MbPets.print")) {
							sender.sendMessage(MbPetsUtils
									.getTextNode("error.noPermission"));
							return;
						}
						try {
							statement = con
									.prepareStatement("Select * from MbPets_Pet left join MbPets_Player on MbPets_Pet.playerid = MbPets_Player.playerid "
											+ "where MbPets_Player.uuid = ? order by number asc;");
							statement.setString(1, offlinePlayer.getUniqueId()
									.toString());
							ResultSet res = statement.executeQuery();
							if (res.first()) {
								res.beforeFirst();
								sender.sendMessage(MbPetsUtils.getTextNode(
										"info.listHead").replace(
										"{0}",
										sender.getName().equals(
												offlinePlayer.getName()) ? "Du"
												: offlinePlayer.getName()));
								while (res.next()) {
									sender.sendMessage(MbPetsUtils
											.getTextNode("info.listElement")
											.replace(
													"{0}",
													Integer.toString(res
															.getInt("number")))
											.replace(
													"{1}",
													res.getString("customname")
															.isEmpty() ? "unbenannt"
															: res.getString("customname"))
											.replace(
													"{2}",
													WordUtils.capitalize(res
															.getString("type")
															.toLowerCase()
															.replace("_", " "))));
								}
							} else {
								sender.sendMessage(MbPetsUtils.getTextNode(
										"error.noPetToCall").replace(
										"{0}",
										sender.getName().equals(
												offlinePlayer.getName()) ? "Du"
												: offlinePlayer.getName()));
							}
						} catch (SQLException e) {

						}
					}
				});
	}

	/**
	 * creates a Pet object and fills it with the attributes entered by command
	 * 
	 * @param argsMap
	 *            the chat input in a HashMap<MbPetUtils.Keys, String)
	 * @throws SQLException
	 */
	private void setUpPet(HashMap<MbPetsUtils.Keys, String> argsMap,
			Player player) throws SQLException {
		Pet pet = MbPets.getInstance().getPreparedPet(player);
		if (!player.hasPermission("MbPets.buy")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		if (argsMap.get(MbPetsUtils.Keys.TYPE) != null) {
			if (MbPetsUtils.parseType(argsMap.get(MbPetsUtils.Keys.TYPE)) != null
					&& MbPetsUtils
							.parseType(argsMap.get(MbPetsUtils.Keys.TYPE)) != DisguiseType.DROPPED_ITEM) {
				pet = new Pet(MbPetsUtils.parseType(argsMap
						.get(MbPetsUtils.Keys.TYPE)));
			} else {
				if (MbPetsUtils.parseItemStack(argsMap
						.get(MbPetsUtils.Keys.TYPE)) != null) {
					pet = new Pet(MbPetsUtils.parseItemStack(argsMap
							.get(MbPetsUtils.Keys.TYPE)));
					pet.setType(DisguiseType.DROPPED_ITEM);
				} else {
					player.sendMessage(MbPetsUtils.getTextNode("help.TYPE"));
					return;
				}
			}
		}
		// a type must be entered first to ensure, that the correct color and
		// style attributes are set!
		if (pet == null) {
			player.sendMessage(MbPetsUtils.getTextNode("error.missingType"));
			player.sendMessage(MbPetsUtils.getTextNode("help.TYPE"));
			return;
		}

		// load the configurable attributes for the given type
		switch (pet.getType()) {
		case CHICKEN:
			if (argsMap.get(Keys.BABY) != null) pet.setBaby(Boolean.parseBoolean(argsMap.get(Keys.BABY)));
			break;
		case COW:
			if (argsMap.get(Keys.BABY) != null) pet.setBaby(Boolean.parseBoolean(argsMap.get(Keys.BABY)));
			break;
		case DROPPED_ITEM:
			if (argsMap.get(Keys.TYPE) != null) pet.setDroppedItemType(MbPetsUtils.parseItemStack(argsMap.get(Keys.TYPE)));
			break;
		case HORSE:
			if (argsMap.get(Keys.BABY) != null) pet.setBaby(Boolean.parseBoolean(argsMap.get(Keys.BABY)));
			if (argsMap.get(Keys.COLOR) != null) pet.setHorseColor(MbPetsUtils.parseHorseColor(argsMap.get(Keys.COLOR)));
			if (argsMap.get(Keys.STYLE) != null) pet.setHorseStyle(MbPetsUtils.parseHorseStyle(argsMap.get(Keys.STYLE)));
			break;
		case IRON_GOLEM:
			break;
		case MUSHROOM_COW:
			if (argsMap.get(Keys.BABY) != null) pet.setBaby(Boolean.parseBoolean(argsMap.get(Keys.BABY)));
			break;
		case OCELOT:
			if (argsMap.get(Keys.BABY) != null) pet.setBaby(Boolean.parseBoolean(argsMap.get(Keys.BABY)));
			if (argsMap.get(Keys.STYLE) != null) pet.setOcelotStyle(MbPetsUtils.parseOcelotType(argsMap.get(Keys.STYLE)));
			break;
		case PIG:
			if (argsMap.get(Keys.BABY) != null) pet.setBaby(Boolean.parseBoolean(argsMap.get(Keys.BABY)));
			break;
		case SHEEP:
			if (argsMap.get(Keys.BABY) != null) pet.setBaby(Boolean.parseBoolean(argsMap.get(Keys.BABY)));
			if (argsMap.get(Keys.COLOR) != null) pet.setColor(MbPetsUtils.parseColor(argsMap.get(Keys.COLOR)));
			break;
		case SKELETON_HORSE:
			if (argsMap.get(Keys.BABY) != null) pet.setBaby(Boolean.parseBoolean(argsMap.get(Keys.BABY)));
			break;
		case UNDEAD_HORSE:
			if (argsMap.get(Keys.BABY) != null) pet.setBaby(Boolean.parseBoolean(argsMap.get(Keys.BABY)));
			break;
		case WOLF:
			if (argsMap.get(Keys.BABY) != null) pet.setBaby(Boolean.parseBoolean(argsMap.get(Keys.BABY)));
			if (argsMap.get(Keys.COLOR) != null) pet.setColor(MbPetsUtils.parseColor(argsMap.get(Keys.COLOR)));
			break;
		default:	
		}
		pet.setOwner(player); 	
		if (argsMap.get(Keys.NAME) != null) pet.setName(argsMap.get(Keys.NAME) != null ? argsMap.get(Keys.NAME) : "");
		pet.setPrice();
		MbPets.getInstance().addPreparedPet(pet);
		pet.printStatus(false);
	}

	/**
	 * prints some help
	 * 
	 * @param argsMap
	 *            the chat input in a HashMap<MbPetUtils.Keys, String)
	 * @param player
	 *            or commandSender
	 */
	private void showHelp(HashMap<Keys, String> argsMap, Player player) {
		if (!player.hasPermission("MbPets.print")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		if (argsMap.get(Keys.TYPE) == null) {
			// user only entered /pet help
			player.sendMessage(MbPetsUtils.getTextNode("help.Options"));
			return;
		}
		player.sendMessage(MbPetsUtils.getTextNode("help.Head"));
		DisguiseType type = MbPetsUtils.parseType(argsMap.get(Keys.TYPE)) != null ? MbPetsUtils.parseType(argsMap.get(Keys.TYPE)) : DisguiseType.DROPPED_ITEM;
		switch (type) {
		case CHICKEN:
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Baby").replace("{1}", MbPetsUtils.getTextNode("help.BABY")));
			break;
		case COW:
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Baby").replace("{1}", MbPetsUtils.getTextNode("help.BABY")));
			break;
		case DROPPED_ITEM:
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Typ").replace("{1}", MbPetsUtils.getTextNode("help.DROPPEDITEMTYPE")));
			break;
		case HORSE:
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Baby").replace("{1}", MbPetsUtils.getTextNode("help.BABY")));
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Color").replace("{1}", MbPetsUtils.getTextNode("help.HORSECOLOR")));
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Style").replace("{1}", MbPetsUtils.getTextNode("help.HORSESTYLE")));
			break;
		case MUSHROOM_COW:
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Baby").replace("{1}", MbPetsUtils.getTextNode("help.BABY")));
			break;
		case OCELOT:
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Baby").replace("{1}", MbPetsUtils.getTextNode("help.BABY")));
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Style").replace("{1}", MbPetsUtils.getTextNode("help.OCELOTSTYLE")));
			break;
		case PIG:
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Baby").replace("{1}", MbPetsUtils.getTextNode("help.BABY")));
			break;
		case SHEEP:
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Baby").replace("{1}", MbPetsUtils.getTextNode("help.BABY")));
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Color").replace("{1}", MbPetsUtils.getTextNode("help.COLOR")));
			break;
		case SKELETON_HORSE:
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Baby").replace("{1}", MbPetsUtils.getTextNode("help.BABY")));
			break;
		case UNDEAD_HORSE:
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Baby").replace("{1}", MbPetsUtils.getTextNode("help.BABY")));
			break;
		case WOLF:
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Baby").replace("{1}", MbPetsUtils.getTextNode("help.BABY")));
			player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Color").replace("{1}", MbPetsUtils.getTextNode("help.COLOR")));
			break;
		default:
			player.sendMessage(ChatColor.DARK_GREEN+"keine");
			return;
		}
		player.sendMessage(MbPetsUtils.getTextNode("info.Element").replace("{0}", ChatColor.DARK_GREEN+"Name").replace("{1}", MbPetsUtils.getTextNode("help.NAME")));
	}

	/**
	 * prints info for the given pet
	 * 
	 * @param player
	 * @param argsMap
	 * @throws SQLException
	 */
	private void showInfo(final Player player,
			final HashMap<Keys, String> argsMap) throws SQLException {
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
					public void run() {
						PreparedStatement statement;
						Connection con = DatabaseConnectionManager
								.getInstance().getConnection();
						ResultSet res;
						Pet pet = new Pet();
						if (!player.hasPermission("MbPets.info")) {
							player.sendMessage(MbPetsUtils
									.getTextNode("error.noPermission"));
							return;
						}
						if (con == null) {
							player.sendMessage(MbPetsUtils
									.getTextNode("error.noDbConnection"));
							return;
						}
						if (argsMap.get(MbPetsUtils.Keys.NUMBER) == null) {
							player.sendMessage(MbPetsUtils.getTextNode(
									"error.missingValue").replace("{0}",
									"Number"));
							return;
						}
						try {
							statement = con
									.prepareStatement("Select * from MbPets_Pet where playerid = (Select playerid from MbPets_Player where uuid = ?) and number = ?");
							statement.setString(1, player.getUniqueId()
									.toString());
							statement.setInt(2, Integer.parseInt(argsMap
									.get(MbPetsUtils.Keys.NUMBER)));
							res = statement.executeQuery();
							if (res.first()) {
								// reconstruct the pet
								pet.setType(MbPetsUtils.parseType(res
										.getString("type")));
								pet.setDroppedItemType(MbPetsUtils
										.parseItemStack(res.getString("type")));
								pet.setColor(MbPetsUtils.parseColor(res
										.getString("color")));
								pet.setHorseColor(MbPetsUtils
										.parseHorseColor(res
												.getString("horsecolor")));
								pet.setHorseStyle(MbPetsUtils
										.parseHorseStyle(res
												.getString("horsestyle")));
								pet.setOcelotStyle(MbPetsUtils
										.parseOcelotType(res
												.getString("ocelotstyle")));
								pet.setName(res.getString("customname") != null ? res
										.getString("customname") : null);
								pet.setBaby(res.getBoolean("baby"));
								pet.setOwner(player);
								pet.printStatus(true);
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
	 * shows a list of all pets owned by the given player
	 * 
	 * @param argsMap
	 *            the chat input in a HashMap<MbPetUtils.Keys, String)
	 * @param player
	 *            or commandSender
	 * @throws SQLException
	 */
	private void showList(HashMap<Keys, String> argsMap, Player player)
			throws SQLException {
		Connection con = DatabaseConnectionManager.getInstance()
				.getConnection();
		if (!player.hasPermission("MbPets.print")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		if (con == null) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noDbConnection"));
			return;
		}
		if (argsMap.get(MbPetsUtils.Keys.PLAYER) != null) {
			String targetPlayerUUID = MbPets.getInstance().getPluginUuidDb()
					.getUUIDByName(argsMap.get(MbPetsUtils.Keys.PLAYER));
			if (targetPlayerUUID != null) {
				// player exists
				OfflinePlayer offlinePlayer = MbPets.getInstance().getServer()
						.getOfflinePlayer(UUID.fromString(targetPlayerUUID));
				printList(player, offlinePlayer);
			} else {
				player.sendMessage(MbPetsUtils
						.getTextNode("error.playerUnknown"));
			}
		} else {
			printList(player, player);
		}

	}

	/**
	 * prints some stuff about db connectivity, number of spawned pets, number
	 * of preparedPets
	 */
	private void showMonitor(CommandSender sender) {
		if (!sender.hasPermission("MbPets.monitor")) {
			sender.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		sender.sendMessage(MbPetsUtils
				.getTextNode("info.monitorDB")
				.replace(
						"{0}",
						MbPets.getInstance().isDbIsCurrentlyAvailable() ? ChatColor.GREEN
								+ "aktiv"
								: ChatColor.RED + "inaktiv"));
		sender.sendMessage(MbPetsUtils
				.getTextNode("info.monitorSpawnedPets")
				.replace("{0}",
						Integer.toString(MbPets.getInstance().getPets().size())));
		sender.sendMessage(MbPetsUtils.getTextNode("info.monitorPreparedPets")
				.replace(
						"{0}",
						Integer.toString(MbPets.getInstance().getPreparedPets()
								.size())));
		sender.sendMessage(MbPets.getInstance().getDescription().getVersion());
	}

	/**
	 * prints the status of the players current configuration
	 * 
	 * @param player
	 *            or commandSender
	 */
	private void showStatus(Player player) {
		Pet pet = MbPets.getInstance().getPreparedPet(player);
		if (!player.hasPermission("MbPets.print")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		if (pet != null) {
			pet.printStatus(false);
		} else {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPreparedPets"));
		}
	}

	/**
	 * despawns the pet and removes it from the list
	 * 
	 * @param player
	 *            or commandSender
	 */
	private void uncallPet(Player player) {
		Pet pet = MbPets.getInstance().getPet(player);
		if (!player.hasPermission("MbPets.call")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		if (pet != null) {
			pet.getEntity().remove();
			MbPets.getInstance().removePet(player);
			player.sendMessage(MbPetsUtils.getTextNode("info.petUncalled"));
		} else {
			player.sendMessage(MbPetsUtils.getTextNode("error.noActivePet"));
		}

	}

	/**
	 * tab complete for all usable values
	 */
	public List<String> onTabComplete(CommandSender sender, Command command,
			String alias, String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		if (args.length > 1) {
			Keys key = Keys.getKey(args[args.length - 2]);
			String arg = args[args.length - 1];
			if (key != null) {
				switch (key) {
				case BABY:
					list.add("true");
					list.add("false");
				case COLOR:
					for (String s : MbPets.getInstance().getConfig()
							.getConfigurationSection("animalColors")
							.getKeys(true)) {
						if (MbPetsUtils
								.parseConfigurationSectionForTabComplete(
										"animalColors." + s, arg) != null) {
							list.add(WordUtils.capitalize(MbPetsUtils
									.parseConfigurationSectionForTabComplete(
											"animalColors." + s, arg)
									.toLowerCase()));
						}
					}
					for (String s : MbPets.getInstance().getConfig()
							.getConfigurationSection("horseColors")
							.getKeys(true)) {
						if (MbPetsUtils
								.parseConfigurationSectionForTabComplete(
										"horseColors." + s, arg) != null) {
							list.add(WordUtils.capitalize(MbPetsUtils
									.parseConfigurationSectionForTabComplete(
											"horseColors." + s, arg)
									.toLowerCase()));
						}
					}
					break;
				case NUMBER:
					list.add(Integer.toString(MbPetsUtils
							.getNumberOfPets((Player) sender)));
					break;
				case NAME: 
					for (String s : MbPets.getInstance().getConfig()
							.getConfigurationSection("chatColors")
							.getKeys(true)) {
						if (MbPetsUtils
								.parseConfigurationSectionForTabComplete(
										"chatColors." + s, arg) != null) {
							list.add(WordUtils.capitalize(MbPetsUtils
									.parseConfigurationSectionForTabComplete(
											"chatColors." + s, arg)
									.toLowerCase()));
						}
					}
					break;
				case STYLE:
					for (String s : MbPets.getInstance().getConfig()
							.getConfigurationSection("horseStyles")
							.getKeys(true)) {
						if (MbPetsUtils
								.parseConfigurationSectionForTabComplete(
										"horseStyles." + s, arg) != null) {
							list.add(WordUtils.capitalize(MbPetsUtils
									.parseConfigurationSectionForTabComplete(
											"horseStyles." + s, arg)
									.toLowerCase()));
						}
					}
					for (String s : MbPets.getInstance().getConfig()
							.getConfigurationSection("ocelotStyles")
							.getKeys(true)) {
						if (MbPetsUtils
								.parseConfigurationSectionForTabComplete(
										"ocelotStyles." + s, arg) != null) {
							list.add(WordUtils.capitalize(MbPetsUtils
									.parseConfigurationSectionForTabComplete(
											"ocelotStyles." + s, arg)
									.toLowerCase()));
						}
					}
					break;
				case TYPE:
					for (String s : MbPets.getInstance().getConfig()
							.getConfigurationSection("disguiseTypes")
							.getKeys(true)) {
						if (MbPetsUtils
								.parseConfigurationSectionForTabComplete(
										"disguiseTypes." + s, arg) != null) {
							list.add(WordUtils.capitalize(MbPetsUtils
									.parseConfigurationSectionForTabComplete(
											"disguiseTypes." + s, arg)
									.toLowerCase()));
						}
					}
					for (String s : MbPets.getInstance().getConfig()
							.getConfigurationSection("droppedItems")
							.getKeys(true)) {
						if (MbPetsUtils
								.parseConfigurationSectionForTabComplete(
										"droppedItems." + s, arg) != null) {
							list.add(WordUtils.capitalize(MbPetsUtils
									.parseConfigurationSectionForTabComplete(
											"droppedItems." + s, arg)
									.toLowerCase()));
						}
					}
					break;
				default:
					break;
				}

			}
		}
		//remove duplicates & sort
		List<String> ret = new ArrayList<String>();
		ret.addAll(new HashSet<String>(list));
		Collections.sort(ret);
		return ret;
	}
}
