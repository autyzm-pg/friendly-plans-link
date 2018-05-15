package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.Backup;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.api.BackupperException;
import se.vidstige.jadb.JadbDevice;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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

        backupName = backupName.replace(" ", "_");
        LocalDate todayDate = LocalDate.now();
        String backupPath = "data/" + todayDate + "_" + backupName + ".ab";
        String backupCommand = "adb backup -f " + backupPath + " " + APPLICATION_PACKAGE_ON_DEVICE;
        
        try {       
            Runtime.getRuntime().exec(backupCommand);
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
