package pl.gda.pg.eti.autyzm.backupper.core;

import pl.gda.pg.eti.autyzm.backupper.api.Restorer;
import se.vidstige.jadb.JadbDevice;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileRestorer implements Restorer {
    private static final File DATA_FOLDER = new File("data");

    @Override
    public void restoreBackupToDevice(String backupName, JadbDevice device) throws IOException {

        String backupPath = "data/" + backupName;
        
        try {       
            Runtime.getRuntime().exec("adb restore " + backupPath);
        } catch (IOException ex) {
            Logger.getLogger(FileBackupper.class.getName()).log(Level.SEVERE, null, ex);
        }  

    }

}
