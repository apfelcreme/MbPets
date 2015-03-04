package io.github.apfelcreme.MbPets.Pets;

import java.sql.Connection;
import java.sql.SQLException;

import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Interfaces.Ageable;
import io.github.apfelcreme.MbPets.Interfaces.Dyeable;
import io.github.apfelcreme.MbPets.Interfaces.Styleable;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.HorseWatcher;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;

import java.sql.PreparedStatement;

public class HorsePet extends Pet implements Ageable, Dyeable<Horse.Color>, Styleable<Horse.Style>{

	Boolean isBaby = null;
	Horse.Color color = null;
	Horse.Style style = null;

	public HorsePet(OfflinePlayer owner, String name, Integer number, boolean isBaby, Horse.Color color, Horse.Style style) {
		super(owner, name, DisguiseType.HORSE, number);
		this.isBaby = isBaby;
		this.color = color;
		this.style = style;
	}
	
	@Override
	public Style getStyle() {
		return style;
	}

	@Override
	public void setStyle(Style style) {
		this.style = style;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
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
										"horseColors." + color.name()
												+ ".displaytext")
								: ChatColor.DARK_GRAY
										+ StringUtils.join(MbPetsConfig.getAvailableHorseColors(),", "))
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
										"horseStyles." + color.name()
												+ ".displaytext")
								: ChatColor.DARK_GRAY
										+ StringUtils.join(MbPetsConfig.getAvailableHorseStyles(),", "))
				+ "\n");
		return stringBuilder;
	}

	@Override
	public FlagWatcher prepareWatcher(FlagWatcher watcher) {
		((HorseWatcher)watcher).setBaby(isBaby);
		((HorseWatcher)watcher).setColor(color);
		((HorseWatcher)watcher).setStyle(style);
		((HorseWatcher)watcher).setCustomName(name);
		return watcher;
	}

	@Override
	public PreparedStatement prepareConfirmStatement(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO MbPets_Pet(playerid, petname, type, horsecolor, horsestyle, baby, number)"
				+ " VALUES ("
				+ "(Select playerid from MbPets_Player where uuid = ?),"
				+ "?, ?, ?, ?, ?, ?"
				+ ")");
		statement.setString(1, owner.getUniqueId().toString());
		statement.setString(2, name);
		statement.setString(3, type.toString());
		statement.setString(4, color.name());
		statement.setString(5, style.name());
		statement.setBoolean(6, isBaby);
		statement.setInt(7, number != null ? number : MbPets.getLatestPetNumber(owner)+1);
		return statement;
	}

	@Override
	public boolean isConfigurationFinished() {
		return (type != null && owner != null && name != null && color != null && style != null);
	}

}
