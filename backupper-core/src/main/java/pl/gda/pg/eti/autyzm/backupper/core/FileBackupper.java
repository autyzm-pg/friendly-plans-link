package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.Backup;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.api.BackupperException;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by superuser on 22-Jun-16.
 */
public class FileBackupper implements Backupper {
    private static final String ACTIVITY_TABLE_NAME = "AKTYWNOSC";
    private static final String[] ACTIVITY_COLUMN_NAMES = {"ICONPATH", "AUDIOPATH"};

    private static final String ACTION_TABLE_NAME = "CZYNNOSC";
    private static final String[] ACTION_COLUMN_NAMES = {"AUDIO", "OBRAZEK"};

    private static final String SETTINGS_TABLE_NAME = "USTAWIENIA_UZYTKOWNIKA";
    private static final String[] SETTINGS_COLUMN_NAMES = {"TIMER_SOUND_PATH"};

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new BackupperException("Failed to initialize SQLLite JDBC Driver", e);
        }
    }

    @Override public void makeBackup(String backupName, URI pathToDevice)
        throws BackupperException {
    }

    @Override public List<Backup> getBackups() throws BackupperException {
        return null;
    }

    @Override public Optional<Backup> getBackup(String backupName) throws BackupperException {
        return null;
    }

    private List<URI> extractMediaURI(File dbFile) throws SQLException, URISyntaxException {
        Objects.requireNonNull(dbFile, "dbFile arg is null");

        if (!dbFile.exists())
            throw new IllegalArgumentException("dbFile doesn't exist");
        else if (!dbFile.isFile())
            throw new IllegalArgumentException("dbFile must be a file!");

        Collection<URI> activityMedia = getActivityMedia(dbFile);
        Collection<URI> actionMedia = getActionMedia(dbFile);
        Collection<URI> settingsMedia = getSettingsMedia(dbFile);

        return Stream.of(activityMedia, actionMedia, settingsMedia)
                     .flatMap(Collection::stream)
                     .collect(Collectors.toList());
    }

    private Collection<URI> getActivityMedia(File dbFile) throws SQLException, URISyntaxException {
        return extractPaths(dbFile, ACTIVITY_TABLE_NAME, ACTIVITY_COLUMN_NAMES);
    }

    private Collection<URI> getActionMedia(File dbFile) throws SQLException, URISyntaxException {
        return extractPaths(dbFile, ACTION_TABLE_NAME, ACTION_COLUMN_NAMES);
    }

    private Collection<URI> getSettingsMedia(File dbFile) throws SQLException, URISyntaxException {
        return extractPaths(dbFile, SETTINGS_TABLE_NAME, SETTINGS_COLUMN_NAMES);
    }

    private Collection<URI> extractPaths(File dbFile, String tableName, String... sourceColumns)
        throws SQLException, URISyntaxException {
        Collection<URI> paths = new LinkedList<>();

        String dbFilePath = dbFile.getAbsolutePath();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath)) {
            Statement stmt = conn.createStatement();

            StringJoiner columnsJoiner = new StringJoiner(",");
            Arrays.stream(sourceColumns).forEach(columnsJoiner::add);
            String sqlColumns = columnsJoiner.toString();

            ResultSet resultSet = stmt.executeQuery("Select " + sqlColumns + " from " + tableName);
            while (resultSet.next()) {
                for (int i = 1; i <= sourceColumns.length; i++) {
                    String path = resultSet.getString(i);
                    if (path != null && path.length() > 0)
                        paths.add(new URI(clearAndroidPath(path)));
                }
            }
        }

        return paths;
    }

    private String clearAndroidPath(String path) {
        return path.replaceFirst("/storage/emulated/0", "")
                   .replaceFirst("/storage/emulated/legacy", "");
    }
}
