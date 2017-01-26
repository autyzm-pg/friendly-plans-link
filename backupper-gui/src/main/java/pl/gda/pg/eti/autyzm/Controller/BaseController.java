package pl.gda.pg.eti.autyzm.Controller;

import javafx.scene.control.Alert;
import pl.gda.pg.eti.autyzm.Info;
import pl.gda.pg.eti.autyzm.Strings;
import pl.gda.pg.eti.autyzm.backupper.core.AdbProxy;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract class BaseController {
    List<JadbDevice> getConnectedDevices() {
        try {
            return AdbProxy.getConnectedDevices();
        } catch (IOException | JadbException e) {
            Info.showAlert(Strings.LOOKING_FOR_DEVICES_ERROR_TITLE, Strings.LOOKING_FOR_DEVICES_ERROR_BODY,
                    Alert.AlertType.ERROR);
            return new ArrayList<>();
        }
    }
}
