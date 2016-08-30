package pl.gda.pg.eti.autyzm.Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import pl.gda.pg.eti.autyzm.Config;
import se.vidstige.jadb.JadbDevice;

import java.util.List;

public class DeviceToRefreshTable extends Table {

    private static final double DEVICE_COLUMN_WIDTH = 0.9;
    private static final double CHOOSE_DEVICE_COLUMN_WIDTH = 0.1;
    private static final double MAX_TABLE_HEIGHT = 100.0;

    private JadbDevice selectedDevice;

    private TableView<JadbDevice> tableView;
    private TableColumn<JadbDevice, String> deviceColumn;
    private TableColumn<JadbDevice, Boolean> chooseDeviceColumn;
    private ObservableList<JadbDevice> devices = FXCollections.observableArrayList();

    public DeviceToRefreshTable(TableView<JadbDevice> tableView, TableColumn<JadbDevice, String> deviceColumn, TableColumn<JadbDevice, Boolean> chooseDeviceColumn, JadbDevice selectedDevice){
        this.tableView = tableView;
        this.deviceColumn = deviceColumn;
        this.chooseDeviceColumn = chooseDeviceColumn;

        // I probably should not share selected device to copies table this way
        this.selectedDevice = selectedDevice;

        setDataBindings();
        setColumnWidth();
        this.tableView.setItems(this.devices);
    }

    @Override
    void setDataBindings() {
        deviceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSerial()));
        chooseDeviceColumn.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        chooseDeviceColumn.setCellFactory(
                cellData -> new ChooseDeviceRadioBoxCell());
    }

    @Override
    void setColumnWidth() {
        tableView.setPrefWidth(Config.SCENE_WIDTH);
        deviceColumn.prefWidthProperty().bind(this.tableView.widthProperty().multiply(DEVICE_COLUMN_WIDTH));
        chooseDeviceColumn.prefWidthProperty().bind(this.tableView.widthProperty().multiply(CHOOSE_DEVICE_COLUMN_WIDTH));
        tableView.setMaxHeight(MAX_TABLE_HEIGHT);
    }

    public void updateDevices(List devices) {
        this.devices.clear();
        this.devices.addAll(devices);
        tableView.refresh();
    }

    private class ChooseDeviceRadioBoxCell extends TableCell<JadbDevice, Boolean> {
        final RadioButton chooseDeviceRadioBox = new RadioButton();

        ChooseDeviceRadioBoxCell(){
            chooseDeviceRadioBox.setOnAction(action -> {
                selectedDevice = devices.get(this.getIndex());
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(chooseDeviceRadioBox);
            }
        }
    }
}
