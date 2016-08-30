package pl.gda.pg.eti.autyzm.Model;

import javafx.scene.control.Alert;

/**
 * Created by malgorzatas on 28/08/16.
 */
public class Info {

    public enum TYPE {
        INFORMATION, WARNING, ERROR
    }

    public static void showAlert(String title, String contentText, String headerText, TYPE alertType) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(getType(alertType));
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private static Alert.AlertType getType(TYPE type){

        switch (type) {

            case WARNING:
                return Alert.AlertType.WARNING;
            case INFORMATION:
                return Alert.AlertType.INFORMATION;
            case ERROR:
                return Alert.AlertType.ERROR;

        }
        return Alert.AlertType.NONE;
    }
}
