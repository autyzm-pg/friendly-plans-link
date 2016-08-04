package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.Backup;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.api.BackupperException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public class FileBackupper implements Backupper {
    private static final File DATA_FOLDER = new File("data");
    private static final int BUFF_SIZE = 4096;

    {
        if (!DATA_FOLDER.exists())
            DATA_FOLDER.mkdir();
    }

    @Override
    public void makeBackup(String backupName, URI pathToDevice) throws BackupperException {
    }

    @Override
    public List<Backup> getBackups() throws BackupperException {
        return null;
    }

    @Override
    public Optional<Backup> getBackup(String backupName) throws BackupperException {
        return null;
    }

    private void createBackupFolder(String backupName) throws IOException {
        File fullPath = new File(DATA_FOLDER, backupName);

        if (!fullPath.exists())
            fullPath.mkdir();
    }

    public void copyFromDevice(String copyFrom, String backupName) throws IOException {
        File sourceFile = new File(copyFrom);

        if (!sourceFile.exists()) {
            throw new IllegalArgumentException("File not found " + copyFrom);
        } else if (!sourceFile.isFile()) {
            throw new IllegalArgumentException(copyFrom + " is not a file.");
        }

        File fullPath = new File(DATA_FOLDER, backupName + File.separator + sourceFile.getName());

        try (InputStream input = new FileInputStream(sourceFile)) {
            try (OutputStream output = new FileOutputStream(fullPath)) {
                byte[] buf = new byte[BUFF_SIZE];
                int bytesRead;
                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }
            }
        }
    }
}
