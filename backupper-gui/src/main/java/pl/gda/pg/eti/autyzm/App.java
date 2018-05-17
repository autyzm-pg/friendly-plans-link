package pl.gda.pg.eti.autyzm;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.nio.file.FileSystems;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import pl.gda.pg.eti.autyzm.Utils.*;
import pl.gda.pg.eti.autyzm.backupper.core.AdbProxy;
import static pl.gda.pg.eti.autyzm.backupper.core.Config.APPLICATION_PACKAGE_ON_DEVICE;

public class App extends Application {
    @Override
    public void start (Stage primaryStage) throws Exception {
        try {
            switch (OperatingSystemUtils.getOperatingSystem()) {
                case WINDOWS:
                    // environment variable PATH should be updated to contain directory with adb.exe 
                    
                    //checkIfCorrectAdbVersionIsInstalled();
                    //checkIfFriendlyPlansAreInstalled();
                    //output = OperatingSystemUtils.execCmd("adb shell pm list packages " + APPLICATION_PACKAGE_ON_DEVICE);
                    AdbProxy.execCmd("adb start-server");

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
        } 
        catch (RuntimeException exception) {
            Info.showAlert(
                    Strings.FAILED_TO_INIT_ADB_TITLE,
                    exception.getMessage(),
                    Alert.AlertType.ERROR
            );
            
            Platform.exit();
        } 
        catch (Exception exception) {
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
    
    private void checkIfCorrectAdbVersionIsInstalled(){
        
        try {
            String output = AdbProxy.execCmd("adb version");
            
            if(! output.equals("Android Debug Bridge version 1.0.31")){
                throw new RuntimeException(Strings.WRONG_ADB_VERSION_INSTALLED);
            }
            
        } catch (IOException ex) {
            throw new RuntimeException(Strings.ADB_NOT_INSTALLED);
        }
        
    }
    
    private void checkIfFriendlyPlansAreInstalled() throws IOException{
        
        String output = AdbProxy.execCmd("adb shell pm list packages " + APPLICATION_PACKAGE_ON_DEVICE);
        
        if(output.equals("")){
            throw new RuntimeException(Strings.APPLICATION_NOT_INSTALLED);
        }
        
    }
                    
}
