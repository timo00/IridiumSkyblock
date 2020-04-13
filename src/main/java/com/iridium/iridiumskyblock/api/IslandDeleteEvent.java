package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.Island;
import org.jetbrains.annotations.NotNull;

public class IslandDeleteEvent extends IslandEvent {
    public IslandDeleteEvent(@NotNull Island island) {
        super(island);
    }
}
