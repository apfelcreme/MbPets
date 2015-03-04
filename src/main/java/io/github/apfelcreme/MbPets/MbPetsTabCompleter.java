package io.github.apfelcreme.MbPets;

import io.github.apfelcreme.MbPets.ChatInput.Operation;
import io.github.apfelcreme.MbPets.Pets.Pet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class MbPetsTabCompleter implements TabCompleter {

	public MbPetsTabCompleter() {
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command,
			String arg2, String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		if (args.length > 1) {
			Operation operation = Operation.getOperation(args[args.length - 2]);
			if (operation != null) {
				Pet pet = MbPets.getInstance().getConfigurations().get(commandSender);
				switch(operation) {
				case BABY:
					list.addAll(Arrays.asList("true", "false"));
					break;
				case COLOR:
					if (pet != null) {
						switch (pet.getType()) {
						case HORSE:
							list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("horseColors").getKeys(true));
							break;
						case SHEEP:
						case WOLF:
							list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("animalColors").getKeys(true));
							break;
						default:
							break;
						}
					} else {
						list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("animalColors").getKeys(true));
						list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("horseColors").getKeys(true));
					}
					break;
				case SIZE: 
					list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("slimeSizes").getKeys(true));
					break;
				case STYLE:
					if (pet != null) {
						switch (pet.getType()) {
						case HORSE:
							list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("horseStyles").getKeys(true));
							break;
						case OCELOT:
							list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("ocelotStyles").getKeys(true));
							break;
						case RABBIT:
							list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("rabbitTypes").getKeys(true));
							break;
						default:
							break;
						}
					} else {
						list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("horseStyles").getKeys(true));
						list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("ocelotStyles").getKeys(true));
						list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("rabbitTypes").getKeys(true));
					}
					break;
				case TYPE:
					list.addAll(MbPets.getInstance().getConfig().getConfigurationSection("disguiseTypes").getKeys(true));
					break;
				default:
					break;			
				}
			}		
		}
		//remove duplicates & sort
		List<String> ret = new ArrayList<String>();
		ret.addAll(new HashSet<String>(list));
		Collections.sort(ret);
		return ret;
	}
}
