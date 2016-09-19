package pl.gda.pg.eti.autyzm.Controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import pl.gda.pg.eti.autyzm.Config;
import pl.gda.pg.eti.autyzm.backupper.core.Db;
import pl.gda.pg.eti.autyzm.backupper.core.Models.Activity;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ActivityController extends BaseController {

    // temporary hardcoded
    private Path dbFilePath = Paths.get("data", "kopia", "commments2.db");
    private Boolean dbFileExists;

    @FXML private TableView<Activity> activityTableView;
    @FXML private TableColumn<Activity, String> nameColumn;
    @FXML private TableColumn<Activity, Boolean> selectColumn;

    private ObservableList<Activity> activities = FXCollections.observableArrayList();
    private Db db;

    public ActivityController () {
        File dbFile = dbFilePath.toFile();

        if (!dbFile.exists()) {
           System.out.println("unable to find activities, dbFile doesnt't exist:"
                    + dbFile.getAbsolutePath());
        }

        this.db = new Db(dbFile);
    }

    @FXML
    public void initialize() {
        setDataBindings();
        setColumnWidth();
        activityTableView.setItems(this.activities);
        showActivities();
    }

    public void updateActivites(List<Activity> activities){
        this.activities.clear();
        this.activities.addAll(activities);
        this.activityTableView.refresh();
    }

    @FXML
    public void refreshActivityList(ActionEvent actionEvent) {
        showActivities();
    }

    private void showActivities() {
        List<Activity> activities = new ArrayList<Activity>();
        try {
            activities = db.getAllActivities();
        } catch (Exception ex) {
            System.out.println("sql error:" + ex.getMessage());
        }
        updateActivites(activities);
    }

    private void setDataBindings() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        selectColumn.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue() != null));
        selectColumn.setCellFactory(cellData -> {
            return new CheckBoxTableCell();
        });
    }

    private void setColumnWidth() {
        activityTableView.setPrefWidth(Config.SCENE_WIDTH);
        nameColumn.prefWidthProperty().bind(activityTableView.widthProperty().multiply(0.7));
        selectColumn.prefWidthProperty().bind(activityTableView.widthProperty().multiply(0.3));
    }
}
