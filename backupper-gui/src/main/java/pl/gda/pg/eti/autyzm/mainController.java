package pl.gda.pg.eti.autyzm;

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

    @FXML public Label directoryRefreshInputLabel;
    @FXML private Button chooseDeviceButton;
    @FXML private BorderPane root;
    @FXML private Label directoryDownloadInputLabel;
    @FXML private TextField nameInput;

    @FXML private TableView<DeviceCopy> tableView;
    @FXML private TableColumn<DeviceCopy, String> name;
    @FXML private TableColumn<DeviceCopy, String> timestamp;
    private ObservableList<DeviceCopy> deviceCopyData = FXCollections.observableArrayList();


    @FXML
    public void chooseDeviceToDownload(ActionEvent actionEvent) {
        File choosenDirectory = showDirectoryChooserDialog(Config.CHOOSE_DEVICE_TO_DOWNLOAD);
        if(choosenDirectory != null)
            directoryDownloadInputLabel.setText((String)choosenDirectory.getAbsolutePath());
    }

    @FXML
    public void chooseDeviceToRefresh(ActionEvent actionEvent) {
        File choosenDirectory = showDirectoryChooserDialog(Config.CHOOSE_DEVICE_TO_REFRESH);
        if(choosenDirectory != null)
            directoryRefreshInputLabel.setText((String)choosenDirectory.getAbsolutePath());
    }

    @FXML
    public void download(ActionEvent actionEvent) {

        String name = nameInput.getText();
        deviceCopyData.add(new DeviceCopy(name, LocalDate.now()));
        //download
    }

    @FXML
    public void refresh(ActionEvent actionEvent) {
        //refresh
    }

    private File showDirectoryChooserDialog(String dialogTitle) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(dialogTitle);
        Stage stage = (Stage) root.getScene().getWindow();
        return directoryChooser.showDialog(stage);
    }

    @FXML
    public void initialize() {
        name.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        timestamp.setCellValueFactory(cellData -> cellData.getValue().getCreateDateProperty());

        tableView.setItems(deviceCopyData);
    }

}
