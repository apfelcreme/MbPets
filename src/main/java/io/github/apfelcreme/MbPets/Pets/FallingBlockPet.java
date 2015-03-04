package io.github.apfelcreme.MbPets.Pets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.FallingBlockWatcher;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Interfaces.FallingBlock;

public class FallingBlockPet extends Pet implements FallingBlock {

	private Material block;

	public FallingBlockPet(OfflinePlayer owner, String name, Integer number, Material block) {
		super(owner, name, DisguiseType.FALLING_BLOCK, number);
		this.block = block;
	}

	@Override
	public Material getBlock() {
		return block;
	}

	@Override
	public void setBlock(Material block) {
		this.block = block;
	}

	@Override
	public StringBuilder appendStatus(StringBuilder stringBuilder) {
		stringBuilder.append(MbPetsConfig
				.getTextNode("info.Element")
				.replace("{0}", "Typ")
				.replace(
						"{1}",
						block != null ? MbPets.getInstance().getConfig().getString("disguiseTypes.FALLING_BLOCK."+block.name()+".displaytext")
								: ChatColor.DARK_GRAY
										+ StringUtils.join(MbPetsConfig.getAvailableFallingBlocks(),", "))
				+ "\n");
		return stringBuilder;
	}

	@Override
	public FlagWatcher prepareWatcher(FlagWatcher watcher) {
		((FallingBlockWatcher)watcher).setCustomName(name);
		((FallingBlockWatcher)watcher).setBlock(new ItemStack(block));
		return watcher;
	}

	@Override
	public PreparedStatement prepareConfirmStatement(Connection connection)
			throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO MbPets_Pet(playerid, petname, type, block, number)"
				+ " VALUES ("
				+ "(Select playerid from MbPets_Player where uuid = ?),"
				+ "?, ?, ?, ?"
				+ ")");
		statement.setString(1, owner.getUniqueId().toString());
		statement.setString(2, name);
		statement.setString(3, type.toString());
		statement.setString(4, block.name());
		statement.setInt(5, number != null ? number : MbPets.getLatestPetNumber(owner)+1);
		return statement;
	}

	@Override
	public boolean isConfigurationFinished() {
		return (type != null && owner != null && name != null && block != null);
	}
	
	/**
	 * extra: display the block instead of the type (which is {@link DisguiseType}.DROPPED_ITEM in all cases 
	 */
	@Override
	public String getListDescription() {
		return MbPetsConfig
				.getTextNode("info.listElement")
				.replace(
						"{0}",number.toString())
				.replace(
						"{1}", name == null || name.isEmpty() ? "unbenannt"
								: name)
				.replace(
						"{2}", MbPets.getInstance().getConfig().getString("disguiseTypes.FALLING_BLOCK."+block.name()+".displaytext"));
	}

}
