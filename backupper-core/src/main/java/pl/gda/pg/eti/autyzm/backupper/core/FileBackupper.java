package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.Backup;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.api.BackupperException;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.io.*;
/**
 * Created by superuser on 22-Jun-16.
 */
public class FileBackupper implements Backupper {

    private static final File DATA_FOLDER = new File("data");
    private static final int BUFF_SIZE = 4096;

    @Override public void makeBackup(String backupName, URI pathToDevice)
        throws BackupperException {
    }

    @Override public List<Backup> getBackups() throws BackupperException {
        return null;
    }

    @Override public Optional<Backup> getBackup(String backupName) throws BackupperException {
        return null;
    }
    private static void createFolder(String backUpName) throws IOException {
        if (!DATA_FOLDER.exists()) {
            DATA_FOLDER.mkdir();
        }
        File fullPath = new File("data" + File.separator + backUpName);
        if (!fullPath.exists()) {
            fullPath.mkdir();
        }
    }

    public static void copyFromDevice(String copyFrom, String backUpName) throws IOException {
        File sourceFile = new File(copyFrom);
        if (!sourceFile.exists()) {
            throw new IllegalArgumentException("File not found " + copyFrom);
        }
        else if (!sourceFile.isFile()){
            throw new IllegalArgumentException( copyFrom + " is not a file.");
        }

        File fullPath = new File("data" + File.separator + backUpName + File.separator + sourceFile.getName());

        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(sourceFile);
            output = new FileOutputStream(fullPath);

            byte[] buf = new byte[BUFF_SIZE];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}
