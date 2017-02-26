package pl.gda.pg.eti.autyzm.Controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.gda.pg.eti.autyzm.Config;
import pl.gda.pg.eti.autyzm.DeviceCopy;
import pl.gda.pg.eti.autyzm.Info;
import pl.gda.pg.eti.autyzm.Strings;
import pl.gda.pg.eti.autyzm.backupper.api.Restorer;
import pl.gda.pg.eti.autyzm.backupper.core.FileRestorer;
import se.vidstige.jadb.JadbDevice;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class RestoreController extends BaseController {

    private static final double CREATE_DATE_COLUMN_WIDTH = 0.3;
    private static final double RESTORE_COPY_COLUMN_WIDTH = 0.2;
    private static final double NAME_COLUMN_WIDTH = 0.5;

    private static final double DEVICE_NAME_COLUMN_WIDTH = 0.9;
    private static final double CHOOSE_DEVICE_COLUMN_WIDTH = 0.1;
    private static final double MAX_TABLE_HEIGHT = 100.0;

    private static final String CURRENT_DIRECTORY_NAME = "data";

    @FXML private TableView<DeviceCopy> copiesTableView;
    @FXML private TableColumn<DeviceCopy, String> copyNameColumn;
    @FXML private TableColumn<DeviceCopy, String> copyCreateDateColumn;
    @FXML private TableColumn<DeviceCopy, Boolean> restoreCopyColumn;

    @FXML private TableView<JadbDevice> devicesTableView;
    @FXML private TableColumn<JadbDevice, String> deviceNameColumn;
    @FXML private TableColumn<JadbDevice, Boolean> chooseDeviceColumn;

    private JadbDevice selectedDevice = null;
    private ObservableList<JadbDevice> devices = FXCollections.observableArrayList();
    private ObservableList<DeviceCopy> copies = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        setDataBindings();
        setColumnWidth();

        populate();

        copiesTableView.setItems(copies);
        devicesTableView.setItems(devices);

        showConnectedDevices(true);
    }

    @FXML
    public void refreshView(ActionEvent actionEvent) {
        showConnectedDevices(false);
        populate();
    }

    private void showConnectedDevices(Boolean appStart) {
        List<JadbDevice> connectedDevices = getConnectedDevices();
        devices.clear();

        if (connectedDevices.isEmpty() && !appStart) {
            Info.showAlert(Strings.NO_CONNECTED_DEVICE_ALERT_TITLE, Strings.NO_CONNECTED_DEVICE_ALERT_BODY,
                    Alert.AlertType.WARNING);
        } else devices.addAll(connectedDevices);

        devicesTableView.refresh();
    }

    private void setDataBindings() {
        copyNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        copyCreateDateColumn.setCellValueFactory(cellData -> cellData.getValue().getCreateDateProperty());
        restoreCopyColumn.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        restoreCopyColumn.setCellFactory(
                cellData -> new RestoreButtonCell());
        deviceNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSerial()));
        chooseDeviceColumn.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        chooseDeviceColumn.setCellFactory(
                cellData -> new ChooseDeviceRadioBoxCell());
    }

    private void setColumnWidth() {
        copyNameColumn.prefWidthProperty().bind(copiesTableView.widthProperty().multiply(NAME_COLUMN_WIDTH));
        copyCreateDateColumn.prefWidthProperty().bind(copiesTableView.widthProperty().multiply(CREATE_DATE_COLUMN_WIDTH));
        restoreCopyColumn.prefWidthProperty().bind(copiesTableView.widthProperty().multiply(RESTORE_COPY_COLUMN_WIDTH));

        devicesTableView.setPrefWidth(Config.SCENE_WIDTH);
        deviceNameColumn.prefWidthProperty().bind(devicesTableView.widthProperty().multiply(DEVICE_NAME_COLUMN_WIDTH));
        chooseDeviceColumn.prefWidthProperty().bind(devicesTableView.widthProperty().multiply(CHOOSE_DEVICE_COLUMN_WIDTH));
        devicesTableView.setMaxHeight(MAX_TABLE_HEIGHT);
    }

    private void populate() {
        Path pathToDataDirectory = Paths.get(".", CURRENT_DIRECTORY_NAME);
        File dataDirectory = new File(pathToDataDirectory.toString());

        // Filter out directories only, per http://stackoverflow.com/a/5125258/1044061
        String[] copiesNames = dataDirectory.list((current, name) -> new File(current, name).isDirectory());
        copies.clear();

        if (copiesNames != null) {
            for (String copyName : copiesNames) {
                copies.add(new DeviceCopy(copyName, null));
            }
        }

        copiesTableView.refresh();
    }

    private class RestoreButtonCell extends TableCell<DeviceCopy, Boolean> {
        final Button restoreButton = new Button(Strings.RESTORE_BUTTON);

        RestoreButtonCell() {
            restoreButton.setOnAction(action -> {
                if (selectedDevice == null) {
                    Info.showAlert(
                            Strings.DEVICE_NOT_SELECTED_ALERT_TITLE,
                            Strings.DEVICE_NOT_SELECTED_ALERT_BODY,
                            Alert.AlertType.WARNING
                    );

                    return;
                }

                String backupName = copiesTableView.getItems().get(this.getIndex()).getName();
                Restorer restorer = new FileRestorer();

                try {
                    restorer.restoreBackupToDevice(backupName, selectedDevice);
                } catch (IOException e) {
                    Info.showAlert(
                            Strings.COPY_RESTORATION_FAILED_TITLE,
                            Strings.COPY_RESTORATION_FAILED_BODY + e.getMessage(),
                            Alert.AlertType.ERROR
                    );

                    return;
                }

                Info.showAlert(
                        Strings.COPY_RESTORED_ALERT_TITLE,
                        Strings.COPY_RESTORED_ALERT_BODY,
                        Alert.AlertType.INFORMATION
                );
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(restoreButton);
            }
        }
    }

    private class ChooseDeviceRadioBoxCell extends TableCell<JadbDevice, Boolean> {
        final RadioButton chooseDeviceRadioBox = new RadioButton();

        ChooseDeviceRadioBoxCell(){
            chooseDeviceRadioBox.setOnAction(action ->
                selectedDevice = devices.get(this.getIndex())
            );
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(chooseDeviceRadioBox);
            }
        }
    }


}
