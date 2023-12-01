package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.game.PlayAction;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameScene {
    private Canvas canvas = new Canvas(700,700);
    private GraphicsContext graphicsContext=canvas.getGraphicsContext2D();


    public void Init(Stage stage){
        AnchorPane anchorPane=new AnchorPane(canvas);
        anchorPane.setPrefWidth(900);
        anchorPane.setPrefHeight(700);
        canvas.setLayoutX(130);
        canvas.setLayoutY(5);
        drawchessmap();
        graphicsContext.setFill(Color.BLUE);
        Scene scene=new Scene(anchorPane,900,700);
        anchorPane.setLayoutX(0);
        anchorPane.setLayoutY(0);
        canvas.setOnMouseClicked(new PlayAction(graphicsContext));
        stage.setScene(scene);
    }
    private void drawchessmap(){
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(5);
        for(int i = 0; i <= 10; i++){
            graphicsContext.strokeLine(i*60, 0, i*60, 600);
            graphicsContext.strokeLine(0, i*60, 600, i*60);
        }
    }

}
