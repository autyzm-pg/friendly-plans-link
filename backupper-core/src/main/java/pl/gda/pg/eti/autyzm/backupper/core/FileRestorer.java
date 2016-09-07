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
    public void restoreBackupToDevice(String backupName, JadbDevice device) {
        try {
            device.push(FileBackupper.getBackupDatabase(backupName), new RemoteFile(Config.PATH_TO_DB));
            restoreAssetsToDevice(backupName, device);
        } catch (IOException | JadbException e) {
            e.printStackTrace();
        }
    }

    private void restoreAssetsToDevice(String backupName, JadbDevice device) throws IOException {
        File fullPathToDB = new File(DATA_FOLDER, backupName + File.separator + Config.DB_NAME);


        SqlLiteAssertExtractor assertExtractor = new SqlLiteAssertExtractor();
        List<URI> uriList = assertExtractor.extractAsserts(fullPathToDB);
        for (URI item : uriList) {

            String assetName = FilenameUtils.getName(item.getPath());
            RemoteFile assetOnAndroid = new RemoteFile(item.getPath().replace("emulated/0", "sdcard0"));

            File pathToAssetOnPC = new File(DATA_FOLDER, backupName + File.separator + assetName);

            try {
                device.push(pathToAssetOnPC, assetOnAndroid);
            } catch (IOException | JadbException e) {
                throw new IOException("Failed to push assets on device", e);
            }
        }
    }
}
