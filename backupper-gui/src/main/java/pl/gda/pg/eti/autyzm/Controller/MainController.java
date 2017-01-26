package pl.gda.pg.eti.autyzm.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import pl.gda.pg.eti.autyzm.Info;
import pl.gda.pg.eti.autyzm.Strings;
import pl.gda.pg.eti.autyzm.backupper.core.AdbProxy;

import java.io.IOException;

public class MainController {

    @FXML private BackupController backupController;
    @FXML private RestoreController restoreController;

    static {
        try {
            AdbProxy.initAdbConnection();
        } catch (IOException e) {
            Info.showAlert(Strings.FAILED_TO_INIT_ADB_CONNECTION_TITLE, Strings.FAILED_TO_INIT_ADB_CONNECTION_BODY,
                    Alert.AlertType.ERROR);
        }
    }

}
