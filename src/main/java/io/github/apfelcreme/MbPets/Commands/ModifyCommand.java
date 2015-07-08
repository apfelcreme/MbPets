package io.github.apfelcreme.MbPets.Commands;

import io.github.apfelcreme.MbPets.ChatInput;
import io.github.apfelcreme.MbPets.DatabaseConnectionManager;
import io.github.apfelcreme.MbPets.Interfaces.DroppedItem;
import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Pets.Pet;

import java.sql.Connection;
import java.sql.SQLException;

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
public class ModifyCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(final ChatInput chatInput) {

        MbPets.getInstance().getServer().getScheduler()
                .runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
                    public void run() {
                        Connection connection = DatabaseConnectionManager.getInstance()
                                .getConnection();
                        if (MbPets.getInstance().getPluginVault() == null) {
                            chatInput.getSender().sendMessage(
                                    MbPetsConfig.getTextNode("error.noVault"));
                            return;
                        }
                        if (!chatInput.getSender().hasPermission("MbPets.modify")) {
                            chatInput.getSender().sendMessage(
                                    MbPetsConfig.getTextNode("error.noPermission"));
                            return;
                        }
                        if (connection == null) {
                            chatInput.getSender().sendMessage(
                                    MbPetsConfig.getTextNode("error.noDbConnection"));
                            return;
                        }
                        int number = chatInput.getNumber() != null ? chatInput
                                .getNumber() : MbPets
                                .getLatestPetNumber(chatInput.getSender());
                        try {
                            Pet pet = MbPets.getInstance().getPet(
                                    chatInput.getSender(), number);
                            MbPets.getInstance().getConfigurations()
                                    .put(chatInput.getSender(), pet);
                            if (!(pet instanceof DroppedItem)) {
                                pet.setPrice(MbPets.getInstance().getConfig()
                                        .getDouble("prices.MODIFY", 1000));
                            } else {
                                //items names arent currently displayed so it would be unfair to charge the owners for changing a name they don't see
                                pet.setPrice(0.0);
                            }
                            chatInput.getSender().sendMessage(pet.toString());
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
