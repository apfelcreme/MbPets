package io.github.apfelcreme.MbPets.Listener;

import io.github.apfelcreme.MbPets.MbPets;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;

public class EntityInteractListener implements Listener {
	
	/**
	 * cancels all interacts on entities
	 * @param e
	 */
	@EventHandler
	public void onEntityInteract(EntityInteractEvent e) {
		if (MbPets.getInstance().getPets().containsValue(MbPets.getInstance().getPetByEntity(e.getEntity()))) {
			// a pet is rightclicked
			e.setCancelled(true);
		}
	}
}
