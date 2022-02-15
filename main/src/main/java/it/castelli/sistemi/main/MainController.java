package it.castelli.sistemi.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public int counterClass = 0;
    private boolean loadKey = false;
    private boolean generateKey = false;
    public Keys newKeys;
    private Keys currentKeys;

    FileChooser fileChooser = new FileChooser();
    File fileSaver;

    public void setLoadKey(boolean loadKey) {
        this.loadKey = loadKey;
    }

    public void setGenerateKey(boolean generateKey) {
        this.generateKey = generateKey;
    }

    @FXML
    private ListView<Keys> keyListView;

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea privateKey;

    @FXML
    private TextArea publicKey;

    @FXML
    private Button saveKey;

    @FXML
    private Button signButton;

    @FXML
    private Button verifyButton;

    Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);

    private static MainController instance;

    public static MainController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        saveKey.setDisable(true);
        signButton.setDisable(true);
        verifyButton.setDisable(true);

    }

    @FXML
    void generateKey() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("setName.fxml"));
        Stage stage = new Stage();
        Scene sceneChoseName = new Scene(loader.load(), 343, 111);
        stage.setTitle("Chose name");
        setStage(stage, sceneChoseName);
        setGenerateKey(false);
        stage.setOnHiding(event -> {
            if (generateKey) {
                loadNewKey();
                setGenerateKey(false);
            }
        });

    }

    @FXML
    void help() {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/FilippoHoch/DigitalSign").toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadKey() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("openKey.fxml"));
        Stage stage = new Stage();
        Scene sceneOpenKey = new Scene(loader.load(), 238, 131);
        stage.setTitle("Load");
        setLoadKey(false);
        setStage(stage, sceneOpenKey);
        stage.setOnHiding(event -> {
            if (loadKey) {
                loadNewKey();
                setLoadKey(false);
            }
        });
    }

    private void setStage(Stage stage, Scene sceneOpenKey) {
        stage.setScene(sceneOpenKey);
        Stage currentStage = (Stage) saveKey.getScene().getWindow();
        stage.initOwner(currentStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
    }


    @FXML
    void saveKey() {
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Key", "*.key"));
        // Saving public key
        fileChooser.setTitle("Save public key");
        fileSaver = fileChooser.showSaveDialog(owner);
        fileChooser.setInitialFileName(currentKeys.getName().trim() + "PublicKey");
        if (fileSaver != null) {
            SaveFile(currentKeys.getPublicKey(), fileSaver);
        }
        // Saving private key
        fileChooser.setTitle("Save private key");
        fileChooser.setInitialFileName(currentKeys.getName().trim() + "PrivateKey");
        fileSaver = fileChooser.showSaveDialog(owner);
        if (fileSaver != null) {
            SaveFile(currentKeys.getPrivateKey(), fileSaver);
        }
        statusLabel.setText("keys saved");
    }

    @FXML
    void selectKey() {
        if (keyListView.getSelectionModel().getSelectedIndex() != -1) {
            saveKey.setDisable(false);
            signButton.setDisable(false);
            verifyButton.setDisable(false);
            currentKeys = keyListView.getSelectionModel().getSelectedItem();
            privateKey.setText(currentKeys.getPrivateKey());
            publicKey.setText(currentKeys.getPublicKey());
        }

    }

    @FXML
    private void loadNewKey() {
        keyListView.getItems().add(newKeys);
        statusLabel.setText("Added the key pair");
    }

    @FXML
    void signDocument() throws IOException {
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("All types", "*"));
        fileChooser.setTitle("Open document to sign");
        File file = fileChooser.showOpenDialog(owner);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
//        bufferedReader.lines();
        // TODO: 15/02/2022 add the sign algorithm
        fileChooser.setTitle("Save signed document");
        fileChooser.setInitialFileName(file.getName() + "Signed");
        fileSaver = fileChooser.showSaveDialog(owner);
        if (fileSaver != null) {
            // TODO: 15/02/2022 substitute 'text' with result of sign (pay attention of carriage return)
//            SaveFile('text', fileSaver);
        }
        statusLabel.setText("Signed document");

    }

    @FXML
    void verifyDocument() throws FileNotFoundException {
        fileChooser.setTitle("Open document to verify");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("All types", "*"));
        File file = fileChooser.showOpenDialog(owner);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
//        bufferedReader.lines();
        // TODO: 15/02/2022 add the verify algorithm and replace 'true' with the result of verification
        if (true) {
            statusLabel.setText("Document verified");
        } else {
            statusLabel.setText("Document not verified");
        }
    }


    private void SaveFile(String content, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
