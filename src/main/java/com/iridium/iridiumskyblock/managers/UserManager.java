package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Role;
import com.iridium.iridiumskyblock.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    public static HashMap<UUID, User> cache = new HashMap<>();

    public static User getUser(String uuid){
        return getUser(UUID.fromString(uuid));
    }


    //Gets a user from UUID
    public static User getUser(UUID uuid) {
        if (cache.containsKey(uuid)) return cache.get(uuid);
        try {
            PreparedStatement statement = IridiumSkyblock.getSqlManager().getConnection().prepareStatement("SELECT * FROM users WHERE UUID =?;");
            statement.setString(1, uuid.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                //There is a value
                User user = new User();
                user.player = resultSet.getString("UUID");
                user.name = resultSet.getString("name");
                user.islandID = resultSet.getInt("island");
                user.bypassing = resultSet.getBoolean("bypassing");
                user.islandChat = resultSet.getBoolean("chat");
                user.flying = resultSet.getBoolean("flying");
                IridiumSkyblock.getInstance().getLogger().info(resultSet.getString("role"));
                user.role = Role.valueOf(resultSet.getString("role"));
                cache.put(uuid, user);
                return user;
            } else {
                //There is no value so create one
                PreparedStatement insert = IridiumSkyblock.getSqlManager().getConnection().prepareStatement("INSERT INTO users (UUID,name,island,role,bypassing,chat,flying,lastecreate) VALUES (?,?,?,?,?,?,?,?);");
                insert.setString(1, uuid.toString());
                insert.setString(2, "");
                insert.setInt(3, 0);
                insert.setString(4, Role.Visitor.name());
                insert.setBoolean(5, false);
                insert.setBoolean(6, false);
                insert.setBoolean(7, false);
                insert.setInt(8, 0);
                insert.executeUpdate();

                return getUser(uuid);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void saveUser(User user) {
        try {
            PreparedStatement insert = IridiumSkyblock.getSqlManager().getConnection().prepareStatement("UPDATE users SET 'name'=?, 'island'=?, 'role'=?, 'bypassing'=?, 'chat'=?, 'flying'=? WHERE 'UUID'=?;");
            insert.setString(1, user.name);
            insert.setInt(2, user.islandID);
            insert.setString(3, user.role.name());
            insert.setBoolean(4, user.bypassing);
            insert.setBoolean(5, user.islandChat);
            insert.setBoolean(6, user.flying);
            insert.setString(7, user.player);
            insert.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
