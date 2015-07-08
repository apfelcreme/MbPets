package io.github.apfelcreme.MbPets.Commands;

import io.github.apfelcreme.MbPets.ChatInput;
import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;

import java.io.File;

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
public class RegenerateCommand implements SubCommand {
    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(ChatInput chatInput) {

        if (!chatInput.getSender().hasPermission("MbPets.regenerate")) {
            chatInput.getSender().sendMessage(MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        new File(MbPets.getInstance().getDataFolder().getAbsoluteFile()
                + "/configOld.yml").delete();
        File oldConfig = new File(MbPets.getInstance().getDataFolder()
                .getAbsoluteFile()
                + "/config.yml");
        oldConfig.renameTo(new File(MbPets.getInstance().getDataFolder()
                .getAbsoluteFile()
                + "/configOld.yml"));
        String dbUser = MbPets.getInstance().getConfig()
                .getString("mysql.dbuser");
        String dbPassword = MbPets.getInstance().getConfig()
                .getString("mysql.dbpassword");
        String database = MbPets.getInstance().getConfig()
                .getString("mysql.database");
        String url = MbPets.getInstance().getConfig().getString("mysql.url");
        new File(MbPets.getInstance().getDataFolder().getAbsoluteFile()
                + "/config.yml").delete();
        MbPets.getInstance().getDataFolder().mkdir();
        MbPets.getInstance().getConfig().set("mysql.dbuser", dbUser);
        MbPets.getInstance().getConfig().set("mysql.dbpassword", dbPassword);
        MbPets.getInstance().getConfig().set("mysql.database", database);
        MbPets.getInstance().getConfig().set("mysql.url", url);
        MbPets.getInstance().saveConfig();
        MbPetsConfig.init();
        MbPets.getInstance().reloadConfig();
        chatInput.getSender().sendMessage(MbPetsConfig.getTextNode("info.configRegenerated"));
    }
}
