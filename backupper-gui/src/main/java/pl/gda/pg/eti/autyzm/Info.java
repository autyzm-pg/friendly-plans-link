package pl.gda.pg.eti.autyzm;

import javafx.scene.control.Alert;

/**
 * Created by malgorzatas on 28/08/16.
 */
public class Info {
    
    public static void showAlert(String title, String contentText, String headerText, Alert.AlertType alertType) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
