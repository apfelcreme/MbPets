package io.github.apfelcreme.MbPets.Listener;


import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.Pets.Pet;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityRightClickListener implements Listener {
	
	/**
	 * cancels all interacts on entities
	 * @param e
	 */
	@EventHandler
	public void onEntityRightClick(PlayerInteractEntityEvent e) {
		final Pet pet = MbPets.getInstance().getPetByEntity(e.getRightClicked());
		if (pet != null) {
			Disguise disguise = pet.getDisguise();
			if (disguise != null) {
				FlagWatcher watcher = disguise.getWatcher();
				watcher.setCustomName(pet.getOwner().getName());
				disguise.setWatcher(watcher);
				DisguiseAPI.disguiseEntity(pet.getEntity(), disguise);
				MbPets.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(MbPets.getInstance(), new Runnable() {

					@Override
					public void run() {
						Disguise disguise = pet.getDisguise();
						if (disguise != null) {
							FlagWatcher watcher = disguise.getWatcher();
							watcher.setCustomName(pet.getName());
							disguise.setWatcher(watcher);
							DisguiseAPI.disguiseEntity(pet.getEntity(), disguise);
						}
					}}, 100L);
			}
		}
	}
}
