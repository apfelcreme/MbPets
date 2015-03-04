package io.github.apfelcreme.MbPets.Listener;

import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Pets.ChickenPet;
import io.github.apfelcreme.MbPets.Pets.CowPet;
import io.github.apfelcreme.MbPets.Pets.HorsePet;
import io.github.apfelcreme.MbPets.Pets.IronGolemPet;
import io.github.apfelcreme.MbPets.Pets.MooshroomPet;
import io.github.apfelcreme.MbPets.Pets.OcelotPet;
import io.github.apfelcreme.MbPets.Pets.Pet;
import io.github.apfelcreme.MbPets.Pets.PigPet;
import io.github.apfelcreme.MbPets.Pets.SheepPet;
import io.github.apfelcreme.MbPets.Pets.SkeletonHorsePet;
import io.github.apfelcreme.MbPets.Pets.SlimePet;
import io.github.apfelcreme.MbPets.Pets.UndeadHorsePet;
import io.github.apfelcreme.MbPets.Pets.WolfPet;

import java.util.HashMap;
import java.util.HashSet;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;

import org.bukkit.ChatColor;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitTask;

public class ConvertRightclickListener implements Listener {

	private HashSet<Player> registeredConverts = new HashSet<Player>();
	private HashMap<Player, BukkitTask> timers = new HashMap<Player, BukkitTask>();

	/**
	 * registers a convert-request for a given player
	 * 
	 * @param player
	 */
	public void addConvert(final Player player) {
		if (!player.hasPermission("MbPets.convert")) {
			player.sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
			return;
		}
		registeredConverts.add(player);
		BukkitTask task = MbPets.getInstance().getServer().getScheduler()
				.runTaskLater(MbPets.getInstance(), new Runnable() {
					public void run() {
						registeredConverts.remove(player);
						player.sendMessage(MbPetsConfig.getTextNode("info.petRightclickEnd"));
					}

				}, 200);

		if (timers.containsKey(player)) {
			timers.get(player).cancel();
		}
		player.sendMessage(MbPetsConfig.getTextNode("info.petRightclick"));
		timers.put(player, task);
	}

	/**
	 * converts an entity into a pet after the player entered /pet convert
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerRightClick(final PlayerInteractEntityEvent e) {
		
		MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(), new Runnable() {

			@Override
			public void run() {
				if (registeredConverts.contains(e.getPlayer())) {
					if (MbPets.getInstance().getPetByEntity(e.getRightClicked()) != null) {
						// check whether the right-clicked entity isn't already a
						// pet
						e.getPlayer().sendMessage(
								MbPetsConfig.getTextNode("error.entityIsAlreadyAPet"));
						registeredConverts.remove(e.getPlayer());
						timers.get(e.getPlayer()).cancel();
						return;
					}
					if (MbPets.getInstance().getPluginAnimalProtect() != null) {
						if (MbPets.getInstance().getPluginAnimalProtect()
								.hasOwner(e.getRightClicked().getUniqueId().toString())
								&& !MbPets
										.getInstance()
										.getPluginAnimalProtect()
										.getOwner(
												e.getRightClicked().getUniqueId()
														.toString())
										.equals(e.getPlayer().getUniqueId().toString())) {
							// check whether the animal the player right-clicked is
							// either
							// unprotected or his own.
							e.getPlayer().sendMessage(
									MbPetsConfig.getTextNode("error.notYourPet"));
							registeredConverts.remove(e.getPlayer());
							timers.get(e.getPlayer()).cancel();
							return;
						}
					} 
					if (e.getRightClicked() instanceof Tameable
							&& ((Tameable) e.getRightClicked()).getOwner() != null
							&& !((Tameable) e.getRightClicked()).getOwner().equals(
									e.getPlayer())) {
						// User rightclicked a pet, that is an other players tamed
						// animal
						e.getPlayer().sendMessage(
								MbPetsConfig.getTextNode("error.notYourPet"));
						registeredConverts.remove(e.getPlayer());
						return;
					}
		 			if (MbPetsConfig.parseType(e.getRightClicked().getType().name()) == null) {
						// allow only available pets for a convert
						e.getPlayer().sendMessage(MbPetsConfig.getTextNode("help.TYPE"));
						registeredConverts.remove(e.getPlayer());
						timers.get(e.getPlayer()).cancel();
						return;
					}
		 			Pet pet;
					switch (DisguiseType.getType(e.getRightClicked())) {
					case COW:
						pet = new CowPet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1, 
								!((Ageable)e.getRightClicked()).isAdult());
						break;
					case SHEEP:
						pet = new SheepPet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1, 
								!((Ageable)e.getRightClicked()).isAdult(), 
								MbPetsConfig.parseColor(((Sheep)e.getRightClicked()).getColor().name()));
						break;
					case CHICKEN:
						pet = new ChickenPet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1, 
								!((Ageable)e.getRightClicked()).isAdult());
						break;
					case HORSE:
						pet = new HorsePet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1, 
								!((Ageable)e.getRightClicked()).isAdult(),
								MbPetsConfig.parseHorseColor(((Horse)e.getRightClicked()).getColor().name()),
								MbPetsConfig.parseHorseStyle(((Horse)e.getRightClicked()).getStyle().name()));
//						if (((Horse)e.getRightClicked()).getInventory().getSize() != 0) {
//							e.getPlayer().getWorld().getBlockAt(e.getRightClicked().getLocation()).setType(Material.CHEST);
//							Chest chest = (Chest) e.getPlayer().getWorld().getBlockAt(e.getRightClicked().getLocation());
//							chest.getInventory().setContents(((Horse)e.getRightClicked()).getInventory().getContents());
//						}
						break;
					case UNDEAD_HORSE:
						pet = new UndeadHorsePet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1, 
								!((Ageable)e.getRightClicked()).isAdult());
						break;
					case SKELETON_HORSE:
						pet = new SkeletonHorsePet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1, 
								!((Ageable)e.getRightClicked()).isAdult());
						break;
					case WOLF:
						pet = new WolfPet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1, 
								!((Ageable)e.getRightClicked()).isAdult(), 
								MbPetsConfig.parseColor(((Wolf)e.getRightClicked()).getCollarColor().name()));
						break;
					case OCELOT:
						pet = new OcelotPet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1, 
								!((Ageable)e.getRightClicked()).isAdult(), 
								MbPetsConfig.parseOcelotType(((Ocelot)e.getRightClicked()).getCatType().name()));
						break;
					case PIG:
						pet = new PigPet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1, 
								!((Ageable)e.getRightClicked()).isAdult());
						break;
					case RABBIT:
						e.getPlayer().sendMessage(ChatColor.GREEN+"Hasen können noch nicht konvertiert werden! Folgt später.");
						registeredConverts.remove(e.getPlayer());
						timers.get(e.getPlayer()).cancel();
						return;
						//TODO: Warten, bis man von Rabbits die Farbe auslesen kann >.>
						//pet.setBaby(!((Ageable) e.getRightClicked()).isAdult());
						//pet.setColor(MbPets.parseRabbitType(((Rabbit) e.getRightClicked())
						//	.getRabbitType().name()));
					case MUSHROOM_COW:
						pet = new MooshroomPet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1, 
								!((Ageable)e.getRightClicked()).isAdult());
						break;
					case IRON_GOLEM:
						pet = new IronGolemPet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1);
						break;
					case SLIME:
						System.out.println(((Slime)e.getRightClicked()).getSize());
						pet = new SlimePet(e.getPlayer(), 
								e.getRightClicked().getCustomName(), 
								MbPets.getLatestPetNumber(e.getPlayer()) +1, ((Slime)e.getRightClicked()).getSize());
						break;
					default:
						e.getPlayer().sendMessage(MbPetsConfig.getTextNode("help.TYPE"));
						registeredConverts.remove(e.getPlayer());
						timers.get(e.getPlayer()).cancel();
						return;
					}
//					if (((LivingEntity) e.getRightClicked()).getCustomName() != null) pet.setName(((LivingEntity) e.getRightClicked()).getCustomName());
//					pet.setOwner(e.getPlayer());
					pet.setEntity(e.getRightClicked()); // for a later despawn
					MbPets.getInstance().getConfigurations().put(e.getPlayer(), pet);
					e.getPlayer().sendMessage(pet.toString());
				}
			}});

	}
	
}
