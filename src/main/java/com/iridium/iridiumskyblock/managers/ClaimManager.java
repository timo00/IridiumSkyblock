package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ClaimManager {

    public static Map<List<Integer>, Set<Integer>> cache = new HashMap<>();

    //Gets a user from UUID
    public static Set<Integer> getIslands(int x, int z) {
        List<Integer> chunkKey = Collections.unmodifiableList(Arrays.asList(x, z));
        if (cache.containsKey(chunkKey)) return cache.get(chunkKey);
        Set<Integer> islands = new HashSet<>();
        try {
            PreparedStatement statement = IridiumSkyblock.getSqlManager().getConnection().prepareStatement("SELECT * FROM claims WHERE x =? AND z=?;");
            statement.setInt(1, x);
            statement.setInt(2, z);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                islands.add(resultSet.getInt("island"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return islands;
    }

    public static void addClaim(int x, int z, int island) {
        try {
            PreparedStatement insert = IridiumSkyblock.getSqlManager().getConnection().prepareStatement("INSERT INTO claims (x,z,island) VALUES (?,?,?);");
            insert.setInt(1, x);
            insert.setInt(2, z);
            insert.setInt(3, island);
            insert.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void removeClaims(int island) {
        try {
            PreparedStatement insert = IridiumSkyblock.getSqlManager().getConnection().prepareStatement("DELETE FROM claims WHERE island=?;");
            insert.setInt(1, island);
            insert.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
