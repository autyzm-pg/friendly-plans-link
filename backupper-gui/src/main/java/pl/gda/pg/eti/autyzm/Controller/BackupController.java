package pl.gda.pg.eti.autyzm.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pl.gda.pg.eti.autyzm.Model.Info;
import pl.gda.pg.eti.autyzm.StringConfig;
import pl.gda.pg.eti.autyzm.Model.DeviceTable;
import se.vidstige.jadb.JadbDevice;
import java.util.List;

/**
 * Created by malgorzatas on 30/08/16.
 */
public class BackupController extends BaseController {

    @FXML private TextField nameInput;

    @FXML private TableView<JadbDevice> deviceTableView;
    @FXML private TableColumn<JadbDevice, String> device;
    @FXML private TableColumn<JadbDevice, Boolean> chooseDevice;

    private DeviceTable deviceTable;

    @FXML
    public void initialize() {
        deviceTable = new DeviceTable(deviceTableView, device, chooseDevice, nameInput);
        showConnectedDevices(true);
    }

    @FXML
    public void refreshDeviceList(ActionEvent actionEvent) {
        showConnectedDevices(false);
    }

    private void showConnectedDevices(Boolean appStart) {
        List devices = getConnectedDevices();

        if(devices.isEmpty() && !appStart) {
            Info.showAlert(StringConfig.NO_CONNECTED_DEVICE_ALERT_TITLE, StringConfig.NO_CONNECTED_DEVICE_ALERT_BODY,
                    null, Info.TYPE.WARNING);
        }
        deviceTable.updateDevices(devices);
    }

}
