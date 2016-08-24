package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.Backup;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.api.BackupperException;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.RemoteFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class FileBackupper implements Backupper {
    private static final File DATA_FOLDER = new File("data");

    static {
        if (!DATA_FOLDER.exists())
            DATA_FOLDER.mkdir();
    }

    private final String PATH_TO_DB = "/storage/sdcard0/commments2.db";
    private final String DB_NAME = "commments2.db";


    @Override
    public void makeBackup(String backupName, JadbDevice device) throws BackupperException {
        try {
            createBackupFolder(backupName);
            copyDbFromDevice(device, PATH_TO_DB, backupName);
            copyAssetsFromDevice(device, backupName);
        } catch (IOException e) {
            throw new BackupperException(e);
        }
    }

    @Override
    public List<Backup> getBackups() throws BackupperException {
        return null;
    }

    @Override
    public Optional<Backup> getBackup(String backupName) throws BackupperException {
        return null;
    }

    /**
     * Creates new backup folder in {@link FileBackupper#DATA_FOLDER}.
     *
     * @param backupName name of backup directory - alias for backup
     * @throws IOException
     */
    private void createBackupFolder(String backupName) throws IOException {
        new File(DATA_FOLDER, backupName).mkdir();
    }


    /**
     * Copies file from specified device via ADB
     *
     * @param device     adb-enabled device to copy from
     * @param copyFrom   source file patch
     * @param backupName destination backup name
     * @throws IOException if method fails to copy file
     */
    private void copyDbFromDevice(JadbDevice device, String copyFrom, String backupName) throws IOException {
        try {
            String copyFromFileName = Paths.get(new URI(copyFrom).getPath()).getFileName().toString();
            File fullPath = new File(DATA_FOLDER, backupName + File.separator + copyFromFileName);
            device.pull(new RemoteFile(copyFrom), fullPath);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Bad copyFrom path", e);
        } catch (JadbException e) {
            throw new IOException("Failed to pull file from device", e);
        }
    }

    private void copyAssetsFromDevice(JadbDevice device, String backupName) throws IOException {

        File fullPath = new File(DATA_FOLDER, backupName + File.separator + DB_NAME);

        SqlLiteAssertExtractor assertExtractor = new SqlLiteAssertExtractor();
        List<URI> uriList = assertExtractor.extractAsserts(fullPath);
        for (URI item : uriList) {
            RemoteFile assetOnAndroid = new RemoteFile(item.getPath());
            File pathToAssetOnPC = new File(DATA_FOLDER, backupName + File.separator + assetOnAndroid.getName());
            try {
                device.pull(assetOnAndroid, pathToAssetOnPC);
            } catch (IOException | JadbException e) {
                throw new IOException("Failed to pull assets from device", e);
            }

        }
    }
}
