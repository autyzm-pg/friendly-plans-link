package pl.gda.pg.eti.autyzm.backupper.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Created by superuser on 22-Jun-16.
 */
public interface Backupper {
    void makeBackup(String backupName, URI pathToDevice) throws BackupperException;
    List<Backup> getBackups() throws BackupperException;
    Optional<Backup> getBackup(String backupName) throws BackupperException;
}
