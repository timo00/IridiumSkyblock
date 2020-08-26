package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Island;
import com.iridium.iridiumskyblock.User;
import com.iridium.iridiumskyblock.Utils;
import com.iridium.iridiumskyblock.managers.UserManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class BypassCommand extends Command {

    public BypassCommand() {
        super(Collections.singletonList("bypass"), "Bypass all island restrictions", "iridiumskyblock.bypass", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        User u = UserManager.getUser(p.getUniqueId());
        u.bypassing = !u.bypassing;
        if (u.bypassing) {
            p.sendMessage(Utils.color(IridiumSkyblock.getMessages().nowBypassing.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
        } else {
            p.sendMessage(Utils.color(IridiumSkyblock.getMessages().noLongerBypassing.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
        }
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island) {
        execute(sender, args);
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
