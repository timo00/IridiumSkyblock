package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Island;
import com.iridium.iridiumskyblock.User;
import com.iridium.iridiumskyblock.Utils;
import com.iridium.iridiumskyblock.managers.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerTalkListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTalk(AsyncPlayerChatEvent event) {
        try {
            final Player player = event.getPlayer();
            final User user = UserManager.getUser(player.getUniqueId());

            if (user.warp != null) {
                if (user.warp.getPassword().equals(event.getMessage())) {
                    Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> player.teleport(user.warp.getLocation()));
                    player.sendMessage(Utils.color(IridiumSkyblock.getMessages().teleporting
                            .replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                } else {
                    player.sendMessage(Utils.color(IridiumSkyblock.getMessages().wrongPassword
                            .replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                    user.warp = null;
                }
                event.setCancelled(true);
            }

            final Island island = user.getIsland();

            String format = event.getFormat();
            if (format.contains(IridiumSkyblock.getConfiguration().chatRankPlaceholder)) {
                if (island == null) {
                    format = format.replace(IridiumSkyblock.getConfiguration().chatRankPlaceholder, "");
                } else {
                    format = format.replace(IridiumSkyblock.getConfiguration().chatRankPlaceholder, Utils.getIslandRank(island) + "");
                }
            }
            if (format.contains(IridiumSkyblock.getConfiguration().chatNAMEPlaceholder)) {
                if (island == null) {
                    format = format.replace(IridiumSkyblock.getConfiguration().chatNAMEPlaceholder, "");
                } else {
                    format = format.replace(IridiumSkyblock.getConfiguration().chatNAMEPlaceholder, island.getName());
                }
            }
            if (format.contains(IridiumSkyblock.getConfiguration().chatValuePlaceholder)) {
                if (island == null) {
                    format = format.replace(IridiumSkyblock.getConfiguration().chatValuePlaceholder, "");
                } else {
                    format = format.replace(IridiumSkyblock.getConfiguration().chatValuePlaceholder, island.getValue() + "");
                }
            }
            if (format.contains(IridiumSkyblock.getConfiguration().chatLevelPlaceholder)) {
                if (island == null) {
                    format = format.replace(IridiumSkyblock.getConfiguration().chatLevelPlaceholder, "");
                } else {
                    format = format.replace(IridiumSkyblock.getConfiguration().chatLevelPlaceholder, String.format("%.2f", island.getValue()));
                }
            }

            if (island != null && user.islandChat) {
                for (String member : island.getMembers()) {
                    final Player islandPlayer = Bukkit.getPlayer(UserManager.getUser(member).name);
                    if (islandPlayer == null) continue;
                    islandPlayer.sendMessage(Utils.color(IridiumSkyblock.getMessages().chatFormat)
                            .replace(IridiumSkyblock.getConfiguration().chatValuePlaceholder, island.getValue() + "")
                            .replace(IridiumSkyblock.getConfiguration().chatNAMEPlaceholder, island.getName())
                            .replace(IridiumSkyblock.getConfiguration().chatLevelPlaceholder, String.format("%.2f", island.getValue()))
                            .replace(IridiumSkyblock.getConfiguration().chatRankPlaceholder, Utils.getIslandRank(island) + "")
                            .replace("%player%", player.getName())
                            .replace("%message%", event.getMessage()));
                }
                event.setCancelled(true);
            }

            event.setFormat(Utils.color(format));
        } catch (Exception e) {
            IridiumSkyblock.getInstance().sendErrorMessage(e);
        }
    }
}
