package ck.ckrc.erie.warchess;


import ck.ckrc.erie.warchess.game.Player;
import ck.ckrc.erie.warchess.ui.GameOver;
import ck.ckrc.erie.warchess.ui.GameScene;
import ck.ckrc.erie.warchess.ui.StartFrame;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class Director {
    public static final int width=1000,height=700;
    private boolean isGameStarted=false;
    public Stage stage;
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
        isGameStarted=false;
    }

    public void gameStart(){
        gameScene.Init(stage);
        isGameStarted=true;
        Main.currentGameEngine.setPlayer(0, Player.getNewPlayer(0));
        Main.currentGameEngine.setPlayer(1, Player.getNewPlayer(1));
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
            if(result.isPresent()&&result.get() == ButtonType.OK){
                Platform.exit();
            }
        });
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }
}
