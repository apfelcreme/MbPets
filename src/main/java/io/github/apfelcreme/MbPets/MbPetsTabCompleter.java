package io.github.apfelcreme.MbPets;

import io.github.apfelcreme.MbPets.ChatInput.Operation;
import io.github.apfelcreme.MbPets.Pets.Pet;

import java.util.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;

import javax.security.auth.login.Configuration;

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
                switch (operation) {
                    case BABY:
                        list.addAll(Arrays.asList("true", "false"));
                        break;
                    case COLOR:
                        if (pet != null) {
                            switch (pet.getType()) {
                                case HORSE:
                                    list.addAll(getDisplayTexts("horseColors"));
                                    break;
                                case SHEEP:
                                case WOLF:
                                    list.addAll(getDisplayTexts("animalColors"));
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            list.addAll(getDisplayTexts("horseColors"));
                            list.addAll(getDisplayTexts("animalColors"));
                        }
                        break;
                    case SIZE:
                        list.addAll(getDisplayTexts("slimeSizes"));
                        break;
                    case STYLE:
                        if (pet != null) {
                            switch (pet.getType()) {
                                case HORSE:
                                    list.addAll(getDisplayTexts("horseStyles"));
                                    break;
                                case OCELOT:
                                    list.addAll(getDisplayTexts("ocelotStyles"));
                                    break;
                                case RABBIT:
                                    list.addAll(getDisplayTexts("rabbitTypes"));
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            list.addAll(getDisplayTexts("horseStyles"));
                            list.addAll(getDisplayTexts("ocelotStyles"));
                            list.addAll(getDisplayTexts("rabbitTypes"));
                        }
                        break;
                    case TYPE:
                        list.addAll(getDisplayTexts("disguiseTypes"));
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

    /**
     * returns a list of nice strings
     * @param section the ConfigurationSection name
     * @return the list of displaytexts
     */
    private List<String> getDisplayTexts(String section) {
        List<String> list = new ArrayList<String>();
        for (String key : MbPets.getInstance().getConfig().getConfigurationSection(section).getKeys(true)) {
            if (key.endsWith("displaytext")) {
                list.add(MbPets.getInstance().getConfig().getString(section + "." + key));
            }
        }
        return list;
    }
}
