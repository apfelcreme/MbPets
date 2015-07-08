package io.github.apfelcreme.MbPets.Commands;

import io.github.apfelcreme.MbPets.ChatInput;
import io.github.apfelcreme.MbPets.DatabaseConnectionManager;
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
public class ListCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(final ChatInput chatInput) {
        MbPets.getInstance().getServer().getScheduler()
                .runTaskAsynchronously(MbPets.getInstance(), new Runnable() {

                    @Override
                    public void run() {
                        Connection connection = DatabaseConnectionManager.getInstance()
                                .getConnection();
                        if (!chatInput.getSender()
                                .hasPermission("MbPets.print")) {
                            chatInput.getSender().sendMessage(
                                    MbPetsConfig.getTextNode("error.noPermission"));
                            return;
                        }
                        if (connection == null) {
                            chatInput.getSender().sendMessage(
                                    MbPetsConfig.getTextNode("error.noDbConnection"));
                            return;
                        }
                        if (chatInput.getTargetPlayer() == null) {
                            // a user wants to see his own list
                            chatInput.setTargetPlayer(chatInput.getSender());
                        }
                        chatInput.getSender().sendMessage(
                                MbPetsConfig.getTextNode("info.listHead").replace(
                                        "{0}",
                                        chatInput
                                                .getSender()
                                                .getName()
                                                .equals(chatInput
                                                        .getTargetPlayer()
                                                        .getName()) ? "Du"
                                                : chatInput.getTargetPlayer()
                                                .getName()));
                        for (int i = 1; i <= MbPets
                                .getLatestPetNumber(chatInput.getTargetPlayer()); i++) {
                            try {
                                Pet pet = MbPets.getInstance()
                                        .getPet(chatInput.getTargetPlayer(), i);
                                if (pet != null)
                                    chatInput.getSender().sendMessage(
                                            pet.getListDescription());
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }
}
