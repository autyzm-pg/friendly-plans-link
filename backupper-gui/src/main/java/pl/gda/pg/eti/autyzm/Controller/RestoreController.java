package pl.gda.pg.eti.autyzm.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.gda.pg.eti.autyzm.DeviceCopy;
import pl.gda.pg.eti.autyzm.Model.Info;
import pl.gda.pg.eti.autyzm.StringConfig;
import pl.gda.pg.eti.autyzm.Model.CopiesTable;
import pl.gda.pg.eti.autyzm.Model.DeviceToRefreshTable;
import se.vidstige.jadb.JadbDevice;
import java.util.List;

/**
 * Created by malgorzatas on 30/08/16.
 */
public class RestoreController extends BaseController {

    @FXML private TableView<DeviceCopy> tableView;
    @FXML private TableColumn<DeviceCopy, String> name;
    @FXML private TableColumn<DeviceCopy, String> createDate;
    @FXML private TableColumn<DeviceCopy, Boolean> refreshCopyAction;

    @FXML private TableView<JadbDevice> deviceToRefreshTableView;
    @FXML private TableColumn<JadbDevice, String> deviceToRefresh;
    @FXML private TableColumn<JadbDevice, Boolean> chooseDeviceToRefresh;

    private JadbDevice selectedDeviceToRefresh = null;
    private CopiesTable copiesTable;
    private DeviceToRefreshTable deviceToRefreshTable;

    @FXML
    public void initialize() {
        copiesTable = new CopiesTable(tableView, name, createDate, refreshCopyAction,selectedDeviceToRefresh);
        deviceToRefreshTable = new DeviceToRefreshTable(deviceToRefreshTableView, deviceToRefresh, chooseDeviceToRefresh, selectedDeviceToRefresh);

        showConnectedDevices(true);
        showExistingCopies();
    }

    @FXML
    public void refreshDeviceList(ActionEvent actionEvent) {
        showConnectedDevices(false);
    }

    private void showExistingCopies() {
        //TODO Show existing copies
    }

    private void showConnectedDevices(Boolean appStart) {
        List devices = getConnectedDevices();

        if(devices.isEmpty() && !appStart) {
            Info.showAlert(StringConfig.NO_CONNECTED_DEVICE_ALERT_TITLE, StringConfig.NO_CONNECTED_DEVICE_ALERT_BODY,
                    null, Info.TYPE.WARNING);
        }
        deviceToRefreshTable.updateDevices(devices);
    }


}
