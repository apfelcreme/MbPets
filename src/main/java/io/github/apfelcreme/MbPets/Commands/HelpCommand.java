package io.github.apfelcreme.MbPets.Commands;

import io.github.apfelcreme.MbPets.ChatInput;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.apache.commons.lang.StringUtils;
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
public class HelpCommand implements SubCommand {
    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(ChatInput chatInput) {

        if (!chatInput.getSender().hasPermission("MbPets.print")) {
            chatInput.getSender().sendMessage(
                    MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        if (chatInput.getType() == null) {
            // user only entered /pet help
            chatInput.getSender().sendMessage(
                    MbPetsConfig.getTextNode("help.Options"));
            chatInput.getSender().sendMessage(
                    MbPetsConfig.getTextNode("info.types") + ChatColor.GREEN
                            + StringUtils.join(MbPetsConfig.getAvailableTypes(), ", ") + ChatColor.GREEN + ", "
                            + StringUtils.join(MbPetsConfig.getAvailableDroppedItems(), ", ") + ChatColor.GREEN + ", "
                            + StringUtils.join(MbPetsConfig.getAvailableFallingBlocks(), ", "));
            return;
        }
        chatInput.getSender().sendMessage(MbPetsConfig.getTextNode("help.Head"));
        DisguiseType type = MbPetsConfig.parseType(chatInput.getType()) != null ? MbPetsConfig
                .parseType(chatInput.getType()) : DisguiseType.DROPPED_ITEM;
        switch (type) {
            case CHICKEN:
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case COW:
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case DROPPED_ITEM:
                chatInput
                        .getSender()
                        .sendMessage(
                                MbPetsConfig.getTextNode("info.Element")
                                        .replace("{0}",
                                                ChatColor.DARK_GREEN + "Typ")
                                        .replace(
                                                "{1}",
                                                StringUtils.join(MbPetsConfig.getAvailableDroppedItems(), ", ")));
                break;
            case HORSE:
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Color")
                                .replace("{1}",
                                        StringUtils.join(MbPetsConfig.getAvailableHorseColors(), ", ")));
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Style")
                                .replace("{1}",
                                        StringUtils.join(MbPetsConfig.getAvailableHorseStyles(), ", ")));
                break;
            case MUSHROOM_COW:
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case OCELOT:
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Style")
                                .replace("{1}",
                                        StringUtils.join(MbPetsConfig.getAvailableOcelotStyles(), ", ")));
                break;
            case PIG:
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case RABBIT:
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Style")
                                .replace("{1}",
                                        StringUtils.join(MbPetsConfig.getAvailableRabbitStyles(), ", ")));
                break;
            case SHEEP:
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Color")
                                .replace("{1}", StringUtils.join(MbPetsConfig.getAvailableColors(), ", ")));
                break;
            case SKELETON_HORSE:
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case UNDEAD_HORSE:
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case WOLF:
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                chatInput.getSender().sendMessage(
                        MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Color")
                                .replace("{1}", StringUtils.join(MbPetsConfig.getAvailableColors(), ", ")));
                break;
            default:
                chatInput.getSender().sendMessage(ChatColor.DARK_GREEN + "keine");
                return;
        }
        chatInput.getSender().sendMessage(
                MbPetsConfig.getTextNode("info.Element")
                        .replace("{0}", ChatColor.DARK_GREEN + "Name")
                        .replace("{1}", MbPetsConfig.getTextNode("help.NAME")));
    }
}
