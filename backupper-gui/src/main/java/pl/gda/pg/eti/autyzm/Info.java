package pl.gda.pg.eti.autyzm;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class Info {
    public static void showAlert(String title, String contentText, Alert.AlertType alertType) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);

        // Allows long text to be wrapped.
        Label label = new Label(contentText);
        label.setWrapText(true);
        alert.getDialogPane().setContent(label);

        alert.setHeaderText(null);
        alert.setResizable(true);
        alert.showAndWait();
    }
}
