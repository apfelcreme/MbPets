package io.github.apfelcreme.MbPets.Listener;

import io.github.apfelcreme.MbPets.MbPets;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLogoutListener implements Listener {

	/**
	 * removes active pets on player logout
	 * 
	 * @param e
	 */
	@EventHandler
	public void onLogout(PlayerQuitEvent e) {
		if (MbPets.getInstance().getPets().containsKey(e.getPlayer())) {
			MbPets.getInstance().getPets().get(e.getPlayer()).uncall();
		}
	}
}
