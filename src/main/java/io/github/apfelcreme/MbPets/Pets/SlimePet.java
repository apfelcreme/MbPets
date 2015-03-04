package io.github.apfelcreme.MbPets.Pets;

import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Interfaces.Sizeable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SlimeWatcher;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class SlimePet extends Pet implements Sizeable {

	private Integer size;

	public SlimePet(OfflinePlayer owner, String name, Integer number, Integer size) {
		super(owner, name, DisguiseType.SLIME, number);
		this.size = size;
	}

	@Override
	public Integer getSize() {
		return size;
	}

	@Override
	public void setSize(Integer size) {
		this.size = size;		
	}

	@Override
	public StringBuilder appendStatus(StringBuilder stringBuilder) {
		stringBuilder.append(MbPetsConfig
				.getTextNode("info.Element")
				.replace("{0}", "Size")
				.replace(
						"{1}", size != null ? 
						MbPets.getInstance()
								.getConfig()
								.getString(
										"slimeSizes." + size + ".displaytext") :  ChatColor.DARK_GRAY
										+ StringUtils.join(MbPetsConfig.getAvailableSlimeSizes(),", "))
				+ "\n");
		return stringBuilder;
	}

	@Override
	public FlagWatcher prepareWatcher(FlagWatcher watcher) {
		((SlimeWatcher)watcher).setCustomName(name);
		((SlimeWatcher)watcher).setSize(size);
		return watcher;
	}
	
	@Override
	public PreparedStatement prepareConfirmStatement(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO MbPets_Pet(playerid, petname, type, number, size)"
				+ " VALUES ("
				+ "(Select playerid from MbPets_Player where uuid = ?),"
				+ "?, ?, ?, ?"
				+ ")");
		statement.setString(1, owner.getUniqueId().toString());
		statement.setString(2, name);
		statement.setString(3, type.toString());
		statement.setInt(4, number != null ? number : MbPets.getLatestPetNumber(owner)+1);
		statement.setInt(5, size);
		return statement;
	}

	@Override
	public boolean isConfigurationFinished() {
		return (type != null && owner != null && name != null && size != null);
	}
}
