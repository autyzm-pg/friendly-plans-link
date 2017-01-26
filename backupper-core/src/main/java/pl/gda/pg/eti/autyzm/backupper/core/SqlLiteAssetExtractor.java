package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.AssetExtractor;
import pl.gda.pg.eti.autyzm.backupper.api.BackupperException;
import pl.gda.pg.eti.autyzm.backupper.api.AssetExtractorException;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Everything in -core implements APIs from -api.

// Can someone please explain why the hell is this even needed? It's nice to put
// everything behind interfaces when it's cleanly decoupled and all... just that
// there'll never be 2 implementations of those interfaces, therefore there's no
// point in creating these in the first place. :|

public class SqlLiteAssetExtractor implements AssetExtractor<File> {
    private static final String[][] ASSET_TABLES_COLUMNS = {
            {"AKTYWNOSC", "ICONPATH"},
            {"AKTYWNOSC", "AUDIOPATH"},
            {"CZYNNOSC", "AUDIO"},
            {"CZYNNOSC", "OBRAZEK"},
            {"USTAWIENIA_UZYTKOWNIKA", "TIMER_SOUND_PATH"}
    };

    //private static final String ACTIVITY_TABLE_NAME = "AKTYWNOSC";
    //private static final String[] ACTIVITY_COLUMN_NAMES = {"ICONPATH", "AUDIOPATH"};

    //private static final String ACTION_TABLE_NAME = "CZYNNOSC";
    //private static final String[] ACTION_COLUMN_NAMES = {"AUDIO", "OBRAZEK"};

    //private static final String SETTINGS_TABLE_NAME = "USTAWIENIA_UZYTKOWNIKA";
    //private static final String[] SETTINGS_COLUMN_NAMES = {"TIMER_SOUND_PATH"};

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new BackupperException("Failed to initialize the SQLite JDBC Driver", e);
        }
    }

    @Override
    public List<URI> extractAssets(File dbFile) {
        Objects.requireNonNull(dbFile, "dbFile arg is null");

        try {
            Connection dbConn = getDatabaseConnection(dbFile);

            Collection<URI>[] mediaURIs = new Collection[ASSET_TABLES_COLUMNS.length];
            for (int i = 0; i < ASSET_TABLES_COLUMNS.length; i++) {
                mediaURIs[i] = extractPaths(dbConn,
                        ASSET_TABLES_COLUMNS[i][0],
                        ASSET_TABLES_COLUMNS[i][1]);
            }

            // Collection<URI> activityMedia = extractPaths(dbConn, ACTIVITY_TABLE_NAME, ACTIVITY_COLUMN_NAMES);
            // Collection<URI> actionMedia = extractPaths(dbConn, ACTION_TABLE_NAME, ACTION_COLUMN_NAMES);
            // Collection<URI> settingsMedia = extractPaths(dbConn, SETTINGS_TABLE_NAME, SETTINGS_COLUMN_NAMES);

            dbConn.close();

            return Stream.of(mediaURIs)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new AssetExtractorException("Failed to extract assets", e);
        }
    }

    public void replaceAssetURIs(File dbFile, Map<URI, String> stringsToReplace) {
        Objects.requireNonNull(dbFile);
        Objects.requireNonNull(stringsToReplace);

        try {
            Connection dbConn = getDatabaseConnection(dbFile);

            // Disable automatic transaction creation. By default JDBC will create a new
            // transaction before each statement and *commit it right away*. Let's wait a sec.
            dbConn.setAutoCommit(false);

            // For each Table-Column pair, try to update the mappings.
            for (String[] AssetTableColumnPair : ASSET_TABLES_COLUMNS) {
                try {
                    Statement stmt = dbConn.createStatement();

                    for (Map.Entry<URI, String> entry : stringsToReplace.entrySet()) {
                        // This is where all assets will be uploaded to from now on.

                        String replacement = "/storage/sdcard0/FriendlyPlans/LegacyAssets/" + entry.getValue();
                        stmt.addBatch(
                                "UPDATE " + AssetTableColumnPair[0] +
                                " SET " + AssetTableColumnPair[1] + "='" + replacement +
                                "' WHERE " + AssetTableColumnPair[1] + "='" + entry.getKey() + "'"
                        );
                    }

                    // All updates prepared, let's give it a try!
                    stmt.executeBatch();
                    dbConn.commit();
                } catch (SQLException ex1) {
                    try {
                        dbConn.rollback();
                    } catch (SQLException ex2) {
                        throw new AssetExtractorException("Failed to update database and rollback, database might be damaged");
                    }

                    throw new AssetExtractorException("Failed to update database (rolled back changes)", ex1);
                }
            }

            dbConn.close();
        } catch (Exception e) {
            throw new AssetExtractorException("Failed to update database", e);
        }
    }

    private static Connection getDatabaseConnection(File dbFile) throws SQLException{
        Objects.requireNonNull(dbFile);

        if (!dbFile.exists())
            throw new IllegalArgumentException("dbFile doesn't exist");
        else if (!dbFile.isFile())
            throw new IllegalArgumentException("dbFile must be a file!");

        return DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
    }

    private static Collection<URI> extractPaths(Connection dbConnection,
                                                String tableName,
                                                String sourceColumn) throws SQLException {
        Collection<URI> paths = new LinkedList<>();
        Statement stmt = dbConnection.createStatement();

        ResultSet resultSet = stmt.executeQuery("Select " + sourceColumn + " from " + tableName);
        while (resultSet.next()) {
            String path = resultSet.getString(sourceColumn);
            if (path != null && path.length() > 0) {
                try {
                    paths.add(new URI(path));
                } catch (URISyntaxException e) {
                    throw new AssetExtractorException("Bad path found", e);
                }
            }
        }

        return paths;
    }
}
