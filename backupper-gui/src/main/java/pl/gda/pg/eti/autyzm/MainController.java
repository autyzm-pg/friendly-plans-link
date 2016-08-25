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

    @FXML private BorderPane root;

    @FXML private TextField nameInput;

    @FXML public Label directoryRefreshLabel;
    private String directoryRefreshPath = null;

    @FXML private TableView<DeviceCopy> tableView;
    @FXML private TableColumn<DeviceCopy, String> name;
    @FXML private TableColumn<DeviceCopy, String> createDate;
    @FXML private TableColumn<DeviceCopy, Boolean> refreshCopyAction;

    @FXML private TableView<JadbDevice> deviceTableView;
    @FXML private TableColumn<JadbDevice, String> device;
    @FXML private TableColumn<JadbDevice, Boolean> chooseDevice;

    private ObservableList<DeviceCopy> deviceCopyData = FXCollections.observableArrayList();
    private ObservableList<JadbDevice> availableDevices = FXCollections.observableArrayList();

    private Backupper backupper = new FileBackupper();

    @FXML
    public void initialize() {
        setDeviceTableColumnDataBindings();
        setTableColumnDataBindings();
        setTableColumnWidth();
        setDeviceTableColumnWidth();
        tableView.setItems(deviceCopyData);
        deviceTableView.setItems(availableDevices);

        initAdbConnection();
        showConnectedDevices();
    }

    @FXML
    public void refreshDeviceList(ActionEvent actionEvent) {
        showConnectedDevices();
    }

    private void showConnectedDevices() {
        List devices = getConnectedDevices();

        if(!devices.isEmpty()) {
              availableDevices.clear();
              availableDevices.addAll(devices);
        } else {
            showAlert(StringConfig.NO_CONNECTED_DEVICE_ALERT_TITLE, StringConfig.NO_CONNECTED_DEVICE_ALERT_BODY,
                    null, Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void chooseDeviceToRefresh(ActionEvent actionEvent) {
        File chosenDirectory = showDirectoryChooserDialog(StringConfig.CHOOSE_DEVICE_TO_REFRESH);
        if(chosenDirectory != null) {
            directoryRefreshPath = chosenDirectory.getAbsolutePath();
            directoryRefreshLabel.setText(chosenDirectory.getAbsolutePath());
        }
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

    private void setDeviceTableColumnDataBindings(){
        device.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSerial()));
        chooseDevice.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        chooseDevice.setCellFactory(
                cellData -> new MakeCopyButtonCell());
    }
    private void setTableColumnDataBindings() {
        name.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        createDate.setCellValueFactory(cellData -> cellData.getValue().getCreateDateProperty());
        refreshCopyAction.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        refreshCopyAction.setCellFactory(
                cellData -> new RefreshButtonCell());
    }
    private void setTableColumnWidth() {
        name.prefWidthProperty().bind(tableView.widthProperty().multiply(0.5));
        createDate.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
        refreshCopyAction.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
    }
    private void setDeviceTableColumnWidth() {
        device.prefWidthProperty().bind(tableView.widthProperty().multiply(0.5));
        chooseDevice.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
    }

    private void showAlert(String title, String contentText, String headerText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private File showDirectoryChooserDialog(String dialogTitle) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(dialogTitle);
        Stage stage = (Stage) root.getScene().getWindow();
        return directoryChooser.showDialog(stage);
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

    private class RefreshButtonCell extends TableCell<DeviceCopy, Boolean> {
        final Button refreshButton = new Button(StringConfig.REFRESH_BUTTON);

        RefreshButtonCell(){

            refreshButton.setOnAction(action -> {
                if(directoryRefreshPath != null) {
                    showAlert(StringConfig.COPY_REFRESHED_ALERT_TITLE, StringConfig.COPY_REFRESHED_ALERT_BODY,
                            null, Alert.AlertType.INFORMATION);

                    //refresh
                }
                else{
                    showAlert(StringConfig.MISSING_FIELDS_ALERT_TITLE, StringConfig.MISSING_FIELDS_ALERT_BODY,
                            null, Alert.AlertType.WARNING);
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(refreshButton);
            }
        }
    }

    private class MakeCopyButtonCell extends TableCell<JadbDevice, Boolean> {
        final Button makeCopyButtonCell = new Button(StringConfig.MAKE_COPY);

        MakeCopyButtonCell(){

            makeCopyButtonCell.setOnAction(action -> {
                if(!nameInput.getText().isEmpty()) {
                    // TODO Check if copy already exists
                    JadbDevice selectedDevice = availableDevices.get(this.getIndex());
                    backupper.makeBackup(nameInput.getText(), selectedDevice);
                    showAlert(StringConfig.COPY_CREATED_ALERT_TITLE, StringConfig.COPY_CREATED_ALERT_BODY,
                            null, Alert.AlertType.INFORMATION);
                }
                else{
                    showAlert(StringConfig.MISSING_FIELDS_ALERT_TITLE, StringConfig.MISSING_FIELDS_ALERT_BODY,
                            null, Alert.AlertType.WARNING);
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(makeCopyButtonCell);
            }
        }
    }

}
