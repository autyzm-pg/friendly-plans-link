package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.Backup;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.api.BackupperException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Created by superuser on 22-Jun-16.
 */
public class FileBackupper implements Backupper {
    @Override public void makeBackup(String backupName, URI pathToDevice)
        throws BackupperException {

    }

    @Override public List<Backup> getBackups() throws BackupperException {
        return null;
    }

    @Override public Optional<Backup> getBackup(String backupName) throws BackupperException {
        return null;
    }
}
