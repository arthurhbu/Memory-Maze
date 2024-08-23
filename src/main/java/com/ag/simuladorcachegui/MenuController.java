package com.ag.simuladorcachegui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MenuController extends mudarTela{

    @FXML
    private ImageView backgroundImageMenu;

    @FXML
    private Label escolhaPersonagemLabel;

    @FXML
    private Button jogador1Button;

    @FXML
    private Button jogador2Button;

    @FXML
    private Button jogador3Button;

    @FXML
    private Label labelJ1;

    @FXML
    private Label labelJ2;

    @FXML
    private Label labelJ3;

    @FXML
    private void telaJ1() throws IOException {
        mudarTela("/com/ag/simuladorcachegui/processador1.fxml", jogador1Button, "/processador1.css");
    }

    @FXML
    private void telaJ2() throws IOException {
        mudarTela("/com/ag/simuladorcachegui/processador2.fxml", jogador1Button, "/processador2.css");
    }

    @FXML
    private void telaJ3() throws IOException {
        mudarTela("/com/ag/simuladorcachegui/processador3.fxml", jogador2Button, "/processador3.css");
    }

    @FXML
    private void initialize() {

        File fileBackground = new File("src/images/CacheAdventure_menuScreen.png");
        Image imageBackground = new Image(fileBackground.toURI().toString());
        backgroundImageMenu.setImage(imageBackground);

        File fileJogador1Button = new File("src/images/jogador1.png");
        Image jogador1ButtonImage = new Image(fileJogador1Button.toURI().toString());
        ImageView view1 = new ImageView(jogador1ButtonImage);
        view1.setFitHeight(515);
        view1.setFitWidth(300);
        jogador1Button.setGraphic(view1);

        File fileJogador2Button = new File("src/images/jogador2.png");
        Image jogador2ButtonImage = new Image(fileJogador2Button.toURI().toString());
        ImageView view2 = new ImageView(jogador2ButtonImage);
        view2.setFitHeight(515);
        view2.setFitWidth(300);
        jogador2Button.setGraphic(view2);

        File fileJogador3Button = new File("src/images/jogador3.png");
        Image jogador3ButtonImage = new Image(fileJogador3Button.toURI().toString());
        ImageView view3 = new ImageView(jogador3ButtonImage);
        view3.setFitHeight(515);
        view3.setFitWidth(300);
        jogador3Button.setGraphic(view3);

        Font fontP = Font.loadFont(getClass().getResourceAsStream("BreatheFire.ttf"), 85);
        escolhaPersonagemLabel.setFont(fontP);

        Font fontJ = Font.loadFont(getClass().getResourceAsStream("BreatheFire.ttf"), 40);

        labelJ1.setFont(fontJ);
        labelJ2.setFont(fontJ);
        labelJ3.setFont(fontJ);

    }



}
