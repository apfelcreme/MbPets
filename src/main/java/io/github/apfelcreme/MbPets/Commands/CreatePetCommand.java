package io.github.apfelcreme.MbPets.Commands;

import io.github.apfelcreme.MbPets.ChatInput;
import io.github.apfelcreme.MbPets.Interfaces.Ageable;
import io.github.apfelcreme.MbPets.Interfaces.DroppedItem;
import io.github.apfelcreme.MbPets.Interfaces.FallingBlock;
import io.github.apfelcreme.MbPets.Interfaces.Sizeable;
import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Pets.*;
import me.libraryaddict.disguise.disguisetypes.AnimalColor;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.RabbitType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;

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
public class CreatePetCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(ChatInput chatInput) {
        if (!chatInput.getSender().getPlayer().hasPermission("MbPets.buy")) {
            chatInput.getSender().getPlayer()
                    .sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        Pet pet = null;
        Pet oldPet = null;
        Player owner = chatInput.getSender();
        DisguiseType type = MbPetsConfig.parseType(chatInput.getType());
        String name = "";
        String color = "";
        String style = "";
        Integer size = MbPetsConfig.parseSlimeSize(chatInput.getSize());
        Material material = MbPetsConfig.parseMaterial(chatInput.getType());
        Material block = MbPetsConfig.parseBlock(chatInput.getType());
        Boolean isBaby = false;
        Integer number = MbPets.getLatestPetNumber(owner) + 1;
        if (MbPets.getInstance().getConfigurations().get(chatInput.getSender()) != null) {
            // get the attributes the current pet object has stored
            oldPet = MbPets.getInstance().getConfigurations()
                    .get(chatInput.getSender());
            type = oldPet.getType();
            number = oldPet.getNumber();
            if (oldPet instanceof HorsePet) {
                color = ((HorsePet) oldPet).getColor() != null ? ((HorsePet) oldPet)
                        .getColor().name() : null;
                style = ((HorsePet) oldPet).getStyle() != null ? ((HorsePet) oldPet)
                        .getStyle().name() : null;
            } else if (oldPet instanceof SheepPet
                    && ((SheepPet) oldPet).getColor() != null) {
                color = ((SheepPet) oldPet).getColor().name();
            } else if (oldPet instanceof WolfPet
                    && ((WolfPet) oldPet).getColor() != null) {
                color = ((WolfPet) oldPet).getColor().name();
            } else if (oldPet instanceof OcelotPet
                    && ((OcelotPet) oldPet).getStyle() != null) {
                style = ((OcelotPet) oldPet).getStyle().name();
            } else if (oldPet instanceof RabbitPet
                    && ((RabbitPet) oldPet).getStyle() != null) {
                style = ((RabbitPet) oldPet).getStyle().name();
            } else if (oldPet instanceof Sizeable
                    && ((Sizeable) oldPet).getSize() != null) {
                size = ((Sizeable) oldPet).getSize();
            }
            if (oldPet instanceof Ageable && ((Ageable) oldPet).isBaby() != null) {
                isBaby = ((Ageable) oldPet).isBaby();
            }
            if (oldPet instanceof DroppedItem) {
                material = ((DroppedItem) oldPet).getMaterial();
                type = DisguiseType.DROPPED_ITEM;
            }
            if (oldPet instanceof FallingBlock) {
                block = ((FallingBlock) oldPet).getBlock();
                type = DisguiseType.FALLING_BLOCK;
            }
            name = oldPet.getName();
        }
        name = chatInput.getName() != null ? chatInput.getName() : name;
        color = chatInput.getColor() != null ? chatInput.getColor() : color;
        style = chatInput.getStyle() != null ? chatInput.getStyle() : style;
        isBaby = Boolean.parseBoolean(chatInput.getBaby() != null ? chatInput
                .getBaby() : isBaby.toString());
        number = chatInput.getNumber() != null ? chatInput.getNumber() : number;
        size = chatInput.getSize() != null ? MbPetsConfig.parseSlimeSize(chatInput.getSize()) : size;
        // a type must be entered first to ensure, that the correct color and
        // style attributes are set!
        if (type == null) {
            if (MbPetsConfig.parseMaterial(chatInput.getType()) != null) {
                type = DisguiseType.DROPPED_ITEM;
            } else if (MbPetsConfig.parseBlock(chatInput.getType()) != null) {
                type = DisguiseType.FALLING_BLOCK;
            } else {
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("error.missingType"));
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.types") + ChatColor.GREEN
                                + StringUtils.join(MbPetsConfig.getAvailableTypes(), ", ") + ChatColor.GREEN + ", "
                                + StringUtils.join(MbPetsConfig.getAvailableDroppedItems(), ", ") + ChatColor.GREEN + ", "
                                + StringUtils.join(MbPetsConfig.getAvailableFallingBlocks(), ", "));
                return;
            }
        }

        switch (type) {
            case CHICKEN:
                pet = new ChickenPet(owner, name, number, isBaby);
                break;
            case COW:
                pet = new CowPet(owner, name, number, isBaby);
                break;
            case DROPPED_ITEM:
                if (material != null) {
                    switch (material) {
                        case OBSIDIAN:
                            pet = new DevilPet(owner, name, number);
                            break;
                        case SNOW_BLOCK:
                            pet = new AngelPet(owner, name, number);
                            break;
                        default:
                            pet = new DroppedItemPet(owner, name, number, material);
                            break;
                    }
                } else {
                    chatInput.getSender().sendMessage(
                            MbPetsConfig.getTextNode("info.types")
                                    + ChatColor.GREEN
                                    + MbPetsConfig.getAvailableDroppedItems());
                }
                break;
            case ENDERMAN:
                //endermen sind buggy <.<
                pet = new EndermanPet(owner, name, number);
//			chatInput.getSender().sendMessage(ChatColor.RED+"Endermen sind derzeit noch nicht verfügbar! Warte ab ;)");
//			return;
                break;
            case FALLING_BLOCK:
                if (block != null) {
                    pet = new FallingBlockPet(owner, name, number, block);
                } else {
                    chatInput.getSender().sendMessage(
                            MbPetsConfig.getTextNode("info.types")
                                    + ChatColor.GREEN
                                    + MbPetsConfig.getAvailableFallingBlocks());
                }
                break;
            case HORSE:
                pet = new HorsePet(owner, name, number, isBaby,
                        MbPetsConfig.parseHorseColor(color),
                        MbPetsConfig.parseHorseStyle(style));
                break;
            case IRON_GOLEM:
                pet = new IronGolemPet(owner, name, number);
                break;
            case MAGMA_CUBE:
                pet = new MagmaCubePet(owner, name, number, size);
                break;
            case MUSHROOM_COW:
                pet = new MooshroomPet(owner, name, number, isBaby);
                break;
            case OCELOT:
                pet = new OcelotPet(owner, name, number, isBaby,
                        MbPetsConfig.parseOcelotType(style));
                break;
            case PIG:
                pet = new PigPet(owner, name, number, isBaby);
                break;
            case PRIMED_TNT:
                //pet = new PrimedTnTPet(owner, name, number);
                owner.sendMessage(ChatColor.RED + "Diese Pets sind derzeit noch nicht verfügbar!");
                return;
            case RABBIT:
                pet = new RabbitPet(owner, name, number, isBaby,
                        MbPetsConfig.parseRabbitType(style));
                break;
            case SHEEP:
                pet = new SheepPet(owner, name, number, isBaby,
                        MbPetsConfig.parseColor(color));
                break;
            case SLIME:
                pet = new SlimePet(owner, name, number, size);
                break;
            case SKELETON_HORSE:
                pet = new SkeletonHorsePet(owner, name, number, isBaby);
                break;
            case UNDEAD_HORSE:
                pet = new UndeadHorsePet(owner, name, number, isBaby);
                break;
            case WOLF:
                pet = new WolfPet(owner, name, number, isBaby,
                        MbPetsConfig.parseColor(color));
                break;
            default:
                break;
        }
        if (oldPet != null) {
            // for uncommon prices e.g. modification price is 1000 benches
            pet.setPrice(oldPet.getPrice());
            if (oldPet.getEntity() != null) {
                //for converted pets as they store the old entity in this field
                pet.setEntity(oldPet.getEntity());
            }
        }
        MbPets.getInstance().getConfigurations()
                .put(chatInput.getSender(), pet);
        owner.sendMessage(pet.toString());

    }

}
