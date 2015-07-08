package io.github.apfelcreme.MbPets.Listener;

import io.github.apfelcreme.MbPets.MbPets;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Created by Jan on 18.04.2015.
 */
public class PlayerTeleportListener implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (MbPets.getInstance().getPets().get(e.getPlayer()) != null) {
            MbPets.getInstance().getPets().get(e.getPlayer()).uncall();
        }
    }

}
