package pl.gda.pg.eti.autyzm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.nio.file.FileSystems;
import pl.gda.pg.eti.autyzm.Utils.*;

public class App extends Application {
    @Override
    public void start (Stage primaryStage) throws Exception {
        try {
            switch (OperatingSystemUtils.getOperatingSystem()) {
                case WINDOWS:
                    Runtime.getRuntime().exec("adb start-server");
                    break;
                    
                case MAC:
                    String directorySeparator = FileSystems.getDefault().getSeparator();
                    String pathToLocalAdb = System.getProperty("user.dir") + directorySeparator + "adb";
                    Runtime.getRuntime().exec(pathToLocalAdb + " start-server");
                    break;

                case LINUX:
                    // On Linux, ADB should be installed globally (in /usr/bin, with udev rules, etc).
                    Runtime.getRuntime().exec("adb start-server");
                    break;

                case OTHER:
                default:
                    throw new Exception("Unsupported operating system.");
            }
        } catch (Exception exception) {
            Info.showAlert(
                    Strings.FAILED_TO_INIT_ADB_TITLE,
                    Strings.FAILED_TO_INIT_ADB_BODY,
                    Alert.AlertType.ERROR
            );
        }

        Parent root = FXMLLoader.load(getClass().getResource(Config.MAIN_FXML_PATH));
        primaryStage.getIcons().add(new Image(Config.LOGO_PATH));
        primaryStage.setTitle(Strings.APP_NAME);
        primaryStage.setScene(new Scene(root, Config.SCENE_WIDTH, Config.SCENE_HEIGHT));
        primaryStage.show();
    }

    public static void main (String[] args) {
        launch(args);
    }
}
