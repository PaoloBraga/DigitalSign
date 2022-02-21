package it.castelli.sistemi.main;

import it.castelli.sistemi.main.documentManipulation.SignDocument;
import it.castelli.sistemi.main.documentManipulation.VerifyDocument;
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
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
            Desktop.getDesktop().browse(new URL("https://github.com/PaoloBraga/DigitalSign").toURI());
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
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Key", "*.key"));
        // Saving public key
        fileChooser.setTitle("Save public key");
        fileChooser.setInitialFileName(currentKeys.getName().trim() + "PublicKey");
        fileSaver = fileChooser.showSaveDialog(owner);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(currentKeys.getPub().getEncoded());
        if (fileSaver != null) {
            SaveFile(x509EncodedKeySpec.getEncoded(), fileSaver);
        }
        // Saving private key
        fileChooser.setTitle("Save private key");
        fileChooser.setInitialFileName(currentKeys.getName().trim() + "PrivateKey");
        fileSaver = fileChooser.showSaveDialog(owner);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(currentKeys.getPrv().getEncoded());
        if (fileSaver != null) {
            SaveFile(pkcs8EncodedKeySpec.getEncoded(), fileSaver);
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
    void signDocument() throws IOException, SignatureException, InvalidKeyException {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All types", "*"));
        fileChooser.setTitle("Open document to sign");
        File file = fileChooser.showOpenDialog(owner);
        FileInputStream fileInputStream = null;
        if (file != null)
        fileInputStream = new FileInputStream(file.getAbsolutePath());
        SignDocument signDocument = new SignDocument(currentKeys.getPrv(), currentKeys.getPub());
        fileChooser.setTitle("Save signed document");
        assert file != null;
        fileChooser.setInitialFileName(file.getName().substring(0,file.getName().indexOf(".")) + "Signed." + file.getName().substring(file.getName().indexOf(".")));
        fileSaver = fileChooser.showSaveDialog(owner);
        if (fileSaver != null) {
            SaveFile(signDocument.sign(fileInputStream), fileSaver);
        }
        statusLabel.setText("Signed document");

    }

    @FXML
    void verifyDocument() throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All types", "*"));
        fileChooser.setTitle("Open signature");
        File fileSignature = fileChooser.showOpenDialog(owner);
        fileChooser.setTitle("Open document to verify");
        File file = fileChooser.showOpenDialog(owner);
        FileInputStream fileInputStreamSignature = new FileInputStream(fileSignature.getAbsolutePath());
        FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
        VerifyDocument verifyDocument = new VerifyDocument(currentKeys.getPrv(), currentKeys.getPub());
        if (verifyDocument.verify(fileInputStreamSignature, fileInputStream)) {
            statusLabel.setText("Document verified");
        } else {
            statusLabel.setText("Document not verified");
        }
    }


    private void SaveFile(byte[] content, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
            fileOutputStream.write(content);
            fileOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
