package io.github.apfelcreme.MbPets.Listener;

import io.github.apfelcreme.MbPets.MbPets;

import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;

public class NetherPortalEnterListener implements Listener {

	/**
	 * removes Entity from the activePets-List when going through a Nether
	 * Portal bc this caused some kind of endless replication bug...
	 * 
	 * @param event
	 */
	@EventHandler
	public void onEnterNetherPortal(EntityPortalEnterEvent event) {
		if (event.getEntity() instanceof Wolf) {// all pets are disguised wolves
			if (MbPets.getInstance().getPetByEntity(event.getEntity()) != null) {
				// a pet ran through a portal
				MbPets.getInstance().getPetByEntity(event.getEntity()).uncall();
			}
		}
	}
}
