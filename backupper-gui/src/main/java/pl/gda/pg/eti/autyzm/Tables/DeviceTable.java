package pl.gda.pg.eti.autyzm.Tables;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import pl.gda.pg.eti.autyzm.Config;
import pl.gda.pg.eti.autyzm.StringConfig;
import pl.gda.pg.eti.autyzm.backupper.api.Backupper;
import pl.gda.pg.eti.autyzm.backupper.core.FileBackupper;
import se.vidstige.jadb.JadbDevice;

import java.util.List;

/**
 * Created by malgorzatas on 26/08/16.
 */
public class DeviceTable extends Table {

    private TextField nameInput;
    private TableView<JadbDevice> deviceTableView;
    private TableColumn<JadbDevice, String> device;
    private TableColumn<JadbDevice, Boolean> chooseDevice;
    private ObservableList<JadbDevice> availableDevices = FXCollections.observableArrayList();
    private Backupper backupper = new FileBackupper();

    public DeviceTable(TableView<JadbDevice> deviceTableView, TableColumn<JadbDevice, String> device, TableColumn<JadbDevice, Boolean> chooseDevice, TextField nameInput){
        this.deviceTableView = deviceTableView;
        this.device = device;
        this.chooseDevice = chooseDevice;
        this.nameInput = nameInput;
        setDataBindings();
        setColumnWidth();
        deviceTableView.setItems(this.availableDevices);
    }

    public void updateDevices(List devices){
        availableDevices.clear();
        availableDevices.addAll(devices);
    }

    @Override
    void setDataBindings() {
        device.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSerial()));
        chooseDevice.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        chooseDevice.setCellFactory(
                cellData -> new MakeCopyButtonCell());
    }

    @Override
    void setColumnWidth() {
        deviceTableView.setPrefWidth(Config.SCENE_WIDTH);
        device.prefWidthProperty().bind(deviceTableView.widthProperty().multiply(0.7));
        chooseDevice.prefWidthProperty().bind(deviceTableView.widthProperty().multiply(0.3));
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
