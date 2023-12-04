package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.ChessClassLoader;
import ck.ckrc.erie.warchess.game.Map;
import ck.ckrc.erie.warchess.game.Play;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Set;

public class GameScene {
    public static final int canvasize=600,edgelength=canvasize/ Map.MapSize;
    private Canvas canvas = new Canvas(canvasize,canvasize );
    private GraphicsContext graphicsContext=canvas.getGraphicsContext2D();

    public static AnchorPane anchorPane;

    public static Scene gameScene;
    private Stage stage;

    private int classcount=5;

    private boolean[] isload=new boolean[10];

    public void Init(Stage stage){
        AnchorPane anchorPane=new AnchorPane(canvas);
        this.anchorPane=anchorPane;
        anchorPane.setPrefWidth(Director.width);
        anchorPane.setPrefHeight(Director.height);
        canvas.setLayoutX(100);
        canvas.setLayoutY(0);
        drawchessmap();
        Scene scene=new Scene(anchorPane,Director.width,Director.height);
        anchorPane.setLayoutX(0);anchorPane.setLayoutY(0);
        canvas.setOnMouseClicked(new Play(graphicsContext).setChessAction);
        makesettingbutton();
        stage.setScene(scene);
        this.stage=stage;
        this.gameScene=stage.getScene();
    }
    private void drawchessmap(){
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(5);
        for(int i = 0; i <= 10; i++){
            graphicsContext.strokeLine(i*edgelength, 0, i*edgelength, canvasize);
            graphicsContext.strokeLine(0, i*edgelength, canvasize, i*edgelength);
        }
    }
    private void makesettingbutton(){
        Button button=new Button("setting");
        button.setPrefSize(75, 30);
        anchorPane.getChildren().add(button);
        button.setLayoutX(10);button.setLayoutY(50);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Parent root=FXMLLoader.load(this.getClass().getResource("/Fxml/Setting.fxml"));
                    root.setId("setting");
                    stage.getScene().setRoot(root);
                    initClass();
                }catch (IOException e){e.printStackTrace();}
            }
        });
    }
    private void initClass(){

        VBox notloadlist=(VBox) stage.getScene().lookup("#NotLoadClassList");
        VBox loadedlist=(VBox) stage.getScene().lookup("#LoadedClassList");
        Set<String> classname= Main.chessClassLoader.getChessClassNames();
        for(var item:classname){
            Label label=new Label("classname["+item+"]");
            label.setPrefSize(200, 50);
            label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
            label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    double layout=label.getParent().getLayoutX();
                }
            });
                notloadlist.getChildren().add(label);
        }
    }

}
