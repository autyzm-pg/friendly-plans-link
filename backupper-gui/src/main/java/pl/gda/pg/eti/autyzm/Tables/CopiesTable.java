package pl.gda.pg.eti.autyzm.Tables;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import pl.gda.pg.eti.autyzm.DeviceCopy;
import pl.gda.pg.eti.autyzm.Info;
import pl.gda.pg.eti.autyzm.StringConfig;
import se.vidstige.jadb.JadbDevice;


public class CopiesTable extends Table {

    private static final double CREATE_DATE_COLUMN_WIDTH = 0.3;
    private static final double REFRESH_COPY_COLUMN_WIDTH = 0.2;
    private static final double NAME_COLUMN_WIDTH = 0.5;

    private final JadbDevice selectedDeviceToRefresh;

    private TableView<DeviceCopy> tableView;
    private TableColumn<DeviceCopy, String> nameColumn;
    private TableColumn<DeviceCopy, String> createDateColumn;
    private TableColumn<DeviceCopy, Boolean> refreshCopyColumn;
    private ObservableList<DeviceCopy> copies = FXCollections.observableArrayList();

    public CopiesTable(TableView<DeviceCopy> tableView, TableColumn<DeviceCopy, String> name, TableColumn<DeviceCopy, String> createDate, TableColumn<DeviceCopy, Boolean> refreshCopy,
                       JadbDevice selectedDeviceToRefresh){
        this.tableView = tableView;
        this.nameColumn = name;
        this.createDateColumn = createDate;
        this.refreshCopyColumn = refreshCopy;

        // I probably should not share selected device with device table table this way
        this.selectedDeviceToRefresh = selectedDeviceToRefresh;

        setDataBindings();
        setColumnWidth();
        tableView.setItems(this.copies);
    }

    @Override
    void setDataBindings() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        createDateColumn.setCellValueFactory(cellData -> cellData.getValue().getCreateDateProperty());
        refreshCopyColumn.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        refreshCopyColumn.setCellFactory(
                cellData -> new RefreshButtonCell());
    }

    @Override
    void setColumnWidth() {
        nameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(NAME_COLUMN_WIDTH));
        createDateColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(CREATE_DATE_COLUMN_WIDTH));
        refreshCopyColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(REFRESH_COPY_COLUMN_WIDTH));
    }

    private class RefreshButtonCell extends TableCell<DeviceCopy, Boolean> {
        final Button refreshButton = new Button(StringConfig.REFRESH_BUTTON);

        RefreshButtonCell(){

            refreshButton.setOnAction(action -> {
                if(selectedDeviceToRefresh != null) {
                    Info.showAlert(StringConfig.COPY_REFRESHED_ALERT_TITLE, StringConfig.COPY_REFRESHED_ALERT_BODY,
                            null, Info.TYPE.INFORMATION);

                    //refresh
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
                setGraphic(refreshButton);
            }
        }
    }

}
