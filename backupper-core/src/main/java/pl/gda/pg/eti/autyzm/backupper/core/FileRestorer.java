package pl.gda.pg.eti.autyzm.backupper.core;

import org.apache.commons.io.FilenameUtils;
import pl.gda.pg.eti.autyzm.backupper.api.Restorer;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.RemoteFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static pl.gda.pg.eti.autyzm.backupper.core.Config.APPLICATION_PACKAGE_ON_DEVICE;

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
