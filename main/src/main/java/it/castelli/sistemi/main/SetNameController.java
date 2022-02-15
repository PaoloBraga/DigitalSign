package it.castelli.sistemi.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SetNameController {

    @FXML
    private TextField pairKeyName;

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) pairKeyName.getScene().getWindow();
        stage.close();
    }

    @FXML
    void generateButton(ActionEvent event) {
        if (pairKeyName.getText().isBlank()) {
            pairKeyName.setText("Keys " + MainController.getInstance().counterClass);
            MainController.getInstance().counterClass++;
        }
        MainController.getInstance().setGenerateKey(true);
        // TODO: 15/02/2022 add the generated keys
//        MainController.getInstance().newKeys = new Keys(pairKeyName.getText(), ,);
        Stage stage = (Stage) pairKeyName.getScene().getWindow();
        stage.close();
    }

}
