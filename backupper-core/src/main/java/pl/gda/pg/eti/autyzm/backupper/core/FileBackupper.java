package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.Backup;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.api.BackupperException;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.RemoteFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class FileBackupper implements Backupper {
    private static final File DATA_FOLDER = new File("data");

    {
        if (!DATA_FOLDER.exists())
            DATA_FOLDER.mkdir();
    }

    private final JadbConnection adbConnection;

    public FileBackupper() {
        try {
            this.adbConnection = new JadbConnection();
        } catch (IOException e) {
            throw new BackupperException("Failed to initialize ADB connection to localhost", e);
        }
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

    /**
     * Creates new backup folder in {@link FileBackupper#DATA_FOLDER}.
     * @param backupName
     * @throws IOException
     */
    private void createBackupFolder(String backupName) throws IOException {
        new File(DATA_FOLDER, backupName).mkdir();
    }


    /**
     * Retrieves a list of connection ADB devices
     * @return
     * @throws IOException
     * @throws JadbException
     */
    private List<JadbDevice> getConnectedDevices() throws IOException, JadbException {
        return adbConnection.getDevices();
    }

    /**
     * Copies file from specified device via ADB
     *
     * @param device adb-enabled device to copy from
     * @param copyFrom source file patch
     * @param backupName destination backup name
     * @throws IOException if method fails to copy file
     */
    private void copyFromDevice(JadbDevice device, String copyFrom, String backupName) throws IOException {
        try {
            String copyFromFileName = Paths.get(new URI(copyFrom).getPath()).getFileName().toString();
            File fullPath = new File(DATA_FOLDER, backupName + File.separator + copyFromFileName);
            device.pull(new RemoteFile(copyFrom), fullPath);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Bad copyFrom path", e);
        } catch (JadbException e) {
            throw new IOException("Failed to pull file from device", e);
        }
    }
}
