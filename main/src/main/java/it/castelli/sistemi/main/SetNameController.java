package it.castelli.sistemi.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.security.*;
import java.util.Base64;

public class SetNameController {

    @FXML
    private TextField pairKeyName;

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) pairKeyName.getScene().getWindow();
        stage.close();
    }

    @FXML
    void generateButton(ActionEvent event) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (pairKeyName.getText().isBlank()) {
            pairKeyName.setText("Keys " + MainController.getInstance().counterClass);
            MainController.getInstance().counterClass++;
        }
        MainController.getInstance().setGenerateKey(true);
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

        keyGen.initialize(1024, random);

        KeyPair pair = keyGen.generateKeyPair();
        MainController.getInstance().newKeys = new Keys(pairKeyName.getText(), Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()), Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded()));
        MainController.getInstance().newKeys.setPrv(pair.getPrivate());
        MainController.getInstance().newKeys.setPub(pair.getPublic());

        Stage stage = (Stage) pairKeyName.getScene().getWindow();
        stage.close();
    }

}
