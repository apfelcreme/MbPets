package io.github.apfelcreme.MbPets.Pets;

import io.github.apfelcreme.MbPets.MbPets;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jan on 17.04.2015.
 */
public class PrimedTnTPet extends Pet {
    public PrimedTnTPet(OfflinePlayer owner, String name, Integer number) {
        super(owner, name, DisguiseType.PRIMED_TNT, number);
    }

    @Override
    public StringBuilder appendStatus(StringBuilder stringBuilder) {
        return stringBuilder;
    }

    @Override
    public FlagWatcher prepareWatcher(FlagWatcher watcher) {
        watcher.setCustomName(name);
        return watcher;
    }

    @Override
    public PreparedStatement prepareConfirmStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO MbPets_Pet(playerid, petname, type, number)"
                + " VALUES ("
                + "(Select playerid from MbPets_Player where uuid = ?),"
                + "?, ?, ?"
                + ")");
        statement.setString(1, owner.getUniqueId().toString());
        statement.setString(2, name);
        statement.setString(3, type.toString());
        statement.setInt(4, number != null ? number : MbPets.getLatestPetNumber(owner)+1);
        return statement;
    }

    @Override
    public boolean isConfigurationFinished() {
        return (type != null && owner != null && name != null);
    }
}
