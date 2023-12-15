package ck.ckrc.erie.warchess.Controller;
import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.example.Miner;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.game.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartFrameController {

    @FXML
    private Button MultiPlayer;

    @FXML
    private Button SingalPlayer;

    @FXML
    private AnchorPane StartController;
    @FXML
    public void ChooseOneSide(MouseEvent event) throws Exception{
        Director.GetDirector().ChooseOneSide();
    }

    @FXML
    void SingalPlayerStart(MouseEvent event) {
        for(int i=0;i< Engine.playerNum;i++){
            Main.currentGameEngine.setPlayer(i, Player.getNewPlayer(i));
        }
        Director.GetDirector().gameStart();
    }

}

