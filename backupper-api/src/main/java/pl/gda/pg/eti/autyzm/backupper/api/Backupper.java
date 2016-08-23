package pl.gda.pg.eti.autyzm.backupper.api;

import se.vidstige.jadb.JadbDevice;

import java.util.List;
import java.util.Optional;

public interface Backupper {
    void makeBackup(String backupName, JadbDevice device) throws BackupperException;
    List<Backup> getBackups() throws BackupperException;
    Optional<Backup> getBackup(String backupName) throws BackupperException;
}
