package it.castelli.sistemi.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OpenKeyController implements Initializable {

    private boolean loadingProgressPublic;
    private boolean loadingProgressPrivate;

    Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);


    private bytes[] publicKey;
    private bytes[] privateKey;

    FileChooser fileChooser = new FileChooser();
    File file;
    FileReader fileReader;
    BufferedReader bufferedReader;


    @FXML
    private Button load;

    @FXML
    private TextField pairName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Keys", "*.key"));
        loadingProgressPublic = false;
        loadingProgressPrivate = false;
        load.setDisable(true);

    }

    @FXML
    void cancelLoad(ActionEvent event) {
        Stage currentStage = (Stage) load.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void loadKeys(ActionEvent event) {
        if (pairName.getText().isBlank()) {
            pairName.setText("Keys " + MainController.getInstance().counterClass);
            MainController.getInstance().counterClass++;
        }
        MainController.getInstance().setLoadKey(true);
        MainController.getInstance().newKeys = new Keys(pairName.getText(), publicKey, privateKey);
        Stage currentStage = (Stage) load.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void loadPrivate(ActionEvent event) {
        Stage currentStage = (Stage) load.getScene().getWindow();
        currentStage.setAlwaysOnTop(false);
        fileChooser.setTitle("Open Private key");
        try {
            File file = openFile();
            if (file == null)
                return;
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            privateKey = new bytes[(int) file.length()];
            fileInputStream.read(privateKey);
            loadingProgressPrivate = true;
            checkLoadingProgress();
        } catch (IOException e) {
            e.printStackTrace();

        }
        currentStage.setAlwaysOnTop(true);
    }

    @FXML
    void loadPublic(ActionEvent event) {
        Stage currentStage = (Stage) load.getScene().getWindow();
        currentStage.setAlwaysOnTop(false);
        fileChooser.setTitle("Open Public key");
        try {
            File file = openFile();
            if (file == null)
                return;
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            publicKey = new bytes[(int) file.length()];
            fileInputStream.read(publicKey);
            loadingProgressPublic = true;
            checkLoadingProgress();
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentStage.setAlwaysOnTop(true);
    }

    @FXML
    private void checkLoadingProgress() {
        if (loadingProgressPublic && loadingProgressPrivate && load.isDisable()) {
            load.setDisable(false);
        }
    }

    private File openFile() throws IOException {
        file = fileChooser.showOpenDialog(owner);
        if (file == null)
            return null;
//        fileReader = new FileReader(file);
//        bufferedReader = new BufferedReader(fileReader);
        return file;
    }


}
