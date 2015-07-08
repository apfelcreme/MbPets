package io.github.apfelcreme.MbPets.Pets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Interfaces.DroppedItem;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.DroppedItemWatcher;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class DroppedItemPet extends Pet implements DroppedItem {

    private Material material;

    public DroppedItemPet(OfflinePlayer owner, String name, Integer number, Material material) {
        super(owner, name, DisguiseType.DROPPED_ITEM, number);
        this.material = material;
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
        stringBuilder.append(MbPetsConfig
                .getTextNode("info.Element")
                .replace("{0}", "Typ")
                .replace(
                        "{1}",
                        material != null ? MbPets
                                .getInstance().getConfig().getString(
                                        "disguiseTypes.DROPPED_ITEM."
                                                + material.name()
                                                + ".displaytext")
                                : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig .getAvailableDroppedItems(), ", "))
                + "\n");
        return stringBuilder;
    }

    @Override
    public FlagWatcher prepareWatcher(FlagWatcher watcher) {
        ((DroppedItemWatcher) watcher).setCustomName(name);
        ((DroppedItemWatcher) watcher).setItemStack(new ItemStack(material));
        return watcher;
    }

    @Override
    public PreparedStatement prepareConfirmStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO MbPets_Pet(playerid, petname, type, material, number)"
                + " VALUES ("
                + "(Select playerid from MbPets_Player where uuid = ?),"
                + "?, ?, ?, ?"
                + ")");
        statement.setString(1, owner.getUniqueId().toString());
        statement.setString(2, name);
        statement.setString(3, type.toString());
        statement.setString(4, material.name());
        statement.setInt(5, number != null ? number : MbPets.getLatestPetNumber(owner) + 1);
        return statement;
    }

    @Override
    public boolean isConfigurationFinished() {
        return (type != null && owner != null && name != null && material != null);
    }

    /**
     * extra: display the material instead of the type (which is {@link DisguiseType}.DROPPED_ITEM in all cases
     */
    @Override
    public String getListDescription() {
        return MbPetsConfig
                .getTextNode("info.listElement")
                .replace(
                        "{0}", number.toString())
                .replace(
                        "{1}", name == null || name.isEmpty() ? "unbenannt"
                                : name)
                .replace(
                        "{2}", MbPets.getInstance().getConfig().getString("disguiseTypes.DROPPED_ITEM." + material.name() + ".displaytext"));
    }

}
