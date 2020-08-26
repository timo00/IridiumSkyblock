package com.iridium.iridiumskyblock;

import java.util.Date;
import java.util.Set;

public class User {

    public String player;
    public String name;
    public int islandID;
    public Role role;
    public Set<Integer> invites;
    public Island.Warp warp;
    public boolean bypassing;
    public boolean islandChat;
    public boolean flying;
    public Date lastCreate;

    public Island getIsland() {
        return IridiumSkyblock.getIslandManager().islands.getOrDefault(islandID, null);
    }

    public Role getRole() {
        if (role == null) {
            if (getIsland() != null) {
                if (getIsland().getOwner().equals(player)) {
                    role = Role.Owner;
                } else {
                    role = Role.Member;
                }
            } else {
                role = Role.Visitor;
            }
        }
        return role;
    }
}
