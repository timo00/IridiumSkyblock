package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
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
                IridiumSkyblock.getInstance().getLogger().log(Level.SEVERE, "SQL exception on initialize", ex);
            } catch (ClassNotFoundException ex) {
                IridiumSkyblock.getInstance().getLogger().log(Level.SEVERE, "Could not find SQL library");
            }
        } else {
            //Use SQL
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + sql.host + ":" + sql.port + "/" + sql.database, sql.username, sql.password);
            } catch (SQLException ex) {
                IridiumSkyblock.getInstance().getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
            } catch (ClassNotFoundException ex) {
                IridiumSkyblock.getInstance().getLogger().log(Level.SEVERE, "Could not find SQL library");
            }
        }
    }

    public void createTables() {
        try {
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS users "
                    + "(UUID VARCHAR(255), name VARCHAR(255), island INTEGER, role VARCHAR(255), bypassing BOOLEAN, chat BOOLEAN, flying BOOLEAN, lastecreate BIGINT, PRIMARY KEY (UUID));");

            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS islands "
                    + "(id INTEGER, pos1x INTEGER, pos1z INTEGER, pos2x INTEGER, pos2z INTEGER, homex INTEGER, homey INTEGER, homez INTEGER, spawnerBooster INTEGER, farmingBooster INTEGER, expBooster INTEGER, flightBooster INTEGER, " +
                    "crystals INTEGER, sizeLevel INTEGER, memberLevel INTEGER, warpLevel INTEGER, oreLevel INTEGER, value DOUBLE, startvalue DOUBLE, extravalue DOUBLE, visit BOOLEAN, borderColor STRING, schematic STRING, netherschematic STRING, " +
                    "name STRING, money DOUBLE, exp INTEGER, biome STRING, lastRegen BIGINT, PRIMARY KEY (id));");

            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS claims "
                    + "(x INTEGER, z INTEGER, island INTEGER);");

        } catch (SQLException ex) {
            IridiumSkyblock.getInstance().getLogger().log(Level.SEVERE, "SQLite exception on Creating Tables", ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
