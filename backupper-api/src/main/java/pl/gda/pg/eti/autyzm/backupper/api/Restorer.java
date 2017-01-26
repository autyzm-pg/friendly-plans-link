package pl.gda.pg.eti.autyzm.backupper.api;

import java.io.IOException;

import se.vidstige.jadb.JadbDevice;

public interface Restorer {
    void restoreBackupToDevice(String backupName, JadbDevice device) throws IOException;
}
