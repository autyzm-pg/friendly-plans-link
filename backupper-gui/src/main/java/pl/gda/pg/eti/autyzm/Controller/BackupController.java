package pl.gda.pg.eti.autyzm.Controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.gda.pg.eti.autyzm.Config;
import pl.gda.pg.eti.autyzm.Info;
import pl.gda.pg.eti.autyzm.Strings;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.core.FileBackupper;
import se.vidstige.jadb.JadbDevice;

import java.util.ArrayList;
import java.util.List;


public class BackupController extends BaseController {

    private static final double DEVICE_COLUMN_WIDTH = 0.7;
    private static final double BACKUP_COLUMN_WIDTH = 0.3;

    @FXML private TextField nameInput;
    @FXML private TableView<JadbDevice> deviceTableView;
    @FXML private TableColumn<JadbDevice, String> deviceColumn;
    @FXML private TableColumn<JadbDevice, Boolean> backupColumn;

    private ObservableList<JadbDevice> devices = FXCollections.observableArrayList();
    private Backupper backupper = new FileBackupper();

    @FXML
    public void initialize() {
        setDataBindings();
        setColumnWidth();
        deviceTableView.setItems(this.devices);
        showConnectedDevices(true);
    }

    private void updateDevices(List<JadbDevice> devices) {
        this.devices.clear();
        if (devices != null) this.devices.addAll(devices);
        this.deviceTableView.refresh();
    }

    @FXML
    public void refreshDeviceList(ActionEvent actionEvent) {
        showConnectedDevices(false);
    }

    private void showConnectedDevices(Boolean appStart) {
        List<JadbDevice> devices = getConnectedDevices();

        if (devices.isEmpty() && !appStart) {
            Info.showAlert(Strings.NO_CONNECTED_DEVICE_ALERT_TITLE, Strings.NO_CONNECTED_DEVICE_ALERT_BODY,
                    Alert.AlertType.WARNING);
        }

        updateDevices(devices);
    }

    private void setDataBindings() {
        deviceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSerial()));
        backupColumn.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        backupColumn.setCellFactory(
                cellData -> new BackupButtonCell());
    }

    private void setColumnWidth() {
        deviceTableView.setPrefWidth(Config.SCENE_WIDTH);
        deviceColumn.prefWidthProperty().bind(deviceTableView.widthProperty().multiply(DEVICE_COLUMN_WIDTH));
        backupColumn.prefWidthProperty().bind(deviceTableView.widthProperty().multiply(BACKUP_COLUMN_WIDTH));
    }

    private class BackupButtonCell extends TableCell<JadbDevice, Boolean> {
        final Button backupButtonCell = new Button(Strings.MAKE_BACKUP);

        BackupButtonCell() {
            backupButtonCell.setOnAction(action -> {
                JadbDevice selectedDevice;

                try {
                    selectedDevice = devices.get(this.getIndex());
                } catch (Exception e) {
                    Info.showAlert(
                            Strings.NO_CONNECTED_DEVICE_ALERT_TITLE,
                            Strings.NO_CONNECTED_DEVICE_ALERT_BODY,
                            Alert.AlertType.WARNING
                    );

                    updateDevices(new ArrayList<>());
                    return;
                }

                if (nameInput.getText().isEmpty()) {
                    Info.showAlert(
                            Strings.BACKUP_NAME_MISSING_TITLE,
                            Strings.BACKUP_NAME_MISSING_BODY,
                            Alert.AlertType.WARNING
                    );

                    return;
                }

                try {
                    // TODO: Check if copy already exists
                    backupper.makeBackup(nameInput.getText(), selectedDevice);

                    Info.showAlert(
                            Strings.BACKUP_CREATED_ALERT_TITLE,
                            Strings.BACKUP_CREATED_ALERT_BODY,
                            Alert.AlertType.INFORMATION
                    );
                } catch (Exception ex) {
                    Info.showAlert(
                            Strings.BACKUP_FAILED_ALERT_TITLE,
                            Strings.BACKUP_FAILED_ALERT_BODY,
                            Alert.AlertType.ERROR
                    );
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(backupButtonCell);
            }
        }
    }

}
