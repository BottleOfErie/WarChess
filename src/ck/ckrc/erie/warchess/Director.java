package ck.ckrc.erie.warchess;


import ck.ckrc.erie.warchess.ui.GameOver;
import ck.ckrc.erie.warchess.ui.GameScene;
import ck.ckrc.erie.warchess.ui.StartFrame;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;

public class Director {
    public static final int width=900,height=700;
    private Stage stage;
    private GameScene gameScene=new GameScene();
    private GameOver overScene=new GameOver();
    private static Director director=new Director();

    public static Director GetDirector(){
        return director;
    }
    public void Init(Stage stage) throws Exception{
        stage.setResizable(false);
        stage.setTitle("WarChess");
        stage.setWidth(width);
        stage.setHeight(height);
        StartFrame.loadtoStartFrame(stage);
        initclose(stage);
        this.stage=stage;
        stage.show();
    }

    public void gameStart(){
        gameScene.Init(stage);
    }
    public void gameOver(){
        overScene.Init(stage);
    }
    public void ChooseOneSide() throws Exception{
        StartFrame.loadChooseOneSide(stage);
    }
    public void BackToStartFrame() throws Exception{
        StartFrame.loadtoStartFrame(stage);
    }
    public void initclose(Stage stage){
        Platform.setImplicitExit(false);
        stage.setOnCloseRequest(e->{
            e.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("退出游戏");
            alert.setHeaderText(null);
            alert.setContentText("是否退出游戏?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                Platform.exit();
            }
        });
    }
}
