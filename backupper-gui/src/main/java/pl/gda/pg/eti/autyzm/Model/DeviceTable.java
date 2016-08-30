package pl.gda.pg.eti.autyzm.Model;

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

import java.util.ArrayList;
import java.util.List;

public class DeviceTable extends Table {

    private static final double DEVICE_COLUMN_WIDTH = 0.7;
    private static final double MAKE_COPY_COLUMN_WIDTH = 0.3;
    private TextField nameInput;

    private TableView<JadbDevice> tableView;
    private TableColumn<JadbDevice, String> deviceColumn;
    private TableColumn<JadbDevice, Boolean> makeCopyColumn;
    private ObservableList<JadbDevice> devices = FXCollections.observableArrayList();

    private Backupper backupper = new FileBackupper();

    public DeviceTable(TableView<JadbDevice> tableView, TableColumn<JadbDevice, String> deviceColumn, TableColumn<JadbDevice, Boolean> makeCopyColumn, TextField nameInput){
        this.tableView = tableView;
        this.deviceColumn = deviceColumn;
        this.makeCopyColumn = makeCopyColumn;

        // I probably should not check name input field in controller is set this way
        this.nameInput = nameInput;

        setDataBindings();
        setColumnWidth();
        tableView.setItems(this.devices);
    }

    public void updateDevices(List devices){
        this.devices.clear();
        this.devices.addAll(devices);
        tableView.refresh();
    }

    @Override
    void setDataBindings() {
        deviceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSerial()));
        makeCopyColumn.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        makeCopyColumn.setCellFactory(
                cellData -> new MakeCopyButtonCell());
    }

    @Override
    void setColumnWidth() {
        tableView.setPrefWidth(Config.SCENE_WIDTH);
        deviceColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(DEVICE_COLUMN_WIDTH));
        makeCopyColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(MAKE_COPY_COLUMN_WIDTH));
    }

    private class MakeCopyButtonCell extends TableCell<JadbDevice, Boolean> {
        final Button makeCopyButtonCell = new Button(StringConfig.MAKE_COPY);

        MakeCopyButtonCell(){

            makeCopyButtonCell.setOnAction(action -> {
                if(!nameInput.getText().isEmpty()) {
                    try{
                    // TODO Check if copy already exists
                    JadbDevice selectedDevice = devices.get(this.getIndex());
                    backupper.makeBackup(nameInput.getText(), selectedDevice);
                    Info.showAlert(StringConfig.COPY_CREATED_ALERT_TITLE, StringConfig.COPY_CREATED_ALERT_BODY,
                            null, Info.TYPE.INFORMATION);
                    }catch (Exception ex){
                        Info.showAlert(StringConfig.NO_CONNECTED_DEVICE_ALERT_TITLE, StringConfig.NO_CONNECTED_DEVICE_ALERT_BODY,
                                null, Info.TYPE.ERROR);
                        updateDevices(new ArrayList());
                    }

                }
                else{
                    Info.showAlert(StringConfig.MISSING_FIELDS_ALERT_TITLE, StringConfig.MISSING_FIELDS_ALERT_BODY,
                            null, Info.TYPE.WARNING);
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
