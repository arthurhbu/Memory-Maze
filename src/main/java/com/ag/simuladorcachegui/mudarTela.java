package com.ag.simuladorcachegui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class mudarTela {

    public void mudarTela(String fxmlFile, Button button, String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage)button.getScene().getWindow();
//        scene.getStylesheets().add(getClass().getResource(path).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
}
