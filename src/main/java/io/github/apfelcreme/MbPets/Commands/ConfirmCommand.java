package io.github.apfelcreme.MbPets.Commands;

import io.github.apfelcreme.MbPets.ChatInput;
import io.github.apfelcreme.MbPets.DatabaseConnectionManager;
import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Pets.Pet;
import net.milkbowl.vault.economy.EconomyResponse;

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
public class ConfirmCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(ChatInput chatInput) {

        Connection connection = DatabaseConnectionManager.getInstance()
                .getConnection();
        if (!chatInput.getSender().hasPermission("MbPets.confirm")) {
            chatInput.getSender().sendMessage(
                    MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        if (connection == null) {
            chatInput.getSender().sendMessage(
                    MbPetsConfig.getTextNode("error.noDbConnection"));
            return;
        }
        if (MbPets.getInstance().getPluginVault() == null) {
            chatInput.getSender().sendMessage(
                    MbPetsConfig.getTextNode("error.noVault"));
            return;
        }
        if (!MbPets.getInstance().getEconomy().hasAccount(chatInput.getSender())) {
            return;
        }
//		Pet currentlySpawnedPet = MbPets.getInstance().getPets().get(chatInput.getSender());
//		if (currentlySpawnedPet != null) {
//			currentlySpawnedPet.uncall();
//		}
        Pet pet = MbPets.getInstance().getConfigurations()
                .get(chatInput.getSender());
        if (pet != null) {
            if (pet.isConfigurationFinished()) {
                if (MbPets.getInstance().getEconomy()
                        .getBalance(pet.getOwner()) >= pet.getPrice()) {

                    try {
                        Pet oldPet = MbPets.getInstance().getPet(
                                chatInput.getSender(), pet.getNumber());
                        // delete the old pet that has the same number
                        if (oldPet != null) {
                            pet.setNumber(oldPet.getNumber());
                            oldPet.delete();
                        }
                        // if the to-spawn entity is a requested convert, the
                        // "old"
                        // entity is stored in pet.entity.
                        // it gets despawned now
                        if (pet.getEntity() != null) {
                            if (MbPets.getInstance().getPluginAnimalProtect() != null) {
                                if (MbPets
                                        .getInstance()
                                        .getPluginAnimalProtect()
                                        .hasOwner(
                                                pet.getEntity().getUniqueId()
                                                        .toString())) {
                                    MbPets.getInstance()
                                            .getPluginAnimalProtect()
                                            .unprotectAnimal(
                                                    pet.getEntity()
                                                            .getUniqueId()
                                                            .toString());
                                    MbPets.getInstance()
                                            .getLogger()
                                            .info("Animal "
                                                    + pet.getEntity()
                                                    .getUniqueId()
                                                    .toString()
                                                    + "/ "
                                                    + pet.getEntity().getType()
                                                    .toString()
                                                    + " has been removed. The protection was deleted!");
                                }
                            }
                            pet.getEntity().remove();
                        }

                        // spawn and enter into the db
                        pet.spawn();
                        EconomyResponse response = MbPets.getInstance().getEconomy()
                                .withdrawPlayer(pet.getOwner(), pet.getPrice());
                        if (response.transactionSuccess()) {
                            pet.confirm();
                            MbPets.getInstance().getConfigurations()
                                    .remove(chatInput.getSender());
                            chatInput.getSender().sendMessage(
                                    MbPetsConfig
                                            .getTextNode("info.petConfirmed"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    chatInput.getSender().sendMessage(
                            MbPetsConfig.getTextNode("error.notThatMuchMoney"));
                }
            } else {
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("error.petNotFinished"));
            }
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
