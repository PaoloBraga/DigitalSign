package it.castelli.sistemi.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

import static it.castelli.sistemi.main.MainController.lastUsedDirectory;

public class OpenKeyController implements Initializable {

    private boolean loadingProgressPublic;
    private boolean loadingProgressPrivate;

    Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);

    private byte[] publicKey;
    private byte[] privateKey;

    FileChooser fileChooser = new FileChooser();
    File file;


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
    void cancelLoad() {
        Stage currentStage = (Stage) load.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void loadKeys() {
        if (pairName.getText().isBlank()) {
            pairName.setText("Keys " + MainController.getInstance().counterClass);
            MainController.getInstance().counterClass++;
        }
        MainController.getInstance().setLoadKey(true);
        try {
            MainController.getInstance().newKeys = new Keys(pairName.getText(), publicKey, privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        Stage currentStage = (Stage) load.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void loadPrivate() {
        Stage currentStage = (Stage) load.getScene().getWindow();
        currentStage.setAlwaysOnTop(false);
        fileChooser.setTitle("Open Private key");
        try {
            File file = openFile();
            if (file == null)
                return;
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            privateKey = new byte[(int) file.length()];
            fileInputStream.read(privateKey);
            loadingProgressPrivate = true;
            checkLoadingProgress();
        } catch (IOException e) {
            e.printStackTrace();

        }
        currentStage.setAlwaysOnTop(true);
    }

    @FXML
    void loadPublic() {
        Stage currentStage = (Stage) load.getScene().getWindow();
        currentStage.setAlwaysOnTop(false);
        fileChooser.setTitle("Open Public key");
        try {
            File file = openFile();
            if (file == null)
                return;
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            publicKey = new byte[(int) file.length()];
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
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Key", "*.key"));
        if (lastUsedDirectory != null){
            fileChooser.setInitialDirectory(lastUsedDirectory);
        }
        file = fileChooser.showOpenDialog(owner);
        lastUsedDirectory = file;
        if (file == null)
            return null;
        return file;
    }


}
