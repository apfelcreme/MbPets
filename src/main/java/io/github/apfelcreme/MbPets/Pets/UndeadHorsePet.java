package io.github.apfelcreme.MbPets.Pets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Interfaces.Ageable;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.AgeableWatcher;

import org.apache.commons.lang.WordUtils;
import org.bukkit.OfflinePlayer;

public class UndeadHorsePet extends Pet implements Ageable {

	private Boolean isBaby = null;
	
	public UndeadHorsePet(OfflinePlayer owner, String name, Integer number, boolean isBaby) {
		super(owner, name, DisguiseType.UNDEAD_HORSE, number);
		this.isBaby = isBaby;
	}

	@Override
	public Boolean isBaby() {
		return isBaby;
	}

	@Override
	public void setBaby(Boolean isBaby) {
		this.isBaby = isBaby;
	}

	@Override
	public StringBuilder appendStatus(StringBuilder stringBuilder) {
		stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby()).toLowerCase()))+"\n");
		return stringBuilder;
	}

	@Override
	public FlagWatcher prepareWatcher(FlagWatcher watcher) {
		((AgeableWatcher)watcher).setBaby(isBaby);
		((AgeableWatcher)watcher).setCustomName(name);
		return watcher;
	}

	@Override
	public PreparedStatement prepareConfirmStatement(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO MbPets_Pet(playerid, petname, type, baby, number)"
				+ " VALUES ("
				+ "(Select playerid from MbPets_Player where uuid = ?),"
				+ "?, ?, ?, ?"
				+ ")");
		statement.setString(1, owner.getUniqueId().toString());
		statement.setString(2, name);
		statement.setString(3, type.toString());
		statement.setBoolean(4, isBaby);
		statement.setInt(5, number != null ? number : MbPets.getLatestPetNumber(owner)+1);
		return statement;
	}

	@Override
	public boolean isConfigurationFinished() {
		return (type != null && owner != null && name != null);
	}
}
