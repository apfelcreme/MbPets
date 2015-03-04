package io.github.apfelcreme.MbPets.Pets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.RabbitType;
import me.libraryaddict.disguise.disguisetypes.watchers.RabbitWatcher;
import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Interfaces.Ageable;
import io.github.apfelcreme.MbPets.Interfaces.Styleable;

public class RabbitPet extends Pet implements Ageable, Styleable<RabbitType>{
	

	Boolean isBaby = null;	
	RabbitType style = null;

	public RabbitPet(OfflinePlayer owner, String name, Integer number, boolean isBaby, RabbitType style) {
		super(owner, name, DisguiseType.RABBIT, number);
		this.isBaby = isBaby;
		this.style = style;
	}

	@Override
	public RabbitType getStyle() {
		return style;
	}

	@Override
	public void setStyle(RabbitType style) {
		this.style = style;
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
				.replace("{0}", "Style")
				.replace(
						"{1}",
						style != null ? MbPets
								.getInstance()
								.getConfig()
								.getString(
										"rabbitTypes." + style.name()
												+ ".displaytext")
								: ChatColor.DARK_GRAY
										+ StringUtils.join(MbPetsConfig
												.getAvailableRabbitStyles(),", "))
				+ "\n");
		return stringBuilder;
	}

	@Override
	public FlagWatcher prepareWatcher(FlagWatcher watcher) {
		((RabbitWatcher)watcher).setBaby(isBaby);
		((RabbitWatcher)watcher).setType(style);
		((RabbitWatcher)watcher).setCustomName(name);
		return watcher;
	}

	@Override
	public PreparedStatement prepareConfirmStatement(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO MbPets_Pet(playerid, petname, type, rabbittype, baby, number)"
				+ " VALUES ("
				+ "(Select playerid from MbPets_Player where uuid = ?),"
				+ "?, ?, ?, ?, ?"
				+ ")");
		statement.setString(1, owner.getUniqueId().toString());
		statement.setString(2, name);
		statement.setString(3, type.toString());
		statement.setString(4, style.name());
		statement.setBoolean(5, isBaby);
		statement.setInt(6, number != null ? number : MbPets.getLatestPetNumber(owner)+1);
		return statement;
	}

	@Override
	public boolean isConfigurationFinished() {
		return (type != null && owner != null && name != null && style != null);
	}
}
