package io.github.apfelcreme.MbPets.Pets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.DroppedItemWatcher;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Interfaces.DroppedItem;

public class AngelPet extends Pet implements DroppedItem {

	public int taskId;
	public Location locaction;
	private Material material;

	public AngelPet(OfflinePlayer owner, String name, Integer number) {
		super(owner, name, DisguiseType.DROPPED_ITEM, number);
		this.material = Material.SNOW_BLOCK;
		price *= 2;
	}

	@Override
	public void spawn() {
		super.spawn();
		taskId = MbPets
				.getInstance()
				.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(MbPets.getInstance(),
						new Runnable() {

							@Override
							public void run() {
								locaction = entity.getLocation();
								locaction.setY(locaction.getY() + 1);
								owner.getPlayer()
										.getWorld()
										.spigot()
										.playEffect(
												locaction,
												Effect.SNOW_SHOVEL,
												0,
												0,
												(float) (-1 + Math.random() * 2),
												(float) (Math.random() * 2.5f),
												(float) (-1 + Math.random() * 2),
												0, 1, 50);
								owner.getPlayer()
										.getWorld()
										.spigot()
										.playEffect(
												locaction,
												Effect.FIREWORKS_SPARK,
												0,
												0,
												(float) (-1 + Math.random() * 2),
												(float) (Math.random() * 2.5f),
												(float) (-1 + Math.random() * 2),
												0, 1, 50);

							}
						}, 10L, 2L);
	}

	@Override
	public void uncall() {
		super.uncall();
		MbPets.getInstance().getServer().getScheduler().cancelTask(taskId);
	}

	@Override
	public Material getMaterial() {
		return material;
	}

	@Override
	public void setMaterial(Material material) {
		this.material = material;
	}

	@Override
	public StringBuilder appendStatus(StringBuilder stringBuilder) {
		stringBuilder.append(MbPetsConfig.getTextNode("info.Element")
				.replace("{0}", "Typ").replace("{1}", "Eisengel\n"));
		return stringBuilder;
	}

	@Override
	public FlagWatcher prepareWatcher(FlagWatcher watcher) {
//		((DroppedItemWatcher) watcher).setCustomNameVisible(true);
		((DroppedItemWatcher) watcher).setCustomName(name);
		((DroppedItemWatcher) watcher).setItemStack(new ItemStack(material));
		return watcher;
	}

	@Override
	public PreparedStatement prepareConfirmStatement(Connection connection)
			throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement("INSERT INTO MbPets_Pet(playerid, petname, type, material, number)"
						+ " VALUES ("
						+ "(Select playerid from MbPets_Player where uuid = ?),"
						+ "?, ?, ?, ?" + ")");
		statement.setString(1, owner.getUniqueId().toString());
		statement.setString(2, name);
		statement.setString(3, type.toString());
		statement.setString(4, material.name());
		statement.setInt(5,
				number != null ? number : MbPets.getLatestPetNumber(owner) + 1);
		return statement;
	}

	@Override
	public boolean isConfigurationFinished() {
		return (type != null && owner != null && name != null && material != null);
	}

	/**
	 * extra: display the material instead of the type (which is
	 * {@link DisguiseType}.DROPPED_ITEM in all cases
	 */
	@Override
	public String getListDescription() {
		return MbPetsConfig
				.getTextNode("info.listElement")
				.replace("{0}", number.toString())
				.replace("{1}",
						name == null || name.isEmpty() ? "unbenannt" : name)
				.replace(
						"{2}","Eisengel");
	}

}
