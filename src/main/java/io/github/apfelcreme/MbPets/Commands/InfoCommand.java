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
public class InfoCommand implements SubCommand {

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
                        try {
                            Integer number = chatInput.getNumber() != null ? chatInput
                                    .getNumber() : MbPets
                                    .getLatestPetNumber(chatInput.getSender());
                            Pet pet = MbPets.getInstance().getPet(
                                    chatInput.getSender(), number);
                            if (pet != null) {
                                chatInput.getSender().sendMessage(
                                        pet.toShortString());
                            } else {
                                chatInput.getSender().sendMessage(
                                        MbPetsConfig.getTextNode("error.noPetToCall")
                                                .replace("{0}", "Du"));
                            }
                            connection.close();
                        } catch (SQLException e) {

                            e.printStackTrace();
                        }
                    }
                });
    }
}
