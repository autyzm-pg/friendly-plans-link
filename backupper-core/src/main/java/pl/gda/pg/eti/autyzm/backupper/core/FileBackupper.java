package pl.gda.pg.eti.autyzm.backupper.core;

import org.apache.commons.io.FilenameUtils;
import pl.gda.pg.eti.autyzm.backupper.api.Backup;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.api.BackupperException;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.RemoteFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class FileBackupper implements Backupper {
    private static final File DATA_FOLDER = new File("data");

    static {
        if (!DATA_FOLDER.exists())
            DATA_FOLDER.mkdir();
    }


    @Override
    public void makeBackup(String backupName, JadbDevice device) throws BackupperException {
        try {
            // This should never break, but people have silly Java installations, occasionally.
            // Check if we can initialize the hasher first. (HashingUtils will break terribly otherwise.)
            MessageDigest md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new BackupperException("The Java version you're running doesn't support MD5 hashing and cannot create backups.");
        }

        try {
            createBackupFolder(backupName);
            copyDbFromDevice(device, Config.PATH_TO_DB, backupName);
            copyAssetsFromDevice(device, backupName);
        } catch (IOException e) {
            throw new BackupperException(e);
        } catch (Exception e) {
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

    static File getBackupDatabase(String backupName) {
        return new File(DATA_FOLDER, backupName + File.separator + "commments2.db");
    }

    /**
     * Creates new backup folder in {@link FileBackupper#DATA_FOLDER}.
     *
     * @param backupName name of backup directory - alias for backup
     * @throws IOException on directory creation failure
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

    private void copyAssetsFromDevice(JadbDevice device, String backupName) throws Exception {
        File fullPath = new File(DATA_FOLDER, backupName + File.separator + Config.DB_NAME);

        SqlLiteAssetExtractor assetExtractor = new SqlLiteAssetExtractor();
        List<URI> uriList = assetExtractor.extractAssets(fullPath);
        Map<URI, String> replacementMap = new HashMap<URI, String>();

        for (URI item : uriList) {
            RemoteFile remoteAsset = new RemoteFile(item.getPath().replace("emulated/0","sdcard0"));
            File localAsset = new File(DATA_FOLDER, backupName + File.separator + "tmpfile");

            try {
                device.pull(remoteAsset, localAsset);
            } catch (IOException | JadbException e) {
                throw new Exception("Failed to pull an asset from the device!", e);
            }

            // The pulled file will be called "tmpfile". Calculate the hash and rename
            // the file properly. (If the target file + extension already exists, delete tmpfile.)
            String checksum = HashingUtils.getChecksumMD5(localAsset);
            String resultName = checksum + '.' + FilenameUtils.getExtension(FilenameUtils.getName(item.getPath()));

            try {
                // Try to rename our tmpfile.
                if (!localAsset.renameTo(new File(DATA_FOLDER, backupName + File.separator + resultName))) {
                    // The target file already exists or something non-fatal. The tmpfile is useless,
                    // but this is actually expected to be common.
                    localAsset.delete();
                }
            } catch (Exception e) {
                // If this fails, something bad happened (security/null pointer exception). The tmpfile
                // is pretty much useless for us at this point, notify someone the backup failed.
                throw new Exception("Failed to prepare files downloaded from the device!");
            }

            // Create a mapping between the extracted URI and the resulting name.
            replacementMap.put(item, resultName);
        }

        // Patch the database to keep the resulting names only, instead of the full file paths.
        assetExtractor.replaceAssetURIs(fullPath, replacementMap);
    }
}
