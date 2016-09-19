package pl.gda.pg.eti.autyzm.backupper.core;


import pl.gda.pg.eti.autyzm.backupper.core.Models.*;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Db {

    private File dbFile;

    public Db(File dbFile) {
        if (!dbFile.exists()) {
            throw new IllegalArgumentException(String.format("dbFile: %s doesn't exists", dbFile.getAbsolutePath()));
        }
        this.dbFile = dbFile;
    }

    public List<Activity> getAllActivities()  throws SQLException {

        List<Activity> activities = new ArrayList<Activity>();

        String dbFilePath = dbFile.getAbsolutePath();
        String conString = String.format("jdbc:sqlite:%s", dbFilePath);
        try (Connection conn = DriverManager.getConnection(conString)) {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("Select ID,title from AKTYWNOSC where type_flag='ACTIVITY'");

            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);

                activities.add(new Activity(id, name));
            }
        }
        return activities;
    }
}
