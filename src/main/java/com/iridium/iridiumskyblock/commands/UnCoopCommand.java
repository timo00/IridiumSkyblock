package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Island;
import com.iridium.iridiumskyblock.User;
import com.iridium.iridiumskyblock.Utils;
import com.iridium.iridiumskyblock.managers.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class UnCoopCommand extends Command {

    public UnCoopCommand() {
        super(Collections.singletonList("uncoop"), "Revokes an Islands coop", "", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(Utils.color(IridiumSkyblock.getConfiguration().prefix) + "/is uncoop <player>");
            return;
        }
        Player p = (Player) sender;
        User user = UserManager.getUser(p.getUniqueId());
        if (user.getIsland() != null) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            if (!user.getIsland().equals(UserManager.getUser(player.getUniqueId()).getIsland()) && UserManager.getUser(player.getUniqueId()).getIsland() != null) {
                if (user.bypassing || user.getIsland().getPermissions(user.getRole()).coop) {
                    user.getIsland().removeCoop(UserManager.getUser(player.getUniqueId()).getIsland());
                } else {
                    sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().noPermission.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                }
            } else {
                sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().playerNoIsland.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
            }
        } else {
            sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
        }
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island) {
        if (args.length != 4) {
            sender.sendMessage(Utils.color(IridiumSkyblock.getConfiguration().prefix) + "/is admin <island> uncoop <player>");
            return;
        }
        if (island != null) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            User user = UserManager.getUser(player.getUniqueId());
            if (!island.equals(user.getIsland()) && user.getIsland() != null) {
                island.removeCoop(user.getIsland());
            } else {
                sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().playerNoIsland.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
            }
        } else {
            sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
        }
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
