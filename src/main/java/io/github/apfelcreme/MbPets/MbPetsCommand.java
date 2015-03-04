package io.github.apfelcreme.MbPets;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.UUID;

import io.github.apfelcreme.MbPets.ChatInput.Operation;
import io.github.apfelcreme.MbPets.Interfaces.Ageable;
import io.github.apfelcreme.MbPets.Interfaces.DroppedItem;
import io.github.apfelcreme.MbPets.Interfaces.FallingBlock;
import io.github.apfelcreme.MbPets.Interfaces.Sizeable;
import io.github.apfelcreme.MbPets.Pets.AngelPet;
import io.github.apfelcreme.MbPets.Pets.ChickenPet;
import io.github.apfelcreme.MbPets.Pets.CowPet;
import io.github.apfelcreme.MbPets.Pets.DroppedItemPet;
import io.github.apfelcreme.MbPets.Pets.DevilPet;
import io.github.apfelcreme.MbPets.Pets.FallingBlockPet;
import io.github.apfelcreme.MbPets.Pets.HorsePet;	
import io.github.apfelcreme.MbPets.Pets.IronGolemPet;
import io.github.apfelcreme.MbPets.Pets.MagmaCubePet;
import io.github.apfelcreme.MbPets.Pets.MooshroomPet;
import io.github.apfelcreme.MbPets.Pets.OcelotPet;
import io.github.apfelcreme.MbPets.Pets.Pet;
import io.github.apfelcreme.MbPets.Pets.PigPet;
import io.github.apfelcreme.MbPets.Pets.RabbitPet;
import io.github.apfelcreme.MbPets.Pets.SheepPet;
import io.github.apfelcreme.MbPets.Pets.SkeletonHorsePet;
import io.github.apfelcreme.MbPets.Pets.SlimePet;
import io.github.apfelcreme.MbPets.Pets.UndeadHorsePet;
import io.github.apfelcreme.MbPets.Pets.WolfPet;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import net.milkbowl.vault.economy.EconomyResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MbPetsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command cmd,
			String lbl, String[] args) {
		if (commandSender instanceof Player) {
			if (MbPets.getInstance().getConfig().getBoolean("enabled")) {
				Player sender = (Player) commandSender;
				if (cmd.getName().equalsIgnoreCase("pet")) {
					ChatInput chatInput = explodeChatInput(args, sender);
					if (chatInput != null) {
						switch (chatInput.getOperation()) {
						case BABY:
						case COLOR:
						case STYLE:
						case SIZE:
						case TYPE:
						case NAME:
							createPet(chatInput);
							break;
						case CALL:
							callPet(chatInput);
							break;
						case CANCEL:
							cancelPreparation(sender);
							break;
						case CONFIRM:
							confirmPet(chatInput);
							break;
						case CONVERT:
							registerConvert(sender);
							break;
						case DELETE:
							deletePet(chatInput);
							break;
						case DESPAWN:
							despawnPet(chatInput);
							break;
						case FLUSH:
							flushPets(chatInput);
							break;
						case HELP:
							printHelp(chatInput);
							break;
						case INFO:
							printInfo(chatInput);
							break;
						case LIST:
							printList(chatInput);
							break;
						case MODIFY:
							modifyPet(chatInput);
							break;
						case MONITOR:
							printMonitor(sender);
							break;
						case REGENERATE:
							regenerateConfig(sender);
							break;
						case RELOAD:
							reloadConfig(sender);
							break;
						case SELL:
							sellPet(chatInput);
							break;
						case STATUS:
							printStatus(sender);
							break;
						case UNCALL:
							uncallPet(sender);
							break;
						default:
							break;
						}
					}
				}
			} else {
				commandSender.sendMessage(ChatColor.RED+"Das Plugin wurde bis zum nächsten Serverneustart deaktiviert! Das heißt auch, dass es bald etwas neues gibt ;)");
			}
		}
		return false;
	}

	/**
	 * adds a convert-request that lasts for 10 seconds
	 * 
	 * @param player
	 */
	private void registerConvert(Player sender) {

		if (!sender.hasPermission("MbPets.convert")) {
			sender.sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		if (MbPets.getInstance().getPluginAnimalProtect() == null) {
			return;
		}
		MbPets.getInstance().getConvertRightclickListener().addConvert(sender);

	}

	/**
	 * calls a pet
	 * 
	 * @param chatInput
	 * @return
	 */
	private void callPet(final ChatInput chatInput) {
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {

					@Override
					public void run() {
						Connection connection = DatabaseConnectionManager.getInstance()
								.getConnection();
						if (!chatInput.getSender().getPlayer().hasPermission("MbPets.call")) {
							chatInput.getSender().getPlayer()
									.sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
							return;
						}
						if (connection == null) {
							chatInput.getSender().getPlayer()
									.sendMessage(MbPetsConfig.getTextNode("error.noDbConnection"));
							return;
						}
						try {
							Integer number;
							if (chatInput.getNumber() == null) {
								number = MbPets.getLatestPetNumber(chatInput
										.getSender());
							} else {
								number = chatInput.getNumber();
							}
							Pet pet = MbPets.getInstance().getPet(
									chatInput.getSender(), number);
							if (pet != null) {
								pet.spawn();
							}
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
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
			player.sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		if (MbPets.getInstance().getConfigurations().containsKey(player)) {
			MbPets.getInstance().getConfigurations().remove(player);
			player.sendMessage(MbPetsConfig.getTextNode("info.preparedPetCanceled"));
		} else {
			player.sendMessage(MbPetsConfig.getTextNode("error.noPreparedPets"));
		}

	}

	/**
	 * confirms a configured pet
	 * 
	 * @param chatInput
	 */
	private void confirmPet(ChatInput chatInput) {
		Connection connection = DatabaseConnectionManager.getInstance()
				.getConnection();
		if (!chatInput.getSender().hasPermission("MbPets.confirm")) {
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		if (connection == null) {
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("error.noDbConnection"));
			return;
		}
		if (MbPets.getInstance().getPluginVault() == null) {
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("error.noVault"));
			return;
		}
		if (!MbPets.getInstance().getEconomy().hasAccount(chatInput.getSender())) {
			return;
		}
//		Pet currentlySpawnedPet = MbPets.getInstance().getPets().get(chatInput.getSender());
//		if (currentlySpawnedPet != null) {
//			currentlySpawnedPet.uncall();
//		}
		Pet pet = MbPets.getInstance().getConfigurations()
				.get(chatInput.getSender());
		if (pet != null) {
			if (pet.isConfigurationFinished()) {
				if (MbPets.getInstance().getEconomy()
						.getBalance(pet.getOwner()) >= pet.getPrice()) {

					try {
						Pet oldPet = MbPets.getInstance().getPet(
								chatInput.getSender(), pet.getNumber());
						// delete the old pet that has the same number
						if (oldPet != null) {
							pet.setNumber(oldPet.getNumber());
							oldPet.delete();
						}
						// if the to-spawn entity is a requested convert, the
						// "old"
						// entity is stored in pet.entity.
						// it gets despawned now
						if (pet.getEntity() != null) {
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
													pet.getEntity()
															.getUniqueId()
															.toString());
									MbPets.getInstance()
											.getLogger()
											.info("Animal "
													+ pet.getEntity()
															.getUniqueId()
															.toString()
													+ "/ "
													+ pet.getEntity().getType()
															.toString()
													+ " has been removed. The protection was deleted!");
								}
							}
							pet.getEntity().remove();
						}

						// spawn and enter into the db
						pet.spawn();
						EconomyResponse response = MbPets.getInstance().getEconomy()
							.withdrawPlayer(pet.getOwner(), pet.getPrice());
						if (response.transactionSuccess()) {
							pet.confirm();
							MbPets.getInstance().getConfigurations()
									.remove(chatInput.getSender());
							chatInput.getSender().sendMessage(
									MbPetsConfig
											.getTextNode("info.petConfirmed"));
						}						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					chatInput.getSender().sendMessage(
							MbPetsConfig.getTextNode("error.notThatMuchMoney"));
				}
			} else {
				chatInput.getSender().sendMessage(
						MbPetsConfig.getTextNode("error.petNotFinished"));
			}
		}
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}

	/**
	 * initializes a pet or loads a pet that is in configuration
	 * 
	 * @param chatInput
	 */
	private void createPet(ChatInput chatInput) {
		if (!chatInput.getSender().getPlayer().hasPermission("MbPets.buy")) {
			chatInput.getSender().getPlayer()
					.sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		Pet pet = null;
		Pet oldPet = null;
		Player owner = chatInput.getSender();
		DisguiseType type = MbPetsConfig.parseType(chatInput.getType());
		String name = "";
		String color = "";
		String style = "";
		Integer size = MbPetsConfig.parseSlimeSize(chatInput.getSize());
		Material material = MbPetsConfig.parseMaterial(chatInput.getType());
		Material block = MbPetsConfig.parseBlock(chatInput.getType());
		Boolean isBaby = false;
		Integer number = MbPets.getLatestPetNumber(owner) + 1;
		if (MbPets.getInstance().getConfigurations().get(chatInput.getSender()) != null) {
			// get the attributes the current pet object has stored
			oldPet = MbPets.getInstance().getConfigurations()
					.get(chatInput.getSender());
			type = oldPet.getType();
			number = oldPet.getNumber();
			if (oldPet instanceof HorsePet) {
				color = ((HorsePet) oldPet).getColor() != null ? ((HorsePet) oldPet)
						.getColor().name() : null;
				style = ((HorsePet) oldPet).getStyle() != null ? ((HorsePet) oldPet)
						.getStyle().name() : null;
			} else if (oldPet instanceof SheepPet
					&& ((SheepPet) oldPet).getColor() != null) {
				color = ((SheepPet) oldPet).getColor().name();
			} else if (oldPet instanceof WolfPet
					&& ((WolfPet) oldPet).getColor() != null) {
				color = ((WolfPet) oldPet).getColor().name();
			} else if (oldPet instanceof OcelotPet
					&& ((OcelotPet) oldPet).getStyle() != null) {
				style = ((OcelotPet) oldPet).getStyle().name();
			} else if (oldPet instanceof RabbitPet
					&& ((RabbitPet) oldPet).getStyle() != null) {
				style = ((RabbitPet) oldPet).getStyle().name();
			} else if (oldPet instanceof Sizeable 
					&& ((Sizeable) oldPet).getSize() != null) {
				size = ((Sizeable) oldPet).getSize();
			}
			if (oldPet instanceof Ageable && ((Ageable) oldPet).isBaby() != null) {
				isBaby = ((Ageable) oldPet).isBaby();
			}
			if (oldPet instanceof DroppedItem) {
				material = ((DroppedItem)oldPet).getMaterial();
				type = DisguiseType.DROPPED_ITEM;
			}
			if (oldPet instanceof FallingBlock) {
				block = ((FallingBlock)oldPet).getBlock();
				type = DisguiseType.FALLING_BLOCK;
			}
			name = oldPet.getName();
		}
		name = chatInput.getName() != null ? chatInput.getName() : name;
		color = chatInput.getColor() != null ? chatInput.getColor() : color;
		style = chatInput.getStyle() != null ? chatInput.getStyle() : style;
		isBaby = Boolean.parseBoolean(chatInput.getBaby() != null ? chatInput
				.getBaby() : isBaby.toString());
		number = chatInput.getNumber() != null ? chatInput.getNumber() : number;
		size = chatInput.getSize() != null ? MbPetsConfig.parseSlimeSize(chatInput.getSize()) : size;
		// a type must be entered first to ensure, that the correct color and
		// style attributes are set!
		if (type == null) {
			if (MbPetsConfig.parseMaterial(chatInput.getType()) != null) {
				type = DisguiseType.DROPPED_ITEM;
			} else if (MbPetsConfig.parseBlock(chatInput.getType()) != null) {
				type = DisguiseType.FALLING_BLOCK;
			} else {
				chatInput.getSender().sendMessage(
						MbPetsConfig.getTextNode("error.missingType"));
				chatInput.getSender().sendMessage(
						MbPetsConfig.getTextNode("info.types") + ChatColor.GREEN
								+ StringUtils.join(MbPetsConfig.getAvailableTypes(),", ") + ChatColor.GREEN + ", " 
								+ StringUtils.join(MbPetsConfig.getAvailableDroppedItems(),", ") + ChatColor.GREEN + ", " 
								+ StringUtils.join(MbPetsConfig.getAvailableFallingBlocks(),", "));
				return;
			}
		}

		switch (type) {
		case CHICKEN:
			pet = new ChickenPet(owner, name, number, isBaby);
			break;
		case COW:
			pet = new CowPet(owner, name, number, isBaby);
			break;
		case DROPPED_ITEM:
			if (material != null) {
				switch (material) {
				case OBSIDIAN:
					pet = new DevilPet(owner, name, number);
					break;
				case SNOW_BLOCK:
					pet = new AngelPet(owner, name, number);
					break;
				default:
					pet = new DroppedItemPet(owner, name, number, material);
					break;
				}
			} else {
				chatInput.getSender().sendMessage(
						MbPetsConfig.getTextNode("info.types")
								+ ChatColor.GREEN
								+ MbPetsConfig.getAvailableDroppedItems());
			}
			break;
		case ENDERMAN: 
			//endermen sind buggy <.<
			//pet = new EndermanPet(owner, name, number);
			chatInput.getSender().sendMessage(ChatColor.RED+"Endermen sind derzeit noch nicht verfügbar! Warte ab ;)");
			return;
		case FALLING_BLOCK: 
			if (block != null) {
				pet = new FallingBlockPet(owner, name, number, block);
			} else {
				chatInput.getSender().sendMessage(
						MbPetsConfig.getTextNode("info.types")
								+ ChatColor.GREEN
								+ MbPetsConfig.getAvailableFallingBlocks());
			}
			break;
		case HORSE:
			pet = new HorsePet(owner, name, number, isBaby,
					MbPetsConfig.parseHorseColor(color),
					MbPetsConfig.parseHorseStyle(style));
			break;
		case IRON_GOLEM:
			pet = new IronGolemPet(owner, name, number);
			break;
		case MAGMA_CUBE:
			pet = new MagmaCubePet(owner, name, number, size);
			break;
		case MUSHROOM_COW:
			pet = new MooshroomPet(owner, name, number, isBaby);
			break;
		case OCELOT:
			pet = new OcelotPet(owner, name, number, isBaby,
					MbPetsConfig.parseOcelotType(style));
			break;
		case PIG:
			pet = new PigPet(owner, name, number, isBaby);
			break;
		case RABBIT:
			pet = new RabbitPet(owner, name, number, isBaby,
					MbPetsConfig.parseRabbitType(style));
			break;
		case SHEEP:
			pet = new SheepPet(owner, name, number, isBaby,
					MbPetsConfig.parseColor(color));
			break;
		case SLIME:
			pet = new SlimePet(owner, name, number, size);
			break;
		case SKELETON_HORSE:
			pet = new SkeletonHorsePet(owner, name, number, isBaby);
			break;
		case UNDEAD_HORSE:
			pet = new UndeadHorsePet(owner, name, number, isBaby);
			break;
		case WOLF:
			pet = new WolfPet(owner, name, number, isBaby,
					MbPetsConfig.parseColor(color));
			break;
		default:	
			break;
		}
		if (oldPet != null) {
			// for uncommon prices e.g. modification price is 1000 benches
			pet.setPrice(oldPet.getPrice());
			if (oldPet.getEntity() != null) {
				//for converted pets as they store the old entity in this field
				pet.setEntity(oldPet.getEntity());
			}
		} 	
		MbPets.getInstance().getConfigurations()
				.put(chatInput.getSender(), pet);
		owner.sendMessage(pet.toString());

	}

	/**
	 * explodes the users chat input and stores it into a {@link ChatInput}
	 * object
	 * 
	 * @param args
	 * @param sender
	 * @return
	 */
	private ChatInput explodeChatInput(String[] args, Player sender) {
		if (args.length == 0) {
			sender.sendMessage(MbPetsConfig.getTextNode("help.Options"));
			sender.sendMessage(
					MbPetsConfig.getTextNode("info.types") + ChatColor.GREEN
							+ StringUtils.join(MbPetsConfig.getAvailableTypes(),", ") + ChatColor.GREEN + ", " 
							+ StringUtils.join(MbPetsConfig.getAvailableDroppedItems(),", ") + ChatColor.GREEN + ", " 
							+ StringUtils.join(MbPetsConfig.getAvailableFallingBlocks(),", "));
			return null;
		} else {
			ChatInput chatInput = new ChatInput();
			chatInput.setSender(sender);
			Operation mainOperation = Operation.getOperation(args[0]);
			if (mainOperation != null) {
				chatInput.setOperation(mainOperation);
			} else {
				sender.sendMessage(MbPetsConfig.getTextNode("error.wrongFunction")
						.replace("{0}", args[0]));
				return null;
			}
			for (int i = 0; i < args.length; i++) {
				Operation operation = Operation.getOperation(args[i]);
				if (NumberUtils.isNumber(args[i])) {
					chatInput.setNumber(Integer.parseInt(args[i]));
					continue;
				}
				if (MbPets.getInstance().getPluginUuidDb() != null) {
					if (MbPets.getInstance().getPluginUuidDb()
							.getUUIDByName(args[i]) != null) {
						chatInput.setTargetPlayer(MbPets
								.getInstance()
								.getServer()
								.getOfflinePlayer(
										UUID.fromString(MbPets.getInstance()
												.getPluginUuidDb()
												.getUUIDByName(args[i]))));
					}
				}
				if (operation != null) {
					if (i + 1 < args.length || !Operation.needsValue(operation)) {
						switch (operation) {
						case BABY:
							chatInput.setBaby(args[i + 1]);
							break;
						case COLOR:
							chatInput.setColor(args[i + 1]);
							break;
						case NAME:
							chatInput.setName(args[i + 1]);
							break;
						case SIZE:
							chatInput.setSize(args[i + 1]);
							break;
						case STYLE:
							chatInput.setStyle(args[i + 1]);
							break;
						case TYPE:
							chatInput.setType(args[i + 1]);
							break;
						default:
							break;
						}
					} else {
						sender.sendMessage(MbPetsConfig.getTextNode(
								"error.missingValue").replace(
								"{0}",
								WordUtils.capitalize(operation.name()
										.toLowerCase())));
						return null;
					}
				}
			}
			return chatInput;
			
		}
	}

	/**
	 * removes the given pet from the db
	 * 
	 * @param chatInput
	 * @throws SQLException
	 */
	private void deletePet(final ChatInput chatInput) {
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {

					@Override
					public void run() {						
						Connection connection = DatabaseConnectionManager.getInstance()
								.getConnection();
						if (!chatInput.getSender().hasPermission("MbPets.delete")) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.noPermission"));
							return;
						}
						if (connection == null) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.noDbConnection"));
							return;
						}
						if (chatInput.getTargetPlayer() == null) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.missingValue").replace("{0}",
											"Player"));
							return;
						}

						int number = (chatInput.getNumber() == null ? MbPets
								.getLatestPetNumber(chatInput.getTargetPlayer())
								: chatInput.getNumber());
						try {
							Pet pet = MbPets.getInstance().getPet(chatInput.getTargetPlayer(), number);
							if (pet != null) {
								pet.delete();
								chatInput.getSender().sendMessage(
										MbPetsConfig.getTextNode("info.petDeleted"));
							}
							if (MbPets.getInstance().getPets().get(chatInput.getSender()) != null) {
								MbPets.getInstance().getPets().get(chatInput.getSender()).uncall();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * despawns all called pets
	 * @param sender
	 */
	private void despawnPet(ChatInput chatInput) {
		if (!chatInput.getSender().hasPermission("MbPets.despawn")) {
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		int i = 0;
		if (chatInput.getNumber() == null) {
			for (Entry<Player, Pet> entry : MbPets.getInstance().getPets().entrySet()) {
				if (entry.getValue() != null) {
					entry.getValue().uncall();
					MbPets.getInstance().getPets().remove(entry.getKey());
					i++;
				}
			}
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.despawnCount").replace("{0}", Integer.toString(i)));
		} else {
			for (Entry<Player, Pet> entry : MbPets.getInstance().getPets().entrySet()) {
				if (entry.getValue() != null) {
					if (entry.getValue().getEntity().getLocation().distance(chatInput.getSender().getLocation()) <= chatInput.getNumber()) {
						entry.getValue().uncall();
						MbPets.getInstance().getPets().remove(entry.getKey());
						i++;
					}
				}
			}
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.despawnCountRange").replace("{0}", Integer.toString(i)).replace("{1}", chatInput.getNumber().toString()));
		}
	}
	
	/**
	 * deletes all pets from one player
	 * 
	 * @param chatInput
	 */
	private void flushPets(final ChatInput chatInput) {
		
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
					public void run() {
						Connection connection = DatabaseConnectionManager.getInstance()
							.getConnection();
						if (!chatInput.getSender().hasPermission("MbPets.delete")) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.noPermission"));
							return;
						}
						if (connection == null) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.noDbConnection"));
							return;
						}
						if (chatInput.getTargetPlayer() == null) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.missingValue").replace("{0}",
											"Player"));
							return;
						}
						PreparedStatement statement;
						try {
							statement = connection
									.prepareStatement("DELETE from MbPets_Pet WHERE playerid = (Select playerid from MbPets_Player where uuid = ?)");
							statement.setString(1, chatInput.getTargetPlayer()
									.getUniqueId().toString());
							statement.executeUpdate();
							statement.close();
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						chatInput.getSender().sendMessage(
								MbPetsConfig.getTextNode("info.petsFlushed"));
					}

				});
	}

	/**
	 * loads a pet from the database and stores it into a {@link Pet} object for
	 * a later modification
	 * 
	 * @param chatInput
	 */
	public void modifyPet(final ChatInput chatInput) {
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
					public void run() {
						Connection connection = DatabaseConnectionManager.getInstance()
								.getConnection();
						if (MbPets.getInstance().getPluginVault() == null) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.noVault"));
							return;
						}
						if (!chatInput.getSender().hasPermission("MbPets.modify")) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.noPermission"));
							return;
						}
						if (connection == null) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.noDbConnection"));
							return;
						}
						int number = chatInput.getNumber() != null ? chatInput
								.getNumber() : MbPets
								.getLatestPetNumber(chatInput.getSender());
						try {
							Pet pet = MbPets.getInstance().getPet(
									chatInput.getSender(), number);
							MbPets.getInstance().getConfigurations()
									.put(chatInput.getSender(), pet);
							if (!(pet instanceof DroppedItem)) {
								pet.setPrice(MbPets.getInstance().getConfig()
										.getDouble("prices.MODIFY", 1000));
							} else {
								//items names arent currently displayed so it would be unfair to charge the owners for changing a name they don't see
								pet.setPrice(0.0);
							}
							chatInput.getSender().sendMessage(pet.toString());
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * prints some help
	 * 
	 * @param chatInput
	 */
	private void printHelp(ChatInput chatInput) {
		if (!chatInput.getSender().hasPermission("MbPets.print")) {
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		if (chatInput.getType() == null) {
			// user only entered /pet help
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("help.Options"));
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.types") + ChatColor.GREEN
							+ StringUtils.join(MbPetsConfig.getAvailableTypes(),", ") + ChatColor.GREEN + ", " 
							+ StringUtils.join(MbPetsConfig.getAvailableDroppedItems(),", ") + ChatColor.GREEN + ", " 
							+ StringUtils.join(MbPetsConfig.getAvailableFallingBlocks(),", "));
			return;
		}
		chatInput.getSender().sendMessage(MbPetsConfig.getTextNode("help.Head"));
		DisguiseType type = MbPetsConfig.parseType(chatInput.getType()) != null ? MbPetsConfig
				.parseType(chatInput.getType()) : DisguiseType.DROPPED_ITEM;
		switch (type) {
		case CHICKEN:
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Baby")
							.replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
			break;
		case COW:
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Baby")
							.replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
			break;
		case DROPPED_ITEM:
			chatInput
					.getSender()
					.sendMessage(
							MbPetsConfig.getTextNode("info.Element")
									.replace("{0}",
											ChatColor.DARK_GREEN + "Typ")
									.replace(
											"{1}",
											StringUtils.join(MbPetsConfig.getAvailableDroppedItems(),", ")));
			break;
		case HORSE:
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Baby")
							.replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Color")
							.replace("{1}",
									StringUtils.join(MbPetsConfig.getAvailableHorseColors(),", ")));
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Style")
							.replace("{1}",
									StringUtils.join(MbPetsConfig.getAvailableHorseStyles(),", ")));
			break;
		case MUSHROOM_COW:
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Baby")
							.replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
			break;
		case OCELOT:
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Baby")
							.replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Style")
							.replace("{1}",
									StringUtils.join(MbPetsConfig.getAvailableOcelotStyles(),", ")));
			break;
		case PIG:
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Baby")
							.replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
			break;
		case RABBIT:
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Baby")
							.replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Style")
							.replace("{1}",
									StringUtils.join(MbPetsConfig.getAvailableRabbitStyles(),", ")));
			break;
		case SHEEP:
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Baby")
							.replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Color")
							.replace("{1}", StringUtils.join(MbPetsConfig.getAvailableColors(),", ")));
			break;
		case SKELETON_HORSE:
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Baby")
							.replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
			break;
		case UNDEAD_HORSE:
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Baby")
							.replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
			break;
		case WOLF:
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Baby")
							.replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("info.Element")
							.replace("{0}", ChatColor.DARK_GREEN + "Color")
							.replace("{1}", StringUtils.join(MbPetsConfig.getAvailableColors(),", ")));
			break;
		default:
			chatInput.getSender().sendMessage(ChatColor.DARK_GREEN + "keine");
			return;
		}
		chatInput.getSender().sendMessage(
				MbPetsConfig.getTextNode("info.Element")
						.replace("{0}", ChatColor.DARK_GREEN + "Name")
						.replace("{1}", MbPetsConfig.getTextNode("help.NAME")));
	}

	/**
	 * prints a pet list
	 * 
	 * @param chatInput
	 */
	private void printList(final ChatInput chatInput) {
		
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {

					@Override
					public void run() {
						Connection connection = DatabaseConnectionManager.getInstance()
								.getConnection();
						if (!chatInput.getSender()
								.hasPermission("MbPets.print")) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.noPermission"));
							return;
						}
						if (connection == null) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.noDbConnection"));
							return;
						}
						if (chatInput.getTargetPlayer() == null) {
							// a user wants to see his own list
							chatInput.setTargetPlayer(chatInput.getSender());
						} 
						chatInput.getSender().sendMessage(
								MbPetsConfig.getTextNode("info.listHead").replace(
										"{0}",
										chatInput
												.getSender()
												.getName()
												.equals(chatInput
														.getTargetPlayer()
														.getName()) ? "Du"
												: chatInput.getTargetPlayer()
														.getName()));
						for (int i = 1; i <= MbPets
								.getLatestPetNumber(chatInput.getTargetPlayer()); i++) {
							try {
								Pet pet = MbPets.getInstance()
										.getPet(chatInput.getTargetPlayer(), i);
								if (pet != null)
									chatInput.getSender().sendMessage(
											pet.getListDescription());
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}

						}
					}
				});
	}
	
	/**
	 * prints the info of the given pet
	 * 
	 * @param chatInput
	 */
	private void printInfo(final ChatInput chatInput) {
		MbPets.getInstance().getServer().getScheduler()
				.runTaskAsynchronously(MbPets.getInstance(), new Runnable() {

					@Override
					public void run() {
						Connection connection = DatabaseConnectionManager.getInstance()
								.getConnection();
						if (!chatInput.getSender()
								.hasPermission("MbPets.print")) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.noPermission"));
							return;
						}
						if (connection == null) {
							chatInput.getSender().sendMessage(
									MbPetsConfig.getTextNode("error.noDbConnection"));
							return;
						}
						try {
							Integer number = chatInput.getNumber() != null ? chatInput
									.getNumber() : MbPets
									.getLatestPetNumber(chatInput.getSender());
							Pet pet = MbPets.getInstance().getPet(
									chatInput.getSender(), number);
							if (pet != null) {
								chatInput.getSender().sendMessage(
										pet.toShortString());
							} else {
								chatInput.getSender().sendMessage(
										MbPetsConfig.getTextNode("error.noPetToCall")
												.replace("{0}", "Du"));
							}
							connection.close();
						} catch (SQLException e) {

							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * prints some stuff about db connectivity, number of spawned pets, number
	 * of preparedPets & plugin version
	 */
	private void printMonitor(Player sender) {
		if (!sender.hasPermission("MbPets.monitor")) {
			sender.sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		sender.sendMessage(MbPetsConfig
				.getTextNode("info.monitorDB")
				.replace(
						"{0}",
						DatabaseConnectionManager.getInstance().getConnection() != null ? ChatColor.GREEN
								+ "aktiv"
								: ChatColor.RED + "inaktiv"));
		sender.sendMessage(MbPetsConfig
				.getTextNode("info.monitorSpawnedPets")
				.replace("{0}",
						Integer.toString(MbPets.getInstance().getPets().size())));
		sender.sendMessage(MbPetsConfig.getTextNode("info.monitorPreparedPets")
				.replace(
						"{0}",
						Integer.toString(MbPets.getInstance().getConfigurations()
								.size())));
		sender.sendMessage(MbPetsConfig.getTextNode("info.monitorVersion")
				.replace(
						"{0}",MbPets.getInstance().getDescription().getVersion()));
	}

	/**
	 * prints the players current configured pet
	 * 
	 * @param sender
	 */
	private void printStatus(Player sender) {
		if (!sender.hasPermission("MbPets.print")) {
			sender.sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		Pet pet = MbPets.getInstance().getConfigurations().get(sender);
		if (pet != null) {
			sender.sendMessage(pet.toString());
		} else {
			sender.sendMessage(MbPetsConfig.getTextNode("error.noPreparedPets"));
		}
	}

	/**
	 * regenerates the plugins config. saves the old one before overwriting it
	 * 
	 * @param player
	 */
	private void regenerateConfig(Player player) {
		if (!player.hasPermission("MbPets.regenerate")) {
			player.sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		new File(MbPets.getInstance().getDataFolder().getAbsoluteFile()
				+ "/configOld.yml").delete();
		File oldConfig = new File(MbPets.getInstance().getDataFolder()
				.getAbsoluteFile()
				+ "/config.yml");
		oldConfig.renameTo(new File(MbPets.getInstance().getDataFolder()
				.getAbsoluteFile()
				+ "/configOld.yml"));
		String dbUser = MbPets.getInstance().getConfig()
				.getString("mysql.dbuser");
		String dbPassword = MbPets.getInstance().getConfig()
				.getString("mysql.dbpassword");
		String database = MbPets.getInstance().getConfig()
				.getString("mysql.database");
		String url = MbPets.getInstance().getConfig().getString("mysql.url");
		new File(MbPets.getInstance().getDataFolder().getAbsoluteFile()
				+ "/config.yml").delete();
		MbPets.getInstance().getDataFolder().mkdir();
		MbPets.getInstance().getConfig().set("mysql.dbuser", dbUser);
		MbPets.getInstance().getConfig().set("mysql.dbpassword", dbPassword);
		MbPets.getInstance().getConfig().set("mysql.database", database);
		MbPets.getInstance().getConfig().set("mysql.url", url);
		MbPets.getInstance().saveConfig();
		MbPetsConfig.init();
		MbPets.getInstance().reloadConfig();
		player.sendMessage(MbPetsConfig.getTextNode("info.configRegenerated"));
	}

	/**
	 * reloads the config file
	 * 
	 * @param player
	 */
	private void reloadConfig(Player player) {
		if (!player.hasPermission("MbPets.reload")) {
			player.sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		MbPets.getInstance().reloadConfig();
		player.sendMessage(MbPetsConfig.getTextNode("info.configReloaded"));
	}

	/**
	 * sells a pet
	 * 
	 * @param chatInput
	 */
	private void sellPet(final ChatInput chatInput) {
		Connection connection = DatabaseConnectionManager.getInstance()
				.getConnection();
		if (!chatInput.getSender().hasPermission("MbPets.sell")) {
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		if (connection == null) {
			chatInput.getSender().sendMessage(
					MbPetsConfig.getTextNode("error.noDbConnection"));
			return;
		}
		try {
			Integer number = chatInput.getNumber() != null ? chatInput
					.getNumber() : MbPets.getLatestPetNumber(chatInput
					.getSender());
			Pet pet = MbPets.getInstance()
					.getPet(chatInput.getSender(), number);
			if (pet != null) {
				Double price = pet.sell();
				chatInput.getSender().sendMessage(MbPetsConfig.getTextNode("info.petSold").replace("{0}", pet.getName()).replace("{1}", price.toString()));
			} else {
				chatInput.getSender().sendMessage(
						MbPetsConfig.getTextNode("error.noPetToCall").replace("{0}",
								"Du"));
			}
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	/**
	 * uncalls the given players pet
	 * 
	 * @param sender
	 */
	private void uncallPet(Player sender) {
		if (!sender.getPlayer().hasPermission("MbPets.call")) {
			sender.getPlayer().sendMessage(
					MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		Pet pet = MbPets.getInstance().getPets().get(sender);
		if (pet != null) {
			pet.uncall();
			sender.sendMessage(MbPetsConfig.getTextNode("info.petUncalled"));
		} else {
			sender.sendMessage(MbPetsConfig.getTextNode("error.noActivePet"));
		}
	}

}
