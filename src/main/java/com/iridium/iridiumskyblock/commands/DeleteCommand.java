package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.*;
import com.iridium.iridiumskyblock.gui.ConfirmationGUI;
import com.iridium.iridiumskyblock.managers.UserManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class DeleteCommand extends Command {

    public DeleteCommand() {
        super(Collections.singletonList("delete"), "Delete you island", "", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        User user = UserManager.getUser(p.getUniqueId());
        if (user.getIsland() != null) {
            if (user.role.equals(Role.Owner)) {
                p.openInventory(new ConfirmationGUI(user.getIsland(), () -> user.getIsland().delete(), IridiumSkyblock.getMessages().deleteAction).getInventory());
            } else {
                sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().mustBeIslandOwner.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
            }
        } else {
            sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
        }
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island) {
        Player p = (Player) sender;
        if (island != null) {
            p.openInventory(new ConfirmationGUI(island, island::delete, IridiumSkyblock.getMessages().deleteAction).getInventory());
        } else {
            sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
        }
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
