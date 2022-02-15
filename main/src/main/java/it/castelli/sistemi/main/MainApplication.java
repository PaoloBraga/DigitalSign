package it.castelli.sistemi.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApplication extends Application {

    private static final String WINDOW_POSITION_X = "Window_Position_X";
    private static final String WINDOW_POSITION_Y = "Window_Position_Y";
    private static final double DEFAULT_X = 10;
    private static final double DEFAULT_Y = 10;
    private static final String NODE_NAME = "ViewSwitcher";


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        stage.setTitle("Key Manipulator");
        stage.setScene(scene);
        stage.show();

        // Pull the saved preferences and set the stage size and start location
        Preferences pref = Preferences.userRoot().node(NODE_NAME);
        double x = pref.getDouble(WINDOW_POSITION_X, DEFAULT_X);
        double y = pref.getDouble(WINDOW_POSITION_Y, DEFAULT_Y);
        stage.setX(x);
        stage.setY(y);

        // When the stage closes store the current size and window location.
        stage.setOnCloseRequest((final WindowEvent event) -> {
            Preferences preferences = Preferences.userRoot().node(NODE_NAME);
            preferences.putDouble(WINDOW_POSITION_X, stage.getX());
            preferences.putDouble(WINDOW_POSITION_Y, stage.getY());
        });

    }

    public static void main(String[] args) {
        launch();
    }
}