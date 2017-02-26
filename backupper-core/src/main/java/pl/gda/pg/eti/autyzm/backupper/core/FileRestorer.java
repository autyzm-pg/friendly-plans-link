package pl.gda.pg.eti.autyzm.backupper.core;

import org.apache.commons.io.FilenameUtils;
import pl.gda.pg.eti.autyzm.backupper.api.Restorer;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.RemoteFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class FileRestorer implements Restorer {
    private static final File DATA_FOLDER = new File("data");

    @Override
    public void restoreBackupToDevice(String backupName, JadbDevice device) throws IOException {
        try {
            device.push(FileBackupper.getBackupDatabase(backupName), new RemoteFile(Config.PATH_TO_DB));
        } catch (IOException | JadbException e) {
            throw new IOException("Failed to restore the app database to the device.", e);
        }

        // If the database restored successfully, we can now try restoring the assets!
        // Any exceptions thrown in this method should be sent to the controller, NOT handled here.
        restoreAssetsToDevice(backupName, device);
    }

    private void restoreAssetsToDevice(String backupName, JadbDevice device) throws IOException {
        File fullPathToDB = new File(DATA_FOLDER, backupName + File.separator + Config.DB_NAME);

        SqlLiteAssetExtractor assetExtractor = new SqlLiteAssetExtractor();
        List<URI> uriList = assetExtractor.extractAssets(fullPathToDB);

        for (URI item : uriList) {
            String assetName = FilenameUtils.getName(item.getPath());
            RemoteFile assetOnAndroid = new RemoteFile(item.getPath().replace("emulated/0", "sdcard0"));
            File pathToAssetOnPC = new File(DATA_FOLDER, backupName + File.separator + assetName);

            try {
                device.push(pathToAssetOnPC, assetOnAndroid);
            } catch (IOException | JadbException e) {
                throw new IOException("Failed to push assets to the device. (File: " + pathToAssetOnPC.getName() + ")", e);
            }
        }
    }
}
