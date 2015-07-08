package io.github.apfelcreme.MbPets;

import io.github.apfelcreme.MbPets.ChatInput.Operation;
import io.github.apfelcreme.MbPets.Commands.*;
import net.zaiyers.UUIDDB.bukkit.UUIDDB;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MbPetsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd,
                             String lbl, String[] args) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            if (cmd.getName().equalsIgnoreCase("pet")) {
                ChatInput chatInput = explodeChatInput(args, sender);
                SubCommand subCommand = null;
                if (chatInput != null) {
                    switch (chatInput.getOperation()) {
                        case BABY:
                        case COLOR:
                        case STYLE:
                        case SIZE:
                        case TYPE:
                        case NAME:
                            subCommand = new CreatePetCommand();
                            break;
                        case CALL:
                            subCommand = new CallCommand();
                            break;
                        case CANCEL:
                            subCommand = new CancelPreparationCommand();
                            break;
                        case CONFIRM:
                            subCommand = new ConfirmCommand();
                            break;
                        case CONVERT:
                            subCommand = new ConvertCommand();
                            break;
                        case DELETE:
                            subCommand = new DeleteCommand();
                            break;
                        case DESPAWN:
                            subCommand = new DespawnCommand();
                            break;
                        case FLUSH:
                            subCommand = new FlushCommand();
                            break;
                        case HELP:
                            subCommand = new HelpCommand();
                            break;
                        case INFO:
                            subCommand = new InfoCommand();
                            break;
                        case LIST:
                            subCommand = new ListCommand();
                            break;
                        case MODIFY:
                            subCommand = new ModifyCommand();
                            break;
                        case MONITOR:
                            subCommand = new MonitorCommand();
                            break;
                        case REGENERATE:
                            subCommand = new RegenerateCommand();
                            break;
                        case RELOAD:
                            subCommand = new ReloadCommand();
                            break;
                        case SELL:
                            subCommand = new SellCommand();
                            break;
                        case STATUS:
                            subCommand = new PrintStatusCommand();
                            break;
                        case UNCALL:
                            subCommand = new UncallCommand();
                            break;
                        default:
                            break;
                    }
                }
                if (subCommand != null) {
                    subCommand.execute(chatInput);
                }
            }
        }
        return false;
    }

    /**
     * explodes the users chat input and stores it into a {@link ChatInput}
     * object
     *
     * @param args   the command args
     * @param sender the command sender
     * @return nicely stored information
     */
    private ChatInput explodeChatInput(String[] args, Player sender) {
        if (args.length == 0) {
            sender.sendMessage(MbPetsConfig.getTextNode("help.Options"));
            sender.sendMessage(
                    MbPetsConfig.getTextNode("info.types") + ChatColor.GREEN
                            + StringUtils.join(MbPetsConfig.getAvailableTypes(), ", ") + ChatColor.GREEN + ", "
                            + StringUtils.join(MbPetsConfig.getAvailableDroppedItems(), ", ") + ChatColor.GREEN + ", "
                            + StringUtils.join(MbPetsConfig.getAvailableFallingBlocks(), ", "));
            return null;
        } else {
            ChatInput chatInput = new ChatInput();
            chatInput.setSender(sender);
            Operation mainOperation = Operation.getOperation(args[0]);
            if (mainOperation != null) {
                chatInput.setOperation(mainOperation);
            } else {
                sender.sendMessage(MbPetsConfig.getTextNode("error.wrongFunction")
                        .replace("{0}", args[0]));
                return null;
            }
            for (int i = 0; i < args.length; i++) {
                Operation operation = Operation.getOperation(args[i]);
                if (NumberUtils.isNumber(args[i])) {
                    chatInput.setNumber(Integer.parseInt(args[i]));
                    continue;
                }
                if (operation != null) {
                    if (i + 1 < args.length || !Operation.needsValue(operation)) {
                        switch (operation) {
                            case BABY:
                                chatInput.setBaby(args[i + 1]);
                                continue;
                            case COLOR:
                                chatInput.setColor(args[i + 1]);
                                continue;
                            case NAME:
                                chatInput.setName(args[i + 1]);
                                continue;
                            case SIZE:
                                chatInput.setSize(args[i + 1]);
                                continue;
                            case STYLE:
                                chatInput.setStyle(args[i + 1]);
                                continue;
                            case TYPE:
                                chatInput.setType(args[i + 1]);
                                continue;
                            default:
                                continue;
                        }
                    } else {
                        sender.sendMessage(MbPetsConfig.getTextNode(
                                "error.missingValue").replace(
                                "{0}",
                                WordUtils.capitalize(operation.name()
                                        .toLowerCase())));
                        return null;
                    }
                }
                if (UUIDDB.getInstance() != null) {
                    if (UUIDDB.getInstance().getStorage().getUUIDByName(args[i], false) != null) {
                        chatInput.setTargetPlayer(MbPets
                                .getInstance()
                                .getServer()
                                .getOfflinePlayer(
                                        UUID.fromString(UUIDDB.getInstance().getStorage().getUUIDByName(args[i], false))));
                    }
                }
            }
            return chatInput;

        }
    }

}
