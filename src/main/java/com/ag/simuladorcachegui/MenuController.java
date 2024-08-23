package com.ag.simuladorcachegui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

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

        Font fontP = Font.loadFont(getClass().getResourceAsStream("BreatheFire.ttf"), 85);
        escolhaPersonagemLabel.setFont(fontP);

        Font fontJ = Font.loadFont(getClass().getResourceAsStream("BreatheFire.ttf"), 40);
        labelJ1.setFont(fontJ);
        labelJ2.setFont(fontJ);
        labelJ3.setFont(fontJ);

        File fileJogadorButton1 = new File("src/images/jogador1.png");
        Image jogadorButtonImage1 = new Image(fileJogadorButton1.toURI().toString());
        ImageView view1 = new ImageView(jogadorButtonImage1);
        view1.setFitHeight(515);
        view1.setFitWidth(300);
        jogador1Button.setGraphic(view1);

        File fileJogadorButton2 = new File("src/images/jogador2.png");
        Image jogadorButtonImage2 = new Image(fileJogadorButton2.toURI().toString());
        ImageView view2 = new ImageView(jogadorButtonImage2);
        view2.setFitHeight(515);
        view2.setFitWidth(300);
        jogador2Button.setGraphic(view2);

        File fileJogadorButton3 = new File("src/images/jogador3.png");
        Image jogadorButtonImage3 = new Image(fileJogadorButton3.toURI().toString());
        ImageView view3 = new ImageView(jogadorButtonImage3);
        view3.setFitHeight(515);
        view3.setFitWidth(300);
        jogador3Button.setGraphic(view3);

        jogador1Button.setOnMouseEntered(e -> {
            view1.setScaleX(1.1);
            view1.setScaleY(1.1);
            jogador1Button.setStyle("-fx-effect: dropshadow(gaussian, #00ff04, 20, 0.8, 0, 0)");

            labelJ1.setStyle("-fx-effect: dropshadow(gaussian, #00ff15, 20, 0.5, 0, 0)");
            labelJ1.setScaleX(1.3);
            labelJ1.setScaleY(1.3);

            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(0.2);
            view1.setEffect(colorAdjust);

        });

        jogador1Button.setOnMouseExited(e -> {
            view1.setScaleX(1.0);
            view1.setScaleY(1.0);

            jogador1Button.setStyle("-fx-background-size: cover; -fx-effect: dropshadow(gaussian, #245400, 20, 0.1, 0, 0) ;-fx-padding: 0; -fx-background-color: transparent; -fx-border-color: transparent");
            labelJ1.setStyle("-fx-text-fill: #000000;");
            labelJ1.setScaleX(1.0);
            labelJ1.setScaleY(1.0);

            view1.setEffect(null);
        });

        jogador2Button.setOnMouseEntered(e -> {
            view2.setScaleX(1.1);
            view2.setScaleY(1.1);
            jogador2Button.setStyle("-fx-effect: dropshadow(gaussian, #00ff91, 20, 0.8, 0, 0)");

            labelJ2.setStyle("-fx-effect: dropshadow(gaussian, #00ccff, 20, 0.5, 0, 0)");
            labelJ2.setScaleX(1.3);
            labelJ2.setScaleY(1.3);

            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(0.2);
            view2.setEffect(colorAdjust);

        });

        jogador2Button.setOnMouseExited(e -> {
            view2.setScaleX(1.0);
            view2.setScaleY(1.0);

            jogador2Button.setStyle("-fx-background-size: cover; -fx-effect: dropshadow(gaussian, #00ffe1, 20, 0.1, 0, 0) ;-fx-padding: 0; -fx-background-color: transparent; -fx-border-color: transparent");
            labelJ2.setStyle("-fx-text-fill: #000000;");
            labelJ2.setScaleX(1.0);
            labelJ2.setScaleY(1.0);

            view2.setEffect(null);
        });
    }
}
