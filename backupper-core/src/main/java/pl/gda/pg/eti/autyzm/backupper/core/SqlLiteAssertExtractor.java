package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.AssertExtractor;
import pl.gda.pg.eti.autyzm.backupper.api.BackupperException;
import pl.gda.pg.eti.autyzm.backupper.api.AssertExtractorException;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlLiteAssertExtractor implements AssertExtractor<File> {
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

    @Override
    public List<URI> extractAsserts(File dbFile) {
        Objects.requireNonNull(dbFile, "dbFile arg is null");

        if (!dbFile.exists())
            throw new IllegalArgumentException("dbFile doesn't exist");
        else if (!dbFile.isFile())
            throw new IllegalArgumentException("dbFile must be a file!");

        try {
            Collection<URI> activityMedia = getActivityMedia(dbFile);
            Collection<URI> actionMedia = getActionMedia(dbFile);
            Collection<URI> settingsMedia = getSettingsMedia(dbFile);

            return Stream.of(activityMedia, actionMedia, settingsMedia)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new AssertExtractorException("Failed to extract asserts", e);
        }

    }

    private static Collection<URI> getActivityMedia(File dbFile) throws SQLException {
        return extractPaths(dbFile, ACTIVITY_TABLE_NAME, ACTIVITY_COLUMN_NAMES);
    }

    private static Collection<URI> getActionMedia(File dbFile) throws SQLException {
        return extractPaths(dbFile, ACTION_TABLE_NAME, ACTION_COLUMN_NAMES);
    }

    private static Collection<URI> getSettingsMedia(File dbFile) throws SQLException {
        return extractPaths(dbFile, SETTINGS_TABLE_NAME, SETTINGS_COLUMN_NAMES);
    }

    private static Collection<URI> extractPaths(File dbFile, String tableName,
                                                String... sourceColumns)
            throws SQLException {
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
                    if (path != null && path.length() > 0) {
                        try {
                            paths.add(new URI(path));
                        } catch (URISyntaxException e) {
                            throw new AssertExtractorException("Bad path found", e);
                        }
                    }
                }
            }
        }

        return paths;
    }
}
