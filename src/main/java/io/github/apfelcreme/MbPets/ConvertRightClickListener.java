package io.github.apfelcreme.MbPets;

import java.util.HashMap;
import java.util.HashSet;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitTask;

public class ConvertRightClickListener implements Listener {

	private HashSet<Player> registeredConverts = new HashSet<Player>();
	private HashMap<Player, BukkitTask> timers = new HashMap<Player, BukkitTask>();

	/**
	 * registers a convert-request for a given player
	 * 
	 * @param player
	 */
	public void addConvert(final Player player) {
		if (!player.hasPermission("MbPets.convert")) {
			player.sendMessage(MbPetsUtils.getTextNode("error.noPermission"));
			return;
		}
		registeredConverts.add(player);
		BukkitTask task = MbPets.getInstance().getServer().getScheduler()
				.runTaskLater(MbPets.getInstance(), new Runnable() {
					public void run() {
						registeredConverts.remove(player);
					}

				}, 200);

		if (timers.containsKey(player)) {
			timers.get(player).cancel();
		}
		player.sendMessage(MbPetsUtils.getTextNode("info.petRightclick"));
		timers.put(player, task);
	}

	/**
	 * converts an entity into a pet after the player entered /pet convert
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerRightClick(PlayerInteractEntityEvent e) {

		if (registeredConverts.contains(e.getPlayer())) {
			if (MbPets.getInstance().getPetByEntity(e.getRightClicked()) != null) {
				// check whether the right-clicked entity isn't already a
				// pet
				e.getPlayer().sendMessage(
						MbPetsUtils.getTextNode("error.entityIsAlreadyAPet"));
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
							MbPetsUtils.getTextNode("error.notYourPet"));
					registeredConverts.remove(e.getPlayer());
					timers.get(e.getPlayer()).cancel();
					return;
				}
			} 
			if (MbPetsUtils.parseType(e.getRightClicked().getType().name()) == null) {
				// allow only available pets for a convert
				e.getPlayer().sendMessage(MbPetsUtils.getTextNode("help.TYPE"));
				registeredConverts.remove(e.getPlayer());
				timers.get(e.getPlayer()).cancel();
				return;
			}
			Pet pet = new Pet();
			pet.setType(DisguiseType.getType(e.getRightClicked().getType()));
			switch (DisguiseType.getType(e.getRightClicked())) {
			case COW:
				pet.setBaby(!((Ageable) e.getRightClicked()).isAdult());
				break;
			case SHEEP:
				pet.setBaby(!((Ageable) e.getRightClicked()).isAdult());
				pet.setColor(MbPetsUtils.parseColor(((Sheep) e
						.getRightClicked()).getColor().name()));
				break;
			case CHICKEN:
				pet.setBaby(!((Ageable) e.getRightClicked()).isAdult());
				break;
			case HORSE:
				pet.setBaby(!((Ageable) e.getRightClicked()).isAdult());
				pet.setHorseColor(MbPetsUtils.parseHorseColor(((Horse) e
						.getRightClicked()).getColor().name()));
				pet.setHorseStyle(MbPetsUtils.parseHorseStyle(((Horse) e
						.getRightClicked()).getStyle().name()));
				break;
			case UNDEAD_HORSE:
				pet.setBaby(!((Ageable) e.getRightClicked()).isAdult());
				break;
			case SKELETON_HORSE:
				pet.setBaby(!((Ageable) e.getRightClicked()).isAdult());
				break;
			case WOLF:
				pet.setBaby(!((Ageable) e.getRightClicked()).isAdult());
				pet.setColor(MbPetsUtils.parseColor(((Wolf) e.getRightClicked())
						.getCollarColor().name()));
				break;
			case OCELOT:
				pet.setBaby(!((Ageable) e.getRightClicked()).isAdult());
				pet.setOcelotStyle(MbPetsUtils.parseOcelotType(((Ocelot) e
						.getRightClicked()).getCatType().name()));
				break;
			case PIG:
				pet.setBaby(!((Ageable) e.getRightClicked()).isAdult());
				break;
			case MUSHROOM_COW:
				pet.setBaby(!((Ageable) e.getRightClicked()).isAdult());
				break;
			default:
				break;
			}
			pet.setPrice();
			if (((LivingEntity) e.getRightClicked()).getCustomName() != null) pet.setName(((LivingEntity) e.getRightClicked()).getCustomName());
			pet.setOwner(e.getPlayer());
			pet.setEntity(e.getRightClicked()); // for a later despawn
			MbPets.getInstance().addPreparedPet(pet);
			pet.printStatus(false);
		}
	}
}
