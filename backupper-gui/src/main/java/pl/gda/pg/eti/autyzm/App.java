package pl.gda.pg.eti.autyzm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception {
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
