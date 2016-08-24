package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.Restorer;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.RemoteFile;

import java.io.IOException;

public class FileRestorer implements Restorer {

    private final String DB_NAME = "comments2.db";

    @Override
    public void restoreBackupToDevice(String backupName, JadbDevice device) {
        try {
            device.push(FileBackupper.getBackupDatabase(backupName), new RemoteFile(Config.PATH_TO_DB));
        } catch (IOException | JadbException e) {
            e.printStackTrace();
        }
    }
}
