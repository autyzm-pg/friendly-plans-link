package pl.gda.pg.eti.autyzm;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.gda.pg.eti.autyzm.Tables.CopiesTable;
import pl.gda.pg.eti.autyzm.Tables.DeviceTable;
import pl.gda.pg.eti.autyzm.Tables.DeviceToRefreshTable;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.core.AdbProxy;
import pl.gda.pg.eti.autyzm.backupper.core.FileBackupper;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML private TextField nameInput;

    @FXML private TableView<DeviceCopy> tableView;
    @FXML private TableColumn<DeviceCopy, String> name;
    @FXML private TableColumn<DeviceCopy, String> createDate;
    @FXML private TableColumn<DeviceCopy, Boolean> refreshCopyAction;

    @FXML private TableView<JadbDevice> deviceTableView;
    @FXML private TableColumn<JadbDevice, String> device;
    @FXML private TableColumn<JadbDevice, Boolean> chooseDevice;

    @FXML private TableView<JadbDevice> deviceToRefreshTableView;
    @FXML private TableColumn<JadbDevice, String> deviceToRefresh;
    @FXML private TableColumn<JadbDevice, Boolean> chooseDeviceToRefresh;

    private JadbDevice selectedDeviceToRefresh = null;
    private DeviceTable deviceTable;
    private CopiesTable copiesTable;
    private DeviceToRefreshTable deviceToRefreshTable;

    @FXML
    public void initialize() {
        deviceTable = new DeviceTable(deviceTableView, device, chooseDevice, nameInput);
        copiesTable = new CopiesTable(tableView, name, createDate, refreshCopyAction,selectedDeviceToRefresh);
        deviceToRefreshTable = new DeviceToRefreshTable(deviceToRefreshTableView, deviceToRefresh, chooseDeviceToRefresh, selectedDeviceToRefresh);

        initAdbConnection();
        showConnectedDevices(true);
        showExistingCopies();
    }

    @FXML
    public void refreshDeviceList(ActionEvent actionEvent) {
        showConnectedDevices(false);
    }

    private void showExistingCopies() {
        //Show existing copies
    }

    private void showConnectedDevices(Boolean appStart) {
        List devices = getConnectedDevices();

        if(devices.isEmpty() && !appStart) {
            showAlert(StringConfig.NO_CONNECTED_DEVICE_ALERT_TITLE, StringConfig.NO_CONNECTED_DEVICE_ALERT_BODY,
                    null, Alert.AlertType.WARNING);
        }
        deviceTable.updateDevices(devices);
        deviceToRefreshTable.updateDevices(devices);
    }

    private void initAdbConnection() {
        try {
            AdbProxy.initAdbConnection();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(StringConfig.FAILED_TO_INIT_ADB_CONNECTION_TITLE, StringConfig.FAILED_TO_INIT_ADB_CONNECTION_BODY,
                    null, Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String contentText, String headerText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private List<JadbDevice> getConnectedDevices() {
        try {
            return AdbProxy.getConnectedDevices();
        } catch (IOException | JadbException e) {
            showAlert(StringConfig.LOOKING_FOR_DEVICES_ERROR_TITLE, StringConfig.LOOKING_FOR_DEVICES_ERROR_BODY,
                    null, Alert.AlertType.ERROR);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
