package ck.ckrc.erie.warchess.Controller;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.game.Player;
import ck.ckrc.erie.warchess.ui.CLIUserInterface;
import ck.ckrc.erie.warchess.ui.Play;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ChooseOneSideController {

    @FXML
    private Button BackToStartButton;

    @FXML
    private Button ClientButton;

    @FXML
    private Button ServerButton;

    @FXML
    void BackToStartFrame(MouseEvent event) throws Exception{
        Director.GetDirector().BackToStartFrame();
    }

    @FXML
    void StartGameAsClient(MouseEvent event) {
        Play.gamemodel=1;
        for(int i = 0; i< Engine.playerNum; i++){
            Main.currentGameEngine.setPlayer(i, Player.getNewPlayer(i));
        }
        Director.GetDirector().gameStart();
    }

    @FXML
    void StartGameAsServer(MouseEvent event) {
        Play.gamemodel=1;
        for(int i=0;i< Engine.playerNum;i++){
            Main.currentGameEngine.setPlayer(i, Player.getNewPlayer(i));
        }
        Director.GetDirector().gameStart();
    }

}

