package pl.gda.pg.eti.autyzm.Tables;

import javafx.scene.control.Alert;

/**
 * Created by malgorzatas on 26/08/16.
 */
public abstract class Table {

    abstract void setDataBindings();
    abstract void setColumnWidth();

    protected void showAlert(String title, String contentText, String headerText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
