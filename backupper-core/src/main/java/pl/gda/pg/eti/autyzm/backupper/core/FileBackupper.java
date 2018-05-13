package pl.gda.pg.eti.autyzm.backupper.core;

import org.apache.commons.io.FilenameUtils;
import pl.gda.pg.eti.autyzm.backupper.api.Backup;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.api.BackupperException;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.RemoteFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static pl.gda.pg.eti.autyzm.backupper.core.Config.APPLICATION_PACKAGE_ON_DEVICE;

public class FileBackupper implements Backupper {
    private static final File DATA_FOLDER = new File("data");

    static {
        if (!DATA_FOLDER.exists())
            DATA_FOLDER.mkdir();
    }


    @Override
    public void makeBackup(String backupName, JadbDevice device) throws BackupperException {

        String backupPath = "data/" + backupName + ".ab";
        
        try {       
            Runtime.getRuntime().exec("adb backup -f " + backupPath + " " + APPLICATION_PACKAGE_ON_DEVICE);
        } catch (IOException ex) {
            Logger.getLogger(FileBackupper.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
    }

    @Override
    public List<Backup> getBackups() throws BackupperException {
        return null;
    }

    @Override
    public Optional<Backup> getBackup(String backupName) throws BackupperException {
        return null;
    }

    static File getBackupDatabase(String backupName) {
        return new File(DATA_FOLDER, backupName + File.separator + "commments2.db");
    }


}
