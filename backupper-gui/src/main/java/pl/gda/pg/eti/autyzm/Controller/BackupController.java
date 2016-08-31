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
import pl.gda.pg.eti.autyzm.StringConfig;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.core.FileBackupper;
import se.vidstige.jadb.JadbDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malgorzatas on 30/08/16.
 */
public class BackupController extends BaseController {

    private static final double DEVICE_COLUMN_WIDTH = 0.7;
    private static final double MAKE_COPY_COLUMN_WIDTH = 0.3;

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

    public void updateDevices(List devices){
        this.devices.clear();
        this.devices.addAll(devices);
        this.deviceTableView.refresh();
    }

    @FXML
    public void refreshDeviceList(ActionEvent actionEvent) {
        showConnectedDevices(false);
    }

    private void showConnectedDevices(Boolean appStart) {
        List devices = getConnectedDevices();

        if(devices.isEmpty() && !appStart) {
            Info.showAlert(StringConfig.NO_CONNECTED_DEVICE_ALERT_TITLE, StringConfig.NO_CONNECTED_DEVICE_ALERT_BODY,
                    null, Alert.AlertType.WARNING);
        }
        updateDevices(devices);
    }

    private void setDataBindings() {
        deviceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSerial()));
        backupColumn.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        backupColumn.setCellFactory(
                cellData -> new MakeCopyButtonCell());
    }

    private void setColumnWidth() {
        deviceTableView.setPrefWidth(Config.SCENE_WIDTH);
        deviceColumn.prefWidthProperty().bind(deviceTableView.widthProperty().multiply(DEVICE_COLUMN_WIDTH));
        backupColumn.prefWidthProperty().bind(deviceTableView.widthProperty().multiply(MAKE_COPY_COLUMN_WIDTH));
    }

    private class MakeCopyButtonCell extends TableCell<JadbDevice, Boolean> {
        final Button makeCopyButtonCell = new Button(StringConfig.MAKE_COPY);

        MakeCopyButtonCell(){

            makeCopyButtonCell.setOnAction(action -> {
                try{
                    if(!nameInput.getText().isEmpty()) {

                        // TODO Check if copy already exists
                        JadbDevice selectedDevice = devices.get(this.getIndex());
                        backupper.makeBackup(nameInput.getText(), selectedDevice);

                        Info.showAlert(StringConfig.COPY_CREATED_ALERT_TITLE, StringConfig.COPY_CREATED_ALERT_BODY,
                             null, Alert.AlertType.INFORMATION);
                    }
                    else{
                        Info.showAlert(StringConfig.MISSING_FIELDS_ALERT_TITLE, StringConfig.MISSING_FIELDS_ALERT_BODY,
                                null, Alert.AlertType.WARNING);
                    }

                }
                catch (Exception ex){
                     Info.showAlert(StringConfig.NO_CONNECTED_DEVICE_ALERT_TITLE, StringConfig.NO_CONNECTED_DEVICE_ALERT_BODY,
                             null, Alert.AlertType.ERROR);
                     updateDevices(new ArrayList());
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
