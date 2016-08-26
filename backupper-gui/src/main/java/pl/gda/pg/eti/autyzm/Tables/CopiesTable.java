package pl.gda.pg.eti.autyzm.Tables;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import pl.gda.pg.eti.autyzm.DeviceCopy;
import pl.gda.pg.eti.autyzm.StringConfig;
import se.vidstige.jadb.JadbDevice;

/**
 * Created by malgorzatas on 26/08/16.
 */
public class CopiesTable extends Table {

    private final JadbDevice selectedDeviceToRefresh;
    private TableView<DeviceCopy> tableView;
    private TableColumn<DeviceCopy, String> name;
    private TableColumn<DeviceCopy, String> createDate;
    private TableColumn<DeviceCopy, Boolean> refreshCopyAction;

    private ObservableList<DeviceCopy> deviceCopyData = FXCollections.observableArrayList();

    public CopiesTable(TableView<DeviceCopy> tableView, TableColumn<DeviceCopy, String> name, TableColumn<DeviceCopy, String> createDate, TableColumn<DeviceCopy, Boolean> refreshCopyAction,
                       JadbDevice selectedDeviceToRefresh){
        this.tableView = tableView;
        this.name = name;
        this.createDate = createDate;
        this.refreshCopyAction = refreshCopyAction;
        this.selectedDeviceToRefresh = selectedDeviceToRefresh;

        setDataBindings();
        setColumnWidth();
        tableView.setItems(this.deviceCopyData);
    }

    @Override
    void setDataBindings() {
        name.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        createDate.setCellValueFactory(cellData -> cellData.getValue().getCreateDateProperty());
        refreshCopyAction.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        refreshCopyAction.setCellFactory(
                cellData -> new RefreshButtonCell());
    }

    @Override
    void setColumnWidth() {
        name.prefWidthProperty().bind(tableView.widthProperty().multiply(0.5));
        createDate.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
        refreshCopyAction.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
    }

    private class RefreshButtonCell extends TableCell<DeviceCopy, Boolean> {
        final Button refreshButton = new Button(StringConfig.REFRESH_BUTTON);

        RefreshButtonCell(){

            refreshButton.setOnAction(action -> {
                if(selectedDeviceToRefresh != null) {
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

}
