package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.Restorer;
import se.vidstige.jadb.JadbDevice;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static pl.gda.pg.eti.autyzm.backupper.core.Config.BACKUP_DIRECTORY;

public class FileRestorer implements Restorer {


    @Override
    public void restoreBackupToDevice(String backupName, JadbDevice device) throws IOException {

        String backupPath = BACKUP_DIRECTORY + "/" + backupName;
        
        try {       
            Runtime.getRuntime().exec("adb restore " + backupPath);
        } catch (IOException ex) {
            Logger.getLogger(FileBackupper.class.getName()).log(Level.SEVERE, null, ex);
        }  

    }

}
