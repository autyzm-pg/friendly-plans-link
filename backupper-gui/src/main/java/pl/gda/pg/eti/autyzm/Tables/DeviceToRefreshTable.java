package pl.gda.pg.eti.autyzm.Tables;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.gda.pg.eti.autyzm.Config;
import se.vidstige.jadb.JadbDevice;

import java.util.List;

/**
 * Created by malgorzatas on 26/08/16.
 */
public class DeviceToRefreshTable extends Table {

    private JadbDevice selectedDeviceToRefresh;
    @FXML private TableView<JadbDevice> deviceToRefreshTableView;
    @FXML private TableColumn<JadbDevice, String> deviceToRefresh;
    @FXML private TableColumn<JadbDevice, Boolean> chooseDeviceToRefresh;
    private ObservableList<JadbDevice> availableDevicesToRefresh = FXCollections.observableArrayList();

    public DeviceToRefreshTable(TableView<JadbDevice> deviceTableView, TableColumn<JadbDevice, String> device, TableColumn<JadbDevice, Boolean> chooseDevice, JadbDevice selectedDeviceToRefresh){
        this.deviceToRefreshTableView = deviceTableView;
        this.deviceToRefresh = device;
        this.chooseDeviceToRefresh = chooseDevice;
        this.selectedDeviceToRefresh = selectedDeviceToRefresh;

        setDataBindings();
        setColumnWidth();
        this.deviceToRefreshTableView.setItems(this.availableDevicesToRefresh);
    }

    @Override
    void setDataBindings() {
        deviceToRefresh.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSerial()));
        chooseDeviceToRefresh.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        chooseDeviceToRefresh.setCellFactory(
                cellData -> new ChooseDeviceRadioBoxCell());
    }

    @Override
    void setColumnWidth() {
        deviceToRefreshTableView.setPrefWidth(Config.SCENE_WIDTH);
        deviceToRefresh.prefWidthProperty().bind(this.deviceToRefreshTableView.widthProperty().multiply(0.9));
        chooseDeviceToRefresh.prefWidthProperty().bind(this.deviceToRefreshTableView.widthProperty().multiply(0.1));
        deviceToRefreshTableView.setMaxHeight(100.0);
    }

    public void updateDevices(List devices) {
        availableDevicesToRefresh.clear();
        availableDevicesToRefresh.addAll(devices);
    }

    private class ChooseDeviceRadioBoxCell extends TableCell<JadbDevice, Boolean> {
        final RadioButton chooseDeviceRadioBox = new RadioButton();

        ChooseDeviceRadioBoxCell(){
            chooseDeviceRadioBox.setOnAction(action -> {
                selectedDeviceToRefresh = availableDevicesToRefresh.get(this.getIndex());
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
