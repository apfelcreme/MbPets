package io.github.apfelcreme.MbPets.Listener;

import io.github.apfelcreme.MbPets.MbPets;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

	/**
	 * cancels all damage events to make pets invulnerable
	 * 
	 * @param event
	 */
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (MbPets.getInstance().getPetByEntity(event.getEntity()) != null) {
			event.setCancelled(true);
		}
	}
}
