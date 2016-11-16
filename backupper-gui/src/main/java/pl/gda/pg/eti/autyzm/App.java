package pl.gda.pg.eti.autyzm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.nio.file.FileSystems;


public class App extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {

            String directorySeparator = FileSystems.getDefault().getSeparator();
            String pathToLocalAdb = System.getProperty("user.dir") + directorySeparator + "adb";
            Runtime.getRuntime().exec(pathToLocalAdb + " start-server");
        }
        catch (Exception exception){
            Info.showAlert(StringConfig.FILED_TO_INIT_ADB_HEADER, StringConfig.FILED_TO_INIT_ADB_BODY,
                    null, Alert.AlertType.ERROR);
        }

        Parent root = FXMLLoader.load(getClass().getResource(Config.MAIN_FXML_PATH));
        primaryStage.getIcons().add(new Image(Config.LOGO_PATH));
        primaryStage.setTitle(StringConfig.APP_NAME);
        primaryStage.setScene(new Scene(root, Config.SCENE_WIDTH, Config.SCENE_HEIGHT));
        primaryStage.show();
    }

    public static void main( String[] args ) {
        launch(args);
    }

}
