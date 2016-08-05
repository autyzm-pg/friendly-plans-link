package pl.gda.pg.eti.autyzm;

import javafx.beans.property.SimpleBooleanProperty;
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

import java.io.File;
import java.time.LocalDate;

/**
 * Created by Gosia on 2016-06-22.
 */
public class MainController {

    @FXML private BorderPane root;

    @FXML private TextField nameInput;

    @FXML public Label directoryRefreshLabel;
    @FXML private Label directoryDownloadInputLabel;
    private String directoryRefreshPath = null;
    private String directoryDownloadPath = null;

    @FXML private TableView<DeviceCopy> tableView;
    @FXML private TableColumn<DeviceCopy, String> name;
    @FXML private TableColumn<DeviceCopy, String> createDate;
    @FXML private TableColumn<DeviceCopy, Boolean> refreshCopyAction;
    private ObservableList<DeviceCopy> deviceCopyData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        setTableColumnDataBindings();
        setTableColumnWidth();
        tableView.setItems(deviceCopyData);
    }

    @FXML
    public void chooseDeviceToDownload(ActionEvent actionEvent) {
        File chosenDirectory = showDirectoryChooserDialog(StringConfig.CHOOSE_DEVICE_TO_DOWNLOAD);
        if(chosenDirectory != null) {
            directoryDownloadPath = chosenDirectory.getAbsolutePath();
            directoryDownloadInputLabel.setText(chosenDirectory.getAbsolutePath());
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

    @FXML
    public void download(ActionEvent actionEvent) {
        String name = nameInput.getText();
        if(name != null && !name.equals("") && directoryDownloadPath != null) {
            //download
            nameInput.clear();
            deviceCopyData.add(new DeviceCopy(name, LocalDate.now()));
            showAlert(StringConfig.COPY_CREATED_ALERT_TITLE, StringConfig.COPY_CREATED_ALERT_BODY,
                    null, Alert.AlertType.INFORMATION);
        }
        else{
            showAlert(StringConfig.COPY_MISSING_FIELDS_ALERT_TITLE, StringConfig.COPY_MISSING_FIELDS_ALERT_BODY,
                    null, Alert.AlertType.WARNING);
        }

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
                    showAlert(StringConfig.COPY_MISSING_FIELDS_ALERT_TITLE, StringConfig.COPY_MISSING_FIELDS_ALERT_BODY,
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
