package io.github.apfelcreme.MbPets.Commands;

import io.github.apfelcreme.MbPets.ChatInput;
import io.github.apfelcreme.MbPets.DatabaseConnectionManager;
import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import org.bukkit.ChatColor;

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
public class MonitorCommand implements SubCommand {
    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(final ChatInput chatInput) {
        MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
            @Override
            public void run() {

                if (!chatInput.getSender().hasPermission("MbPets.monitor")) {
                    chatInput.getSender().sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
                    return;
                }
                chatInput.getSender().sendMessage(MbPetsConfig
                        .getTextNode("info.monitorDB")
                        .replace(
                                "{0}",
                                DatabaseConnectionManager.getInstance().getConnection() != null ? ChatColor.GREEN
                                        + "aktiv"
                                        : ChatColor.RED + "inaktiv"));
                chatInput.getSender().sendMessage(MbPetsConfig
                        .getTextNode("info.monitorSpawnedPets")
                        .replace("{0}",
                                Integer.toString(MbPets.getInstance().getPets().size())));
                chatInput.getSender().sendMessage(MbPetsConfig.getTextNode("info.monitorPreparedPets")
                        .replace(
                                "{0}",
                                Integer.toString(MbPets.getInstance().getConfigurations()
                                        .size())));
                chatInput.getSender().sendMessage(MbPetsConfig.getTextNode("info.monitorVersion")
                        .replace(
                                "{0}", MbPets.getInstance().getDescription().getVersion()));
            }
        });
    }
}
