package pl.gda.pg.eti.autyzm.backupper.api;


import se.vidstige.jadb.JadbDevice;

public interface Restorer {

    void restoreBackupToDevice(String backupName, JadbDevice device);
}




