package io.github.apfelcreme.MbPets.Pets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Interfaces.Ageable;
import io.github.apfelcreme.MbPets.Interfaces.Dyeable;
import me.libraryaddict.disguise.disguisetypes.AnimalColor;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SheepWatcher;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class SheepPet extends Pet implements Ageable, Dyeable<AnimalColor> {

	AnimalColor color = null;
	Boolean isBaby = null;	

	public SheepPet(OfflinePlayer owner, String name, Integer number, boolean isBaby, AnimalColor color) {
		super(owner, name, DisguiseType.SHEEP, number);
		this.isBaby = isBaby;
		this.color = color;
	}	

	@Override
	public AnimalColor getColor() {
		return color;
	}

	@Override
	public void setColor(AnimalColor color) {
		this.color = color;
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
		stringBuilder.append(MbPetsConfig
				.getTextNode("info.Element")
				.replace("{0}", "Baby")
				.replace(
						"{1}",
						WordUtils.capitalize(Boolean.toString(isBaby())
								.toLowerCase()))
				+ "\n");
		stringBuilder.append(MbPetsConfig
				.getTextNode("info.Element")
				.replace("{0}", "Color")
				.replace(
						"{1}",
						color != null ? MbPets
								.getInstance()
								.getConfig()
								.getString(
										"animalColors." + color.name()
												+ ".displaytext")
								: ChatColor.DARK_GRAY
										+ StringUtils.join(MbPetsConfig.getAvailableColors(),", "))
				+ "\n");
		return stringBuilder;
	}

	@Override
	public FlagWatcher prepareWatcher(FlagWatcher watcher) {
		((SheepWatcher)watcher).setBaby(isBaby);
		((SheepWatcher)watcher).setColor(color);
		((SheepWatcher)watcher).setCustomName(name);
		return watcher;
	}

	@Override
	public PreparedStatement prepareConfirmStatement(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO MbPets_Pet(playerid, petname, type, sheepcolor, baby, number)"
				+ " VALUES ("
				+ "(Select playerid from MbPets_Player where uuid = ?),"
				+ "?, ?, ?, ?, ?"
				+ ")");
		statement.setString(1, owner.getUniqueId().toString());
		statement.setString(2, name);
		statement.setString(3, type.toString());
		statement.setString(4, color.name());
		statement.setBoolean(5, isBaby);
		statement.setInt(6, number != null ? number : MbPets.getLatestPetNumber(owner)+1);
		return statement;
	}

	@Override
	public boolean isConfigurationFinished() {
		return (type != null && owner != null && name != null && color != null);
	}
}
