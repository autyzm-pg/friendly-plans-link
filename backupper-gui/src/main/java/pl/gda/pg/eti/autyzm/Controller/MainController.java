package pl.gda.pg.eti.autyzm.Controller;

import javafx.fxml.FXML;
import pl.gda.pg.eti.autyzm.Model.Info;
import pl.gda.pg.eti.autyzm.StringConfig;
import pl.gda.pg.eti.autyzm.backupper.core.AdbProxy;

import java.io.IOException;

public class MainController {

    @FXML private BackupController backupController;
    @FXML private RestoreController restoreController;

    static {
        try {
            AdbProxy.initAdbConnection();
        } catch (IOException e) {
            e.printStackTrace();
            Info.showAlert(StringConfig.FAILED_TO_INIT_ADB_CONNECTION_TITLE, StringConfig.FAILED_TO_INIT_ADB_CONNECTION_BODY,
                    null, Info.TYPE.ERROR);
        }
    }

    @FXML
    public void initialize() {
    }


}
