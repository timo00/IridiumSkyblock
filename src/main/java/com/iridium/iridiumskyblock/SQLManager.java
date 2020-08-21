package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.configs.SQL;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLManager {
    private Connection connection;

    public void setupConnection() {
        final SQL sql = IridiumSkyblock.getSql();
        //Check if we need to use SQL or SQLLite
        if (IridiumSkyblock.sql.username.isEmpty()) {
            //If the username is empty, continue with sql lite
            File dataFolder = new File(IridiumSkyblock.getInstance().getDataFolder(), sql.database + ".db");
            if (!dataFolder.exists()) {
                //Create the .db file if it doesnt exist
                try {
                    dataFolder.createNewFile();
                } catch (IOException e) {
                    IridiumSkyblock.getInstance().getLogger().log(Level.SEVERE, "File write error: " + sql.database + ".db");
                }
            }
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            } catch (SQLException ex) {
                IridiumSkyblock.getInstance().getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
            } catch (ClassNotFoundException ex) {
                IridiumSkyblock.getInstance().getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
            }
        } else {
            //Use SQL
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + sql.host + ":" + sql.port + "/" + sql.database, sql.username, sql.password);
            } catch (SQLException ex) {
                IridiumSkyblock.getInstance().getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
            } catch (ClassNotFoundException ex) {
                IridiumSkyblock.getInstance().getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
