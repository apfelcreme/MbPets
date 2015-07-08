package io.github.apfelcreme.MbPets.Commands;

import io.github.apfelcreme.MbPets.ChatInput;
import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Pets.Pet;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Alliances
 * Copyright (C) 2015 Lord36 aka Apfelcreme
 * <p>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme on 01.07.2015.
 */
public class DespawnCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(ChatInput chatInput) {

        if (!chatInput.getSender().hasPermission("MbPets.despawn")) {
            chatInput.getSender().sendMessage(
                    MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        int i = 0;
        if (chatInput.getNumber() == null) {
            for (Map.Entry<Player, Pet> entry : MbPets.getInstance().getPets().entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().uncall();
                    MbPets.getInstance().getPets().remove(entry.getKey());
                    i++;
                }
            }
            chatInput.getSender().sendMessage(
                    MbPetsConfig.getTextNode("info.despawnCount").replace("{0}", Integer.toString(i)));
        } else {
            for (Map.Entry<Player, Pet> entry : MbPets.getInstance().getPets().entrySet()) {
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
}
